package map.social_network.repository.database;

import map.social_network.domain.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserRepository extends DatabaseAbstractRepository<Long, User> {

    public DatabaseUserRepository(String url, String username, String password) throws SQLException {
        super(url, username, password);
    }


    @Override
    protected String getFindOneQuery() {
        return "SELECT * FROM users WHERE user_id = ?";
    }

    @Override
    protected String getFindAllQuery() {
        return "SELECT * FROM users";
    }

    @Override
    protected String getSaveQuery() {
        return "INSERT INTO users(first_name, last_name) VALUES (?, ?)";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM users WHERE user_id = ?";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE Users SET first_name=?, last_name=?, WHERE user_id = ?";
    }


    @Override
    protected void setSaveParameters(PreparedStatement preparedStatement, User entity) {
        try {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setGeneratedId(User entity, ResultSet generatedKeys) {
        try {
            entity.setId(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, User entity) {
        try {
            preparedStatement.setLong(1, entity.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setFindOneParameters(PreparedStatement preparedStatement, Long id) {
        try {
            preparedStatement.setObject(1, id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setDeleteParameters(PreparedStatement preparedStatement, Long id) {
        try {
            preparedStatement.setObject(1, id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected User mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("user_id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        User user = new User(firstName, lastName);
        user.setId(id);
        return user;
    }

}