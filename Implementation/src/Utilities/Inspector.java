package Utilities;

import Model.Database.DataSource;
import Model.Manager;
import Model.User;
import Model.Worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Inspector {
    public static Boolean checkPermissions (DataSource dataSource, User user, Integer idCampaign) {
        String isTheRightUserQuery;
        if (user instanceof Worker) {
            isTheRightUserQuery = "SELECT * FROM crowdsourcingpeak.partecipation WHERE partecipation.idCampaign = ? AND partecipation.idWorker = ?";
        } else if (user instanceof Manager) {
            isTheRightUserQuery = "SELECT * FROM crowdsourcingpeak.campaign WHERE campaign.idCampaign = ? AND campaign.idManager = ?";
        } else {
            return false;
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(isTheRightUserQuery)) {

            preparedStatement.setInt(1, idCampaign);
            preparedStatement.setInt(2, user.getIdUser());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
