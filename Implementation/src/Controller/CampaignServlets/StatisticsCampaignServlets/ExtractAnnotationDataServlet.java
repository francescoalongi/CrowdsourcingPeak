package Controller.CampaignServlets.StatisticsCampaignServlets;

import Model.Annotation;
import Model.Database.DataSource;
import Model.Manager;
import Model.User;
import Model.Worker;
import Repository.PeakRepo;
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

@WebServlet(name = "ExtractAnnotationDataServlet")
public class ExtractAnnotationDataServlet extends HttpServlet {

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

            PeakRepo peakRepo = new PeakRepo();
            ArrayList<Annotation> annotations = peakRepo.loadAnnotation(dataSource, Integer.parseInt(request.getParameter("idPeak")), Integer.parseInt(request.getParameter("idCampaign")));
            Map<Worker, Annotation> workerAnnotationMap = new HashMap<>();
            if (annotations != null && !annotations.isEmpty()) {
                String loadWorkerQuery = "SELECT * FROM crowdsourcingpeak.annotation JOIN crowdsourcingpeak.worker ON annotation.idWorker = worker.idUser WHERE annotation.idAnnotation = ?";
                try (Connection conn = dataSource.getConnection()) {
                    PreparedStatement preparedStatement = null;
                    ResultSet resultSet = null;
                    for (Annotation annotation : annotations) {
                        preparedStatement = conn.prepareStatement(loadWorkerQuery);
                        preparedStatement.setInt(1, annotation.getIdAnnotation());
                        resultSet = preparedStatement.executeQuery();
                        while (resultSet.next()) {
                            Worker worker = new Worker(resultSet.getString("username"), resultSet.getInt("idUser"), resultSet.getString("email"), resultSet.getString("hash"));
                            workerAnnotationMap.put(worker, annotation);
                        }
                    }
                    preparedStatement.close();
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            request.setAttribute("workerAnnotationMap", workerAnnotationMap);
            request.getRequestDispatcher("/Manager/AnnotationDetails").forward(request, response);
        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }
}
