package Controller.CampaignServlets.LoadCampaignInfoServlets;

import Model.Campaign;
import Model.Database.DataSource;
import Model.Manager;
import Model.User;
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

@WebServlet(name = "LoadCampaignDataServlet")
public class LoadCampaignDataServlet extends HttpServlet {

    DataSource dataSource;

    @Override
    public void init() throws ServletException {
        this.dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession(false).getAttribute("user");
        if (user instanceof Manager && Inspector.checkPermissions(dataSource, user , Integer.parseInt(request.getParameter("idCampaign")))){

            String loadCampaignDataQuery = "SELECT * FROM crowdsourcingpeak.campaign WHERE idCampaign = ?";
            try (Connection connection = this.dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(loadCampaignDataQuery)) {
                preparedStatement.setInt(1, Integer.parseInt(request.getParameter("idCampaign")));
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    request.setAttribute("nameCampaign", resultSet.getString("name"));
                    request.setAttribute("stateCampaign", resultSet.getString("state"));
                    request.setAttribute("startDateCampaign", resultSet.getTimestamp("startDate"));
                    request.setAttribute("endDateCampaign", resultSet.getTimestamp("endDate"));
                    request.getRequestDispatcher("/Manager/CampaignDetails").forward(request, response);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }
}
