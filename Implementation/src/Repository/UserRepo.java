package Repository;

import Model.Database.DataSource;
import Model.Manager;
import Model.User;
import Model.Worker;
import com.ja.security.PasswordHash;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserRepo {

    public void updatePersonalData(DataSource dataSource, User user) throws SQLException {
        String updateDataQuery = "";
        if (user instanceof Manager)
           updateDataQuery = "UPDATE crowdsourcingpeak.manager SET ";
        if (user instanceof Worker)
            updateDataQuery = "UPDATE crowdsourcingpeak.worker SET ";

        if (user.getUsername() != null && !user.getUsername().equals("")) {
            updateDataQuery += "username = ?,";
        }
        if (user.getPassword() != null && !user.getPassword().equals("")) {
            updateDataQuery += "hash = ?,";
        }
        if (user.getEmail() != null && !user.getEmail().equals("")) {
            updateDataQuery += "email = ?,";
        }

        updateDataQuery = updateDataQuery.substring(0, updateDataQuery.length()-1);

        updateDataQuery += " WHERE idUser = ?";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateDataQuery)) {
            int i = 1;
            if (updateDataQuery.contains("username")) preparedStatement.setString(i++, user.getUsername());
            if (updateDataQuery.contains("hash")) preparedStatement.setString(i++, new PasswordHash().createHash(user.getPassword()));
            if (updateDataQuery.contains("email")) preparedStatement.setString(i++, user.getEmail());

            preparedStatement.setInt(i, user.getIdUser());
            preparedStatement.executeUpdate();

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

    }

}
