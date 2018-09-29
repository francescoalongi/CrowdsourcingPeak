package Repository;

import Model.Annotation;
import Model.Database.DataSource;
import Model.LocalizedName;
import Model.Peak;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PeakRepo {

    public ArrayList<Annotation> loadAnnotation (DataSource dataSource, Integer idPeak, Integer idCampaign) {
        String loadAnnotationQuery = "SELECT * FROM crowdsourcingpeak.annotation WHERE annotation.idpeak = ? AND annotation.idCampaign = ?";
        ArrayList<Annotation> annotations = new ArrayList<>();
        try (Connection connAnnotation = dataSource.getConnection();
             PreparedStatement loadAnnotationPS = connAnnotation.prepareStatement(loadAnnotationQuery)) {

            loadAnnotationPS.setInt(1,idPeak);
            loadAnnotationPS.setInt(2,idCampaign);
            ResultSet annotationsRS = loadAnnotationPS.executeQuery();
            while (annotationsRS.next()) {
                String loadLocalizedNamesQuery = "SELECT * FROM crowdsourcingpeak.annotationlocalizedname JOIN" +
                        " crowdsourcingpeak.localizedname ON annotationlocalizedname.idlocalizedname = " +
                        "localizedname.idlocalizedname WHERE annotationlocalizedname.idannotation = ?";
                ArrayList<LocalizedName> localizedNames = new ArrayList<>();
                try (Connection connLocNames = dataSource.getConnection();
                     PreparedStatement loadLocalizedNamesPS = connLocNames.prepareStatement(loadLocalizedNamesQuery)) {

                    loadLocalizedNamesPS.setInt(1, annotationsRS.getInt("idAnnotation"));
                    ResultSet localizedNamesRS = loadLocalizedNamesPS.executeQuery();
                    while (localizedNamesRS.next()) {
                        LocalizedName localizedName = new LocalizedName(localizedNamesRS.getInt("idLocalizedName"),
                                localizedNamesRS.getString("country"), localizedNamesRS.getString("localizedName"));
                        localizedNames.add(localizedName);
                    }
                }

                Double elevation = annotationsRS.getDouble("elevation");
                if (annotationsRS.wasNull()) elevation = null;
                Annotation annotation = new Annotation(annotationsRS.getInt("idAnnotation"), idPeak,
                        annotationsRS.getInt("idWorker"),annotationsRS.getTimestamp("creationDate"),
                        annotationsRS.getBoolean("validity"), elevation,
                        annotationsRS.getString("name"), localizedNames, annotationsRS.getBoolean("state"));

                annotations.add(annotation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return annotations;
    }

    public ArrayList<LocalizedName> loadLocalizedName (DataSource dataSource, Integer idPeak) {
        ArrayList<LocalizedName> localizedNames = new ArrayList<>();
        String loadLocalizedNamesQuery = "SELECT * FROM crowdsourcingpeak.peaklocalizedname JOIN crowdsourcingpeak.localizedname ON peaklocalizedname.idLocalizedName = localizedname.idLocalizedName WHERE peaklocalizedname.idPeak = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement loadLocalizedNames = conn.prepareStatement(loadLocalizedNamesQuery)) {
            loadLocalizedNames.setInt(1, idPeak);
            ResultSet resultSet = loadLocalizedNames.executeQuery();
            while (resultSet.next()) {
                LocalizedName localizedName = new LocalizedName(resultSet.getInt("idLocalizedName"),
                        resultSet.getString("country"), resultSet.getString("localizedName"));
                localizedNames.add(localizedName);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return localizedNames;
    }
}
