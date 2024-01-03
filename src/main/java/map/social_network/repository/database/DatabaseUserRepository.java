package map.social_network.repository.database;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import map.social_network.domain.entities.User;
import map.social_network.repository.paging.Page;
import map.social_network.repository.paging.Pageable;
import map.social_network.repository.paging.Paginator;
import map.social_network.repository.paging.PagingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUserRepository extends DatabaseAbstractRepository<Long, User>  implements PagingRepository<Long, User> {

    public DatabaseUserRepository(String url, String username, String password) {
        super(url, username, password);
    }


    @Override
    protected String getFindOneQuery() {
        return "SELECT * FROM users WHERE id = ?";
    }

    @Override
    protected String getFindAllQuery() {
        return "SELECT * FROM users";
    }

    @Override
    protected String getSaveQuery() {
        return "INSERT INTO users(first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM users WHERE users.id = ?";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE users SET first_name=?, last_name=? WHERE id = ?";
    }


    @Override
    protected void setSaveParameters(PreparedStatement preparedStatement, User entity) {
        try {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setString(4, entity.getPassword());
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
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setLong(3, entity.getId());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.setString(5, entity.getPassword());

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
        Long id = resultSet.getLong("id");
        String firstName = resultSet.getString("first_name");
        String lastName = resultSet.getString("last_name");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        User user = new User(firstName, lastName, email, password);
        user.setId(id);
        return user;
    }


    @Override
    public Page<User> findAll(Pageable pageable) {
        List<User> entities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement( "SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?")) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
            }
            Paginator<User> paginator = new Paginator<User>(pageable, entities);
            return paginator.paginate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<User> findAll(Pageable pageable, User user) {
        return null;
    }
}