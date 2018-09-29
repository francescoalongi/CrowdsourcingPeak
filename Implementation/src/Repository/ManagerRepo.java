package Repository;

import Model.Database.DataSource;
import Model.Campaign;
import Model.Manager;
import com.ja.security.PasswordHash;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerRepo {


    //TODO: create a datasource attribute
    public Integer insertCampaign (DataSource dataSource, Campaign campaign){
            String insertCampaignQuery = "INSERT INTO crowdsourcingpeak.campaign (name,state,startDate,endDate,idManager) " +
                    "VALUES (?,?,?,?,?)";
            String getIdCampaignQuery = "SELECT LAST_INSERT_ID() AS last_id";

            try (Connection connection = dataSource.getConnection();
                 PreparedStatement insertCampaign = connection.prepareStatement(insertCampaignQuery);
                 PreparedStatement getIdCampaign = connection.prepareStatement(getIdCampaignQuery)) {
                insertCampaign.setString(1, campaign.getName());
                insertCampaign.setString(2, campaign.getState());
                insertCampaign.setTimestamp(3, campaign.getStartDate());
                insertCampaign.setTimestamp(4, campaign.getEndDate());
                insertCampaign.setInt(5, campaign.getIdManager());
                insertCampaign.executeUpdate();

                ResultSet resultSet = getIdCampaign.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("last_id");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

    public ArrayList<Campaign> getCreatedCampaign(DataSource dataSource, Manager manager) {
        String requestCreatedCampaign = "SELECT * FROM Campaign WHERE Campaign.idManager = ?";
        ArrayList<Campaign> campaigns = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(requestCreatedCampaign)){
            preparedStatement.setInt(1, manager.getIdUser());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Campaign campaign = new Campaign(resultSet.getInt("idCampaign"), resultSet.getString("name"),
                        resultSet.getString("state"), resultSet.getTimestamp("startDate"), resultSet.getTimestamp("endDate"), manager.getIdUser());
                campaigns.add(campaign);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return campaigns;
    }
}

