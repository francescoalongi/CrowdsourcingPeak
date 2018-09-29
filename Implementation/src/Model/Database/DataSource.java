package Model.Database;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

public class DataSource {

    private static DataSource instance;
    private BasicDataSource basicDataSource;

    public static DataSource getInstance() {
        if (DataSource.instance == null) {
            DataSource.instance = new DataSource();
        }
        return DataSource.instance;
    }

    public Connection getConnection() throws SQLException {
        return this.basicDataSource.getConnection();
    }

    private DataSource() {
        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(DataSourceDetails.DB_DRIVER);
        basicDataSource.setUrl(DataSourceDetails.DB_URL);
        basicDataSource.setUsername(DataSourceDetails.DB_USER);
        basicDataSource.setPassword(DataSourceDetails.DB_PASSWORD);
    }


}
