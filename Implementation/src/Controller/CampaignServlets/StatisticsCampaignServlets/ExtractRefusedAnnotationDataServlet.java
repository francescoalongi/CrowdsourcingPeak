package Controller.CampaignServlets.StatisticsCampaignServlets;

import Model.*;
import Model.Database.DataSource;
import Utilities.Inspector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ExtractRefusedAnnotationDataServlet")
public class ExtractRefusedAnnotationDataServlet extends HttpServlet {
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
        if (user instanceof Manager && Inspector.checkPermissions(dataSource, user, Integer.parseInt(request.getParameter("idCampaign")))) {

            String loadRefusedAnnotationQuery = "SELECT * FROM crowdsourcingpeak.annotation" +
                    " WHERE annotation.idPeak = ? AND annotation.idCampaign = ? AND annotation.state = 0";

            String loadLocalizedNamesQuery = "SELECT * FROM crowdsourcingpeak.annotation JOIN crowdsourcingpeak.annotationlocalizedname ON " +
                    "annotation.idAnnotation = annotationlocalizedname.idAnnotation JOIN crowdsourcingpeak.localizedname ON " +
                    "annotationlocalizedname.idLocalizedName = localizedname.idLocalizedName WHERE annotation.idAnnotation = ?";

            String loadWorkerQuery = "SELECT * FROM crowdsourcingpeak.annotation JOIN crowdsourcingpeak.worker ON " +
                    "annotation.idWorker = worker.idUser WHERE annotation.idAnnotation = ?";

            Map<Worker, Annotation> workerAnnotationMap = new HashMap<>();
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement loadRefusedAnnotation = connection.prepareStatement(loadRefusedAnnotationQuery);
                 PreparedStatement loadLocalizedNames = connection.prepareStatement(loadLocalizedNamesQuery);
                 PreparedStatement loadWorker = connection.prepareStatement(loadWorkerQuery)) {
                loadRefusedAnnotation.setInt(1, Integer.parseInt(request.getParameter("idPeak")));
                loadRefusedAnnotation.setInt(2, Integer.parseInt(request.getParameter("idCampaign")));
                ResultSet loadRefusedAnnotationRS = loadRefusedAnnotation.executeQuery();
                while (loadRefusedAnnotationRS.next()) {
                    loadLocalizedNames.setInt(1, loadRefusedAnnotationRS.getInt("idAnnotation"));
                    ResultSet loadLocalizedNamesRS = loadLocalizedNames.executeQuery();
                    ArrayList<LocalizedName> localizedNames = new ArrayList<>();
                    while (loadLocalizedNamesRS.next()) {
                        LocalizedName localizedName = new LocalizedName(loadLocalizedNamesRS.getInt("idLocalizedName"),
                                loadLocalizedNamesRS.getString("country"), loadLocalizedNamesRS.getString("localizedName"));
                        localizedNames.add(localizedName);
                    }
                    loadLocalizedNamesRS.close();
                    Double elevation = loadRefusedAnnotationRS.getDouble("elevation");
                    if (loadRefusedAnnotationRS.wasNull()) elevation = null;
                    Annotation annotation = new Annotation(loadRefusedAnnotationRS.getInt("idAnnotation"), Integer.parseInt(request.getParameter("idPeak")),
                            loadRefusedAnnotationRS.getInt("idWorker"), loadRefusedAnnotationRS.getTimestamp("creationDate"),
                            loadRefusedAnnotationRS.getBoolean("validity"), elevation,
                            loadRefusedAnnotationRS.getString("name"), localizedNames, loadRefusedAnnotationRS.getBoolean("state"));
                    loadWorker.setInt(1, annotation.getIdAnnotation());
                    ResultSet loadWorkerRS = loadWorker.executeQuery();
                    while (loadWorkerRS.next()) {
                        Worker worker = new Worker(loadWorkerRS.getString("username"), loadWorkerRS.getInt("idUser"), loadWorkerRS.getString("email"), loadWorkerRS.getString("hash"));
                        workerAnnotationMap.put(worker, annotation);
                    }
                    loadWorkerRS.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            request.setAttribute("workerAnnotationMap", workerAnnotationMap);
            request.getRequestDispatcher("/Manager/AnnotationDetails").forward(request, response);
        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }
}
