package Repository;

import Model.*;
import Model.Database.DataSource;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import java.sql.*;
import java.util.*;
import java.util.Date;

public class CampaignRepo {
    public ArrayList<Peak> getCampaignPeaksData(DataSource dataSource, Integer idCampaign){

        String loadPeaksDataQuery = "SELECT peak.idPeak, peak.givenIdPeak, peak.latitude,peak.longitude,peak.altitude,peak.name,peakbelongings.toBeAnnotated, peak.provenance FROM crowdsourcingpeak.peak JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak= peakbelongings.idPeak WHERE idCampaign=? ";
        ArrayList<Peak> peaks = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(loadPeaksDataQuery)){
                preparedStatement.setInt(1, idCampaign);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    // for each peak, it is needed to retrieve localized names too
                    String loadLocalizedNamesQuery = "SELECT localizedname.idLocalizedName, localizedname.country, localizedname.localizedName FROM crowdsourcingpeak.localizedname JOIN crowdsourcingpeak.peaklocalizedname ON localizedname.idLocalizedName = peakLocalizedName.idLocalizedName WHERE peakLocalizedName.idPeak = ?";
                    ArrayList<LocalizedName> localizedNames = new ArrayList<>();
                    try (Connection localizedNamesConnection = dataSource.getConnection();
                         PreparedStatement localizedNamesPreparedStatement = localizedNamesConnection.prepareStatement(loadLocalizedNamesQuery)) {
                        localizedNamesPreparedStatement.setInt(1, resultSet.getInt("idPeak"));
                        ResultSet localizedNamesResultSet = localizedNamesPreparedStatement.executeQuery();
                        while (localizedNamesResultSet.next()) {
                            LocalizedName localizedName = new LocalizedName(localizedNamesResultSet.getInt("idLocalizedName"), localizedNamesResultSet.getString("country"), localizedNamesResultSet.getString("localizedName"));
                            localizedNames.add(localizedName);
                        }
                    }
                    Peak peak = new Peak(resultSet.getInt("idPeak"), resultSet.getInt("givenIdPeak"), resultSet.getString("provenance"), resultSet.getDouble("longitude"),
                            resultSet.getDouble("latitude"), resultSet.getDouble("altitude"), resultSet.getString("name"), localizedNames, resultSet.getBoolean("toBeAnnotated"));
                    peaks.add(peak);
                }

            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return peaks;
    }

    public MultiKeyMap<MultiKey, Map<Integer, ArrayList<Integer>>> getExistingPeaks (Connection connection) {
        MultiKeyMap<MultiKey, Map<Integer, ArrayList<Integer>>> existingPeak = new MultiKeyMap<>();
        String getExistingPeakQuery = "SELECT * FROM crowdsourcingpeak.peak JOIN crowdsourcingpeak.peakbelongings ON peak.idPeak = peakbelongings.idPeak";
        try (PreparedStatement getExistingPeak = connection.prepareStatement(getExistingPeakQuery)) {
            ResultSet resultSet = getExistingPeak.executeQuery();
            while (resultSet.next()) {
                MultiKey mapKey = new MultiKey(resultSet.getInt("givenIdPeak"), resultSet.getString("provenance"));
                if (existingPeak.containsKey(mapKey)) {
                    Map<Integer, ArrayList<Integer>> campaignsPerPeaks = existingPeak.get(mapKey);
                    if (campaignsPerPeaks.containsKey(resultSet.getInt("idPeak"))) {
                        campaignsPerPeaks.get(resultSet.getInt("idPeak")).add(resultSet.getInt("idCampaign"));
                    } else {
                        ArrayList<Integer> idCampaigns = new ArrayList<>();
                        idCampaigns.add(resultSet.getInt("idCampaign"));
                        existingPeak.get(mapKey).put(resultSet.getInt("idPeak"), idCampaigns);
                    }
                } else {
                    Map<Integer, ArrayList<Integer>> campaignsPerPeaks = new HashMap<>();
                    ArrayList<Integer> idCampaigns = new ArrayList<>();
                    idCampaigns.add(resultSet.getInt("idCampaign"));
                    campaignsPerPeaks.put(resultSet.getInt("idPeak"), idCampaigns);
                    existingPeak.put(mapKey, campaignsPerPeaks);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return existingPeak;
    }

    public void insertPeaks (DataSource dataSource, Integer idCampaign, ArrayList<Peak> peaks) {

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            MultiKeyMap<MultiKey, Map<Integer, ArrayList<Integer>>> existingPeak = this.getExistingPeaks(connection);
            StringBuffer insertPeakQuerySB = new StringBuffer("INSERT INTO crowdsourcingpeak.peak (givenIdPeak, latitude,longitude,altitude,name, provenance) VALUES ");
            StringBuffer insertLocalizedNameQuerySB = new StringBuffer("INSERT INTO crowdsourcingpeak.localizedname (country, localizedName) VALUES ");
            StringBuffer insertPeakBelongingsQuerySB = new StringBuffer("INSERT INTO crowdsourcingpeak.peakbelongings (idPeak, idCampaign, insertionDate, toBeAnnotated) VALUES ");
            StringBuffer insertPeakLocalizedNameQuerySB = new StringBuffer("INSERT INTO crowdsourcingpeak.peaklocalizedname (idPeak, idLocalizedName) VALUES ");
            String getLastInsertIdQuery = "SELECT LAST_INSERT_ID() AS last_id";
            Boolean peakQueryEmpty = true;
            Boolean localizedNameQueryEmpty = true;
            Boolean peakBelongingsQueryEmpty = true;
            ArrayList<Peak> alreadyExistingPeaks = new ArrayList<>();
            ArrayList<Peak> peaksWithLocalizedNames = new ArrayList<>();
            for (Peak peak : peaks) {
                MultiKey peakInfo = new MultiKey(peak.getGivenIdPeak(), peak.getProvenance());
                if (!existingPeak.containsKey(peakInfo)) {
                    if (peak.getName() != null)
                        peak.setName(peak.getName().replace('\"', '\''));

                    if (peak.getAltitude() != null && peak.getName() != null)
                        insertPeakQuerySB.append("(" + peak.getGivenIdPeak() + "," + peak.getLatitude() + "," + peak.getLongitude() + ","
                                + peak.getAltitude() + ",\"" + peak.getName() + "\",'" + peak.getProvenance() + "')");
                    else if (peak.getAltitude() != null)
                        insertPeakQuerySB.append("(" + peak.getGivenIdPeak() + "," + peak.getLatitude() + "," + peak.getLongitude() + ","
                                + peak.getAltitude() + ",NULL,'" + peak.getProvenance() + "')");
                    else if (peak.getName() != null)
                        insertPeakQuerySB.append("(" + peak.getGivenIdPeak() + "," + peak.getLatitude() + "," + peak.getLongitude() + ",NULL,\""
                                + peak.getName() + "\",'" + peak.getProvenance() + "')");
                    else if (peak.getAltitude() == null && peak.getName() == null)
                        insertPeakQuerySB.append("(" + peak.getGivenIdPeak() + "," + peak.getLatitude() + "," + peak.getLongitude() + ",NULL,NULL,'" + peak.getProvenance() + "')");


                    ArrayList<LocalizedName> localizedNames = peak.getLocalizedNames();
                    if (localizedNames != null && !localizedNames.isEmpty()) {
                        peaksWithLocalizedNames.add(peak);
                        for (LocalizedName localizedName : localizedNames) {
                            insertLocalizedNameQuerySB.append("(\"" + localizedName.getCountry() + "\",\"" + localizedName.getLocalizedName() + "\")");
                            insertLocalizedNameQuerySB.append(",");
                            localizedNameQueryEmpty = false;
                        }
                    }

                    insertPeakQuerySB.append(",");
                    peakQueryEmpty = false;
                } else {
                    Map<Integer, ArrayList<Integer>> campaignsPerPeak = existingPeak.get(peakInfo);
                    Map.Entry<Integer, ArrayList<Integer>> entry = campaignsPerPeak.entrySet().iterator().next();
                    if (!campaignsPerPeak.get(entry.getKey()).contains(idCampaign)) {
                        //It is not safe to remove an object from a collection while iterating it!
                        peak.setIdPeak(entry.getKey());
                        alreadyExistingPeaks.add(peak);
                    } else {
                        campaignsPerPeak.get(entry.getKey()).add(idCampaign);
                    }
                }
            }

            if (!peakQueryEmpty) {
                String insertPeakQuery = insertPeakQuerySB.toString();
                insertPeakQuery = insertPeakQuery.substring(0, insertPeakQuery.length() - 1);
                Integer peakRowCount = statement.executeUpdate(insertPeakQuery);
                ResultSet firstIdPeakRS = statement.executeQuery(getLastInsertIdQuery);
                Integer firstIdPeak = null;
                while (firstIdPeakRS.next()) {
                    firstIdPeak = firstIdPeakRS.getInt("last_id");
                }
                firstIdPeakRS.close();

                peaks.removeAll(alreadyExistingPeaks);
                // this loop give me the id of each inserted peak. So then I can insert a tuple into peakBelongings and peakLocalizedName
                for (int i = firstIdPeak, j = 0; i < firstIdPeak + peakRowCount; i++, j++) {
                    peaks.get(j).setIdPeak(i);
                    Timestamp insertionTimestamp = new java.sql.Timestamp(new Date().getTime());
                    insertPeakBelongingsQuerySB.append("(" + i + "," + idCampaign + ",\"" + insertionTimestamp + "\"," + peaks.get(j).getToBeAnnotated() + ")");
                    insertPeakBelongingsQuerySB.append(",");
                    peakBelongingsQueryEmpty = false;
                }

                if (!localizedNameQueryEmpty) {
                    String insertLocalizedNameQuery = insertLocalizedNameQuerySB.toString();
                    insertLocalizedNameQuery = insertLocalizedNameQuery.substring(0, insertLocalizedNameQuery.length() - 1);
                    Integer localizedRowCount = statement.executeUpdate(insertLocalizedNameQuery);
                    ResultSet firstIdLocalizedRS = statement.executeQuery(getLastInsertIdQuery);
                    Integer idLocalized = null;
                    while (firstIdLocalizedRS.next()) {
                        idLocalized = firstIdLocalizedRS.getInt("last_id");
                    }
                    firstIdLocalizedRS.close();

                    Integer iterator = idLocalized;
                    for (Peak peak : peaksWithLocalizedNames) {
                        for (LocalizedName localizedName : peak.getLocalizedNames()) {
                            if (iterator >= idLocalized + localizedRowCount) break;
                            insertPeakLocalizedNameQuerySB.append("(" + peak.getIdPeak() + "," + idLocalized + ")");
                            insertPeakLocalizedNameQuerySB.append(",");
                            idLocalized++;
                        }
                    }
                    String insertPeakLocalizedNameQuery = insertPeakLocalizedNameQuerySB.toString();
                    insertPeakLocalizedNameQuery = insertPeakLocalizedNameQuery.substring(0, insertPeakLocalizedNameQuery.length() - 1);
                    statement.executeUpdate(insertPeakLocalizedNameQuery);
                }

            }
            if (!alreadyExistingPeaks.isEmpty()) {
                for (Peak peak : alreadyExistingPeaks) {
                    Timestamp insertionTimestamp = new java.sql.Timestamp(new Date().getTime());
                    insertPeakBelongingsQuerySB.append("(" + peak.getIdPeak() + "," + idCampaign + ",\"" + insertionTimestamp + "\"," + peak.getToBeAnnotated() + ")");
                    insertPeakBelongingsQuerySB.append(",");
                    peakBelongingsQueryEmpty = false;
                }
            }
            if (!peakBelongingsQueryEmpty) {
                String insertPeakBelongingsQuery = insertPeakBelongingsQuerySB.toString();
                insertPeakBelongingsQuery = insertPeakBelongingsQuery.substring(0, insertPeakBelongingsQuery.length() - 1);
                statement.executeUpdate(insertPeakBelongingsQuery);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
