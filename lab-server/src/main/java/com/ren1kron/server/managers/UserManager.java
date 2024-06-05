package com.ren1kron.server.managers;

import com.ren1kron.common.network.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class UserManager {
    private static final Logger logger = LogManager.getLogger(UserManager.class);

    private static String DB_URL;
    private static String USER;
    private static String PASSWORD;
    private static final String INSERT_USER = "insert into users (login, password_hash, salt) VALUES (?, ?,?)";
    private static final String SELECT_USER = "select * from users where login = ?";
    public UserManager(String dbUrl, String user, String password) {
        UserManager.DB_URL = dbUrl;
        UserManager.USER = user;
        UserManager.PASSWORD = password;
    }

    public static boolean addUser(User user) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(INSERT_USER)) {
                user.setSalt(generateSalt());
                user.setPassword(hashPassword(user.getPassword(), user.getSalt()));
                setAttributes(statement, user);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
//                    ResultSet generatedKeys = statement.getGeneratedKeys();
//                    if (generatedKeys.next()) {
//                        return true;
//                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Error while adding user");
            e.printStackTrace();
        }
        return false;
    }
    public static boolean checkUser(User user) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(SELECT_USER)) {
                statement.setString(1, user.getUsername());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String databasePasswordHash = resultSet.getString("password_hash");
                    String salt = resultSet.getString("salt");

                    user.setPassword(hashPassword(user.getPassword(), salt));
//                    String controlPassword = salt + user.getPassword();
//
//                    MessageDigest md = MessageDigest.getInstance("SHA-256");
//                    byte[] controlPasswordHashed = md.digest(controlPassword.getBytes());
//                    String comparingPassHash = Base64.getEncoder().encodeToString(controlPasswordHashed);
                    return databasePasswordHash.equals(user.getPassword());
                }
            }
        } catch (SQLException e) {
            logger.error("Error while adding user");
            e.printStackTrace();
        }
        return false;
    }

    public static Connection getConnection() throws SQLException {
        logger.info("Connecting to database...");
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
    private static void setAttributes(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getSalt());
    }

    private static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
//        salt = Base64.getEncoder().encodeToString(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
    private static String hashPassword(String password, String salt) {
        try {
//            String saltedPassword = user.getPassword() + user.getSalt();
            String saltedPassword = password + salt;


            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] passHashBytes = md.digest(saltedPassword.getBytes());
            return Base64.getEncoder().encodeToString(passHashBytes);
//            user.setPassword(Base64.getEncoder().encodeToString(passHashBytes));
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error while hashing password - where is no such algorithm to securing it");
            e.printStackTrace();
        }
        return null;
    }
//    public static void setDbUrl(String dbUrl) {
//        DB_URL = dbUrl;
//    }
}
