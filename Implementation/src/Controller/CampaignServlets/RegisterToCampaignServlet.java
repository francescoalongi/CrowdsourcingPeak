package Controller.CampaignServlets;

import Model.Database.DataSource;
import Model.User;
import Model.Worker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RegisterToCampaignServlet extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        this.dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User) request.getSession(false).getAttribute("user");
        if (user instanceof Worker) {
            String insertPartecipationStatement = "INSERT INTO `crowdsourcingpeak`.`partecipation` (`idCampaign`, `idWorker`, `subscriptionDate`) VALUES (?, ?, ?)";

            try (Connection connection = this.dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(insertPartecipationStatement)) {

                preparedStatement.setInt(1, (Integer.parseInt(request.getParameter("idCampaign"))));
                preparedStatement.setInt(2, user.getIdUser());
                Date subDate = new Date();
                Timestamp subscriptionDateTime = new java.sql.Timestamp(subDate.getTime());
                preparedStatement.setTimestamp(3, subscriptionDateTime);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            request.getRequestDispatcher("/LoadCampaignPeaksData?idCampaign=" + request.getParameter("idCampaign")).forward(request, response);
        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }
}
