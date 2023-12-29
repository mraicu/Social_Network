package map.social_network.repository.database;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import map.social_network.domain.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUserRepository extends DatabaseAbstractRepository<Long, User> {

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
        return "DELETE FROM users WHERE id = ?";
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

//        Image img = new Image(getClass().getResource("view/images/email.png").toExternalForm());
//        ImageView view = new ImageView(img);
        Button btnAdd = new Button("+");
        Button btnSend = new Button("[]");
//        btnSend.setGraphic(view);
        user.setAddBtn(btnAdd);
        user.setSendMsg(btnSend);
        return user;
    }


}