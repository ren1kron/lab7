package com.ren1kron.server.managers;

import com.ren1kron.common.models.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class DatabaseManager {
    private static final Logger logger = LogManager.getLogger(DatabaseManager.class);
    private static String DB_URL;
    private static String USER;
    private static String PASSWORD;
//    private static final String DB_URL = "jdbc:postgresql://localhost:5432/studs";
//    private static final String USER = "s409411";
//    private static final String PASSWORD = "***";
    private static final String INSERT_ORGANIZATION_SQL = "insert into organizations (full_name, annual_turnover, employees_count) VALUES (?, ?, ?)";
    private static final String INSERT_WORKER_SQL = "insert into workers (key, user_name, name, coordinates_x, coordinates_y, salary, start_date, position, status, organization_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?::position_enum, ?::status_enum, ?)";

//    private static final String INSERT_WORKER_SQL = "insert into workers (" +
////        "id," +
//        "user_id, " +
//        " key," +
//        " name, " +
//        "coordinates_x, " +
//        "coordinates_y, " +
////        "creation_date, " +
//        "salary, " +
//        "start_date, " +
//        "position, " +
//        "status, " +
//        "organization_full_name, " +
//        "organization_annual_turnover, " +
//        "organization_employees_count) " +
////        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // здесь версия, в которой стоит id, как параметр
////        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // а здесь не стоит
////        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // а здесь и creationDate нет
//        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // а здесь и creationDate нет
////        "VALUES (id, key, name, coordinates_x, coordinates_y, creation_date, salary, start_date, position, status, organization_full_name, organization_annual_turnover, organization_employees_count)";
////    private static final String SELECT_ALL_WORKERS_SQL = "select * from workers";
    private static final String SELECT_ALL_WORKERS_SQL = "select workers.id, key, user_name, name, coordinates_x as x, coordinates_y as y, creation_date, salary, start_date, position, status, o.full_name, o.annual_turnover, o.employees_count from workers left join organizations o on o.id = workers.organization_id";
    private static final String REMOVE_WORKER_BY_KEY_SQL = "delete from workers where key = ?";
    private static final String REMOVE_WORKERS_BY_USER_SQL = "delete from workers where user_name = ?";

    public DatabaseManager(String dBurl, String user, String password) {
        DB_URL = dBurl;
        USER = user;
        PASSWORD = password;
        logger.info("DatabaseManager initialized with URL: " + DB_URL);
    }
    public synchronized int addWorker(Worker worker) {
//    public void addWorker(Worker worker, int userId) {
        try (Connection connection = getConnection()) {
//            try (PreparedStatement orgStatement = connection.prepareStatement(INSERT_ORGANIZATION_SQL, Statement.RETURN_GENERATED_KEYS)) {
//                try (PreparedStatement statement = connection.prepareStatement(INSERT_WORKER_SQL, Statement.RETURN_GENERATED_KEYS)) {
            PreparedStatement orgStatement = connection.prepareStatement(INSERT_ORGANIZATION_SQL, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement statement = connection.prepareStatement(INSERT_WORKER_SQL, Statement.RETURN_GENERATED_KEYS);
            Integer orgId = null;
            if (worker.getOrganization() != null) {
                setOrganization(orgStatement, worker.getOrganization());
                orgStatement.executeUpdate();

                try (ResultSet generatedOrgKeys = orgStatement.getGeneratedKeys()) {
                    if (generatedOrgKeys.next()) {
                        orgId = generatedOrgKeys.getInt(1);

                    }
                }
            }
            setAttributes(statement, worker, orgId);
            statement.executeUpdate();
//                int rowsAffected = statement.executeUpdate();
//                if (rowsAffected > 0) {
//                    ResultSet generatedKeys = statement.getGeneratedKeys();
//                    if (generatedKeys.next()) return generatedKeys.getInt(1);
//                    else logger.error("Failed to retrieve generated keys after adding Element");
//                }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
//                    System.out.println(id);
                    worker.setId(id);
//                        worker.setId(generatedKeys.getInt(1));
                    return id;
                }
            }
//                }
//            }
        } catch (SQLException e) {
            logger.error("Error when adding element: " + e);
            e.printStackTrace();
        }
        return -1;
    }
    public synchronized Map<Integer, Worker> getWorkers() {
        Map<Integer, Worker> workers = new LinkedHashMap<>();
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_WORKERS_SQL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Worker worker = getWorkerFromDatabase(resultSet);
                workers.put(worker.getKey(), worker);
                logger.info("Loaded worker: " + worker);
            }
        } catch (SQLException e) {
            logger.error("Error while retrieving workers from the database", e);
        }
        return workers;
    }

    public synchronized boolean removeByKey(int key) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(REMOVE_WORKER_BY_KEY_SQL);
            statement.setInt(1, key);
//            statement.executeUpdate();
//            ResultSet resultSet = statement.executeQuery();
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) return true;
        } catch (SQLException e) {
            logger.error("Error while deleting worker from the database", e);
        }
        return false;
    }
    public synchronized void removeByUser(String username) {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(REMOVE_WORKERS_BY_USER_SQL);
            statement.setString(1, username);
            statement.executeUpdate();
//            int rowsAffected = statement.executeUpdate();
//            if (rowsAffected > 0) return true;
        } catch (SQLException e) {
            logger.error("Error while clearing users elements", e);
        }
    }


    private synchronized void setOrganization(PreparedStatement statement, Organization organization) throws SQLException {
        statement.setString(1, organization.getFullName());

        Integer annualTurnover = organization.getAnnualTurnover();
        if (annualTurnover != null) statement.setInt(2, organization.getAnnualTurnover());
        else statement.setNull(2, Types.INTEGER);

        statement.setInt(3, organization.getEmployeesCount());
    }
    private synchronized void setAttributes(PreparedStatement statement, Worker worker, Integer orgId) throws SQLException {
        statement.setInt(1, worker.getKey()); // key
//        statement.setInt(2, worker.getUserId()); // user_id
        statement.setString(2, worker.getUsername()); // user_name
        statement.setString(3, worker.getName()); // name
        statement.setFloat(4, worker.getCoordinates().getX()); // cords_x
        statement.setDouble(5, worker.getCoordinates().getY()); // cords_y
//        Timestamp creationDate = Timestamp.from(worker.getCreationDate().toInstant());
//        statement.setTimestamp(++i, creationDate);
        statement.setFloat(6, worker.getSalary()); // salary
        statement.setDate(7, Date.valueOf(worker.getStartDate())); // startDate
        statement.setString(8, worker.getPosition().name()); // position

        Status status = worker.getStatus();
        if (status != null) statement.setString(9, status.name()); // status mb null
        else statement.setNull(9, Types.VARCHAR);
        // org
//        Organization organization = worker.getOrganization();
//        if (organization != null) statement.setInt(10, orgId); // mb null
//        else statement.setNull(10, Types.INTEGER);
        if (orgId != null) statement.setInt(10, orgId); // mb null
        else statement.setNull(10, Types.INTEGER);
//        private static final String INSERT_WORKER_SQL = "insert into workers (key, user_name, name, coordinates_x, coordinates_y, salary, start_date, position, status, organization_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        statement.setString(++i, worker.getOrganization().getFullName());
//        statement.setInt(++i, worker.getOrganization().getAnnualTurnover());
//        statement.setInt(++i, worker.getOrganization().getEmployeesCount());
    }
//    private static Worker getWorkerFromDatabase(ResultSet resultSet) throws SQLException {
//        int id = resultSet.getInt("id");
//        int key = resultSet.getInt("key");
//        String username = resultSet.username("user_name");
//        String name = resultSet.username("name");
//        //
//        float coordinateX = resultSet.getFloat("coordinate_x");
//        double coordinateY = resultSet.getDouble("coordinate_y");
//        // здесь надо будет создавать новые корды, наверное
//        Coordinates coordinates = new Coordinates(coordinateX, coordinateY);
//        Timestamp creationDate = resultSet.getTimestamp("creationDate");
//        float salary = resultSet.getFloat("salary");
//        LocalDate startDate = resultSet.getDate("startDate").toLocalDate();
//        Position position = Position.valueOf(resultSet.username("position"));
//        Status status = Status.valueOf(resultSet.username("status"));
//        //
//        String organizationFullName = resultSet.username("organization_full_name");
//        int organizationAnnualTurnover = resultSet.getInt("organization_annual_turnover");
//        int organizationEmployeesCount = resultSet.getInt("organization_employees_count");
//        // здесь нужно будет создавать новую оргу, наверное
//        Organization organization = new Organization(organizationFullName, organizationAnnualTurnover, organizationEmployeesCount);
////        return new Worker(key, id, name, coordinates, creationDate, salary, startDate, position, status, organization);
//        Worker worker = new Worker(key, id, name, organization, position, status, salary, coordinates, creationDate, startDate);
//        worker.setUsername(username);
//        return worker;
//
//
//    }
    private static synchronized Worker getWorkerFromDatabase(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int key = resultSet.getInt("key");
        String username = resultSet.getString("user_name");
        String name = resultSet.getString("name");
        float coordinateX = resultSet.getFloat("x");
        double coordinateY = resultSet.getDouble("y");
        Coordinates coordinates = new Coordinates(coordinateX, coordinateY);
        Timestamp creationDate = resultSet.getTimestamp("creation_date");
        float salary = resultSet.getFloat("salary");
        LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
        Position position = Position.valueOf(resultSet.getString("position"));
        String statusString = resultSet.getString("status");
        Status status = (statusString != null) ? Status.valueOf(statusString) : null;

        String organizationFullName = resultSet.getString("full_name");
        int organizationAnnualTurnover = resultSet.getInt("annual_turnover");
        int organizationEmployeesCount = resultSet.getInt("employees_count");
        Organization organization = null;

        if (organizationFullName != null) {
            organization = new Organization(organizationFullName, organizationAnnualTurnover, organizationEmployeesCount);
        }

        Worker worker = new Worker(key, id, name, organization, position, status, salary, coordinates, creationDate, startDate);
        worker.setUsername(username);
        return worker;
    }

    private static synchronized Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
