package Controller.CampaignServlets.LifecycleCampaignServlets;

import Model.Database.DataSource;
import Model.Manager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

@WebServlet(name = "CloseCampaignServlet")
public class CloseCampaignServlet extends HttpServlet {
    private DataSource dataSource;

    @Override
    public void init() throws ServletException {
        this.dataSource = DataSource.getInstance();
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false).getAttribute("user") instanceof Manager) {
            Manager manager = (Manager) request.getSession(false).getAttribute("user");
            String closeCampaignQuery = "UPDATE crowdsourcingpeak.campaign SET state = ?, endDate = ? WHERE idCampaign = ? AND idManager = ?";
            try (Connection connection = this.dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(closeCampaignQuery)){
                preparedStatement.setString(1,"CLOSED");
                Timestamp timestamp = new Timestamp(new Date().getTime());
                preparedStatement.setTimestamp(2, timestamp);
                preparedStatement.setInt(3, Integer.parseInt(request.getParameter("idCampaign")));
                preparedStatement.setInt(4, manager.getIdUser());
                preparedStatement.executeUpdate();
                response.sendRedirect("/CrowdsourcingPeak");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            response.sendRedirect("/CrowdsourcingPeak/GeneralError");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
