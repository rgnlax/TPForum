package ru.mp.forum.database;

import com.mysql.jdbc.CommunicationsException;
import com.mysql.jdbc.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by maksim on 08.01.16.
 */
@Configuration
public class DatabaseHelper {
    private static Connection connection;

    @Bean
    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }

        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder builder = new StringBuilder();
            builder.append("jdbc:mysql://localhost:3306/");
            builder.append("forum?");
            builder.append("user=f_usr&");
            builder.append("password=secret&");
            builder.append("useUnicode=true&characterEncoding=utf8&autoReconnect=true");

            connection = DriverManager.getConnection(builder.toString());

            return connection;
        } catch (CommunicationsException e) {
            System.out.println("Communication error. Check MySQL Server state.");
            e.printStackTrace();
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            System.out.println("Connection error. Check database configuration.");
            e.printStackTrace();
        }
        return null;
    }
}
