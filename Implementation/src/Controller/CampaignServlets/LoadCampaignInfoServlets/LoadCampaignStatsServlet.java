package Controller.CampaignServlets.LoadCampaignInfoServlets;

import Model.Database.DataSource;
import Model.Manager;
import Model.Peak;
import Model.User;
import Repository.PeakRepo;
import Repository.StatsDataStructures.StatsData;
import Utilities.Inspector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoadCampaignStatsServlet")
public class LoadCampaignStatsServlet extends HttpServlet {

    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession(false).getAttribute("user");
        if (user instanceof Manager && Inspector.checkPermissions(dataSource, user , Integer.parseInt(request.getParameter("idCampaign")))){

            try (Connection conn = dataSource.getConnection()) {

                PreparedStatement preparedStatement = conn.prepareStatement(StatsData.Query.NUM_PEAKS_NOT_ANNOTATED_YET);
                preparedStatement.setInt(1, Integer.parseInt(request.getParameter("idCampaign")));
                preparedStatement.setInt(2, Integer.parseInt(request.getParameter("idCampaign")));
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    request.setAttribute("num_peaks_not_annotated_yet", resultSet.getInt("NUM"));
                }

                preparedStatement = conn.prepareStatement(StatsData.Query.NUM_PEAKS_NOT_ANNOTABLE);
                preparedStatement.setInt(1, Integer.parseInt(request.getParameter("idCampaign")));
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    request.setAttribute("num_peaks_not_annotable", resultSet.getInt("NUM"));
                }

                preparedStatement = conn.prepareStatement(StatsData.Query.NUM_PEAKS_ANNOTATED);
                preparedStatement.setInt(1, Integer.parseInt(request.getParameter("idCampaign")));
                preparedStatement.setInt(2, Integer.parseInt(request.getParameter("idCampaign")));
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    request.setAttribute("num_peaks_annotated", resultSet.getInt("NUM"));
                }

                preparedStatement = conn.prepareStatement(StatsData.Query.NUM_PEAKS_ONE_OR_MORE_ANNOTATION_REFUSED);
                preparedStatement.setInt(1, Integer.parseInt(request.getParameter("idCampaign")));
                preparedStatement.setInt(2, Integer.parseInt(request.getParameter("idCampaign")));
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    request.setAttribute("num_peaks_one_or_more_annotation_refused", resultSet.getInt("NUM"));
                }

                preparedStatement = conn.prepareStatement(StatsData.Query.NUM_CONFLICTS);
                preparedStatement.setInt(1, Integer.parseInt(request.getParameter("idCampaign")));
                preparedStatement.setInt(2, Integer.parseInt(request.getParameter("idCampaign")));
                preparedStatement.setInt(3, Integer.parseInt(request.getParameter("idCampaign")));
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    request.setAttribute("num_conflicts", resultSet.getInt("NUM"));
                }

                PeakRepo peakRepo = new PeakRepo();

                preparedStatement = conn.prepareStatement(StatsData.Query.LIST_PEAKS_ANNOTATED);
                preparedStatement.setInt(1, Integer.parseInt(request.getParameter("idCampaign")));
                preparedStatement.setInt(2, Integer.parseInt(request.getParameter("idCampaign")));
                resultSet = preparedStatement.executeQuery();
                ArrayList<Peak> annotatedPeaks = new ArrayList<>();
                while (resultSet.next()) {
                    Peak peak = new Peak(resultSet.getInt("idPeak"), resultSet.getInt("givenIdPeak"),
                            resultSet.getString("provenance"), resultSet.getDouble("longitude"),
                            resultSet.getDouble("latitude"), resultSet.getDouble("altitude"),
                            resultSet.getString("name"), peakRepo.loadLocalizedName(dataSource, resultSet.getInt("idPeak")),
                            resultSet.getBoolean("toBeAnnotated"));
                    annotatedPeaks.add(peak);
                }
                request.setAttribute("annotatedPeaks", annotatedPeaks);

                preparedStatement = conn.prepareStatement(StatsData.Query.LIST_PEAKS_ONE_OR_MORE_ANNOTATION_REFUSED);
                preparedStatement.setInt(1, Integer.parseInt(request.getParameter("idCampaign")));
                preparedStatement.setInt(2, Integer.parseInt(request.getParameter("idCampaign")));
                resultSet = preparedStatement.executeQuery();
                ArrayList<Peak> peaksWithAnnotationRefused = new ArrayList<>();
                while (resultSet.next()) {
                    Peak peak = new Peak(resultSet.getInt("idPeak"), resultSet.getInt("givenIdPeak"),
                            resultSet.getString("provenance"), resultSet.getDouble("longitude"),
                            resultSet.getDouble("latitude"), resultSet.getDouble("altitude"),
                            resultSet.getString("name"), peakRepo.loadLocalizedName(dataSource, resultSet.getInt("idPeak")),
                            resultSet.getBoolean("toBeAnnotated"));
                    peaksWithAnnotationRefused.add(peak);
                }
                request.setAttribute("peaksWithAnnotationRefused", peaksWithAnnotationRefused);

                preparedStatement = conn.prepareStatement(StatsData.Query.LIST_CONFLICTS);
                preparedStatement.setInt(1, Integer.parseInt(request.getParameter("idCampaign")));
                preparedStatement.setInt(2, Integer.parseInt(request.getParameter("idCampaign")));
                preparedStatement.setInt(3, Integer.parseInt(request.getParameter("idCampaign")));
                resultSet = preparedStatement.executeQuery();

                // At the position 0 of the array of Integer will be placed the number of annotation with validity true,
                // otherwise at the position 1 will be placed the number of annotation with validity false.
                Map<Peak, Integer[]> peakConflictsValidityMap = new HashMap<>();
                while (resultSet.next()) {
                    Peak peak = new Peak(resultSet.getInt("idPeak"), resultSet.getInt("givenIdPeak"),
                            resultSet.getString("provenance"), resultSet.getDouble("longitude"),
                            resultSet.getDouble("latitude"), resultSet.getDouble("altitude"),
                            resultSet.getString("name"), peakRepo.loadLocalizedName(dataSource, resultSet.getInt("idPeak")),
                            resultSet.getBoolean("toBeAnnotated"));
                    Integer[] validity = new Integer[2];

                    ResultSet loadAnnotationValidityRS;
                    preparedStatement = conn.prepareStatement(StatsData.Query.ANNOTATION_VALIDITY);
                    preparedStatement.setBoolean(1, true);
                    preparedStatement.setInt(2, peak.getIdPeak());
                    preparedStatement.setInt(3, Integer.parseInt(request.getParameter("idCampaign")));
                    loadAnnotationValidityRS = preparedStatement.executeQuery();
                    while (loadAnnotationValidityRS.next()) {
                        validity[0] = loadAnnotationValidityRS.getInt("NUM");
                    }

                    preparedStatement = conn.prepareStatement(StatsData.Query.ANNOTATION_VALIDITY);
                    preparedStatement.setBoolean(1, false);
                    preparedStatement.setInt(2, peak.getIdPeak());
                    preparedStatement.setInt(3, Integer.parseInt(request.getParameter("idCampaign")));
                    loadAnnotationValidityRS = preparedStatement.executeQuery();
                    while (loadAnnotationValidityRS.next()) {
                        validity[1] = loadAnnotationValidityRS.getInt("NUM");
                    }
                    loadAnnotationValidityRS.close();

                    peakConflictsValidityMap.put(peak, validity);

                }
                request.setAttribute("peakConflictsValidityMap", peakConflictsValidityMap);

                preparedStatement.close();
                resultSet.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

            request.getRequestDispatcher("/Manager/CampaignDetails/CampaignStatistics").forward(request, response);

        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }
}
