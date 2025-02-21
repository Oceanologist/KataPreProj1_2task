package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }

    Connection connection = Util.getConnection();

    public void createUsersTable() {

        String sql = "CREATE TABLE kata_preproj_1_2task.USERS_TABLE (" +
                "  ID BIGINT NOT NULL AUTO_INCREMENT," +
                "  NAME VARCHAR(100) NOT NULL," +
                "  LAST_NAME VARCHAR(100) NOT NULL," +
                "  AGE INT(3) NOT NULL," +
                "  PRIMARY KEY (ID));";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            System.out.println("Таблица создана");
        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы");
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        String sql = "DROP TABLE USERS_TABLE ";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            System.err.println("Ошибка при удалении таблицы");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO USERS_TABLE ( NAME, LAST_NAME, AGE) VALUES (?, ?, ?)";
        if (connection == null) {
            System.err.println("Ошибка: соединение с базой данных не установлено.");
            return;
        }
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            int rowsInserted = statement.executeUpdate();
            System.out.println("Добавлено строк: " + rowsInserted);
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении данных:");
            e.printStackTrace();

        }
    }

    public void removeUserById(long id) {
        String sql = "DELETE FROM kata_preproj_1_2task.USERS_TABLE WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении данных");
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT ID, NAME, LAST_NAME, AGE FROM kata_preproj_1_2task.USERS_TABLE";

        if (connection == null) {
            System.err.println("Ошибка: соединение с базой данных не установлено.");
            return userList;
        }

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("ID"));
                user.setName(resultSet.getString("NAME"));
                user.setLastName(resultSet.getString("LAST_NAME"));
                user.setAge(resultSet.getByte("AGE"));
                userList.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении данных:");
        }

        return userList;
    }

    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE kata_preproj_1_2task.USERS_TABLE";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении всех данных из таблицы");
            e.printStackTrace();
        }

    }
}
