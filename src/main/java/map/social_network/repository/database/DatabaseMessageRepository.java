package map.social_network.repository.database;

import map.social_network.domain.entities.Message;
import map.social_network.domain.entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class DatabaseMessageRepository extends DatabaseAbstractRepository<Long, Message> {
    DatabaseUserRepository userRepository;

    public DatabaseMessageRepository(String url, String username, String password) {
        super(url, username, password);
        userRepository = new DatabaseUserRepository(url, username, password);
    }

    @Override
    protected Message mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("message_id");
        Long from = resultSet.getLong("from_id");
        Long to = resultSet.getLong("to_id");
        String text = resultSet.getString("text");
        Long reply = resultSet.getLong("reply");
        Timestamp time= resultSet.getTimestamp("date");
        User userFrom = userRepository.findOne(from).get();
        User userto = userRepository.findOne(to).get();


        Message message = new Message(text, userFrom, userto, reply, time);
        message.setId(id);

        return message;
    }

    @Override
    protected void setSaveParameters(PreparedStatement preparedStatement, Message entity) {
        try {
            preparedStatement.setString(1, entity.getText());
            preparedStatement.setLong(2, entity.getFrom().getId());
            List<User> l = entity.getTo();
            preparedStatement.setLong(3, l.get(0).getId());
            preparedStatement.setTimestamp(4, entity.getDate());
            if (entity.getReplyId() != null) {
                preparedStatement.setLong(5, entity.getReplyId());
            } else {
                preparedStatement.setNull(5, 0);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setGeneratedId(Message entity, ResultSet generatedKeys) {
        try {
            entity.setId(generatedKeys.getLong(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setFindOneParameters(PreparedStatement preparedStatement, Long id) throws SQLException {
        try {
            preparedStatement.setObject(1, id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setDeleteParameters(PreparedStatement preparedStatement, Long id) throws SQLException {
        try {
            preparedStatement.setObject(1, id);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, Message entity) {
        try {
            preparedStatement.setString(1, entity.getText());
            preparedStatement.setLong(2, entity.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getFindOneQuery() {
        return "SELECT * FROM message WHERE message_id = ?";
    }

    @Override
    protected String getFindAllQuery() {
        return "SELECT * FROM message";
    }


    @Override
    protected String getSaveQuery() {
        return "INSERT INTO message(text,from_id,to_id,date,reply) VALUES (?,?,?,?,?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE message SET text=? WHERE message_id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM message WHERE message_id=?";
    }

}
