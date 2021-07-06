package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;

public class DatabaseConnectorPool {

    private static HikariDataSource dataSource;
    private static final String dbHostUrl = "pandemicpal-database.cb9bahjfarub.ca-central-1.rds.amazonaws.com";
    private static final String port = "3306";
    private static final String username = "admin";
    private static final String password = "password";

    //Call the get connection method.
    static Connection getConnection() throws SQLException {
        String query = "USE PandemicPalRDB";
        PreparedStatement preparedStatement = null;
        try {
            Connection connection = getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeQuery();
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        finally {
            try { preparedStatement.close(); } catch (Exception e) { System.out.println(e.getMessage()); }
        }
    }

    //Get the DataSource. If not available create the new one
    //It is not threadsafe. I didn't wanted to complicate things.
    private static DataSource getDataSource() {
        if (null == dataSource) {
            System.out.println("No DataSource is available. We will create a new one.");
            createDataSource();
        }
        return dataSource;
    }

    //To create a DataSource and assigning it to variable dataSource.
    private static void createDataSource() {
        HikariConfig hikariConfig = getHikariConfig();
        System.out.println("Configuration is ready.");
        System.out.println("Creating the HiakriDataSource and assigning it as the global");
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);

        dataSource = hikariDataSource;
    }

    //returns HikariConfig containing JDBC connection properties
    //which will be used by HikariDataSource object.
    private static HikariConfig getHikariConfig() {
        System.out.println("Creating the config with HikariConfig");
        HikariConfig hikaConfig = new HikariConfig();

        //This is same as passing the Connection info to the DriverManager class.
        //your jdbc url. in my case it is mysql.
        hikaConfig.setJdbcUrl("jdbc:mysql://" + dbHostUrl + ":" + port + "?useSSL=false");
        //username
        hikaConfig.setUsername(username);
        //password
        hikaConfig.setPassword(password);
        //driver class name
        hikaConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");

        // Information about the pool
        //pool name. This is optional you don't have to do it.
        hikaConfig.setPoolName("PandemicPalPool");

        //the maximum connection which can be created by or resides in the pool
        hikaConfig.setMaximumPoolSize(5);

        //how much time a user can wait to get a connection from the pool.
        //if it exceeds the time limit then a SQlException is thrown
        hikaConfig.setConnectionTimeout(Duration.ofSeconds(30).toMillis());

        //The maximum time a connection can sit idle in the pool.
        // If it exceeds the time limit it is removed form the pool.
        // If you don't want to retire the connections simply put 0.
        hikaConfig.setIdleTimeout(Duration.ofMinutes(2).toMillis());

        return hikaConfig;
    }

    public static void closeDataSource() {
        //System.out.println("entering close data source");
        if (dataSource != null) {
            dataSource.close();
        }
    }

}
