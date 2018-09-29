package Repository;

import Model.Campaign;
import Model.Database.DataSource;
import Model.Worker;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WorkerRepo {

    public ArrayList<Campaign> getRegisteredCampaigns(DataSource dataSource, Worker worker) {
        String requestRegisteredCampaigns = "SELECT crowdsourcingpeak.campaign.idCampaign,crowdsourcingpeak.campaign.name,crowdsourcingpeak.campaign.state, crowdsourcingpeak.campaign.startDate,crowdsourcingpeak.campaign.endDate,crowdsourcingpeak.campaign.idManager FROM crowdsourcingpeak.campaign inner join crowdsourcingpeak.partecipation on crowdsourcingpeak.campaign.idCampaign = crowdsourcingpeak.partecipation.idCampaign WHERE campaign.state='STARTED' AND crowdsourcingpeak.partecipation.idWorker=? ";
        ArrayList<Campaign> campaigns = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(requestRegisteredCampaigns)){
            preparedStatement.setInt(1, worker.getIdUser());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Campaign campaign = new Campaign(resultSet.getInt("idCampaign"), resultSet.getString("name"),
                        resultSet.getString("state"), resultSet.getTimestamp("startDate"), resultSet.getTimestamp("endDate"), resultSet.getInt("idManager"));
                campaigns.add(campaign);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campaigns;
    }

    public ArrayList<Campaign> getAvailableToRegisterCampaigns(DataSource dataSource,Worker worker) {
        String requestAvailableCampaigns = "SELECT * FROM crowdsourcingpeak.campaign WHERE campaign.state='STARTED' AND idCampaign NOT IN(SELECT idCampaign FROM crowdsourcingpeak.partecipation WHERE partecipation.idWorker = ?)";
        ArrayList<Campaign> campaigns = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(requestAvailableCampaigns)){
            preparedStatement.setInt(1,worker.getIdUser());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Campaign campaign = new Campaign(resultSet.getInt("idCampaign"), resultSet.getString("name"),
                        resultSet.getString("state"), resultSet.getTimestamp("startDate"), resultSet.getTimestamp("endDate"), resultSet.getInt("idManager"));
                campaigns.add(campaign);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campaigns;
    }

}
