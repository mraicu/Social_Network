package map.social_network.repository.database;

import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Friendship;
import map.social_network.utils.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DatabaseFriendshipRepository extends DatabaseAbstractRepository<Tuple<Long, Long>, Friendship> {

    public DatabaseFriendshipRepository(String url, String username, String password) {
        super(url, username, password);
    }


    @Override
    protected String getFindOneQuery() {
        return "SELECT * FROM friendship WHERE (left_user = ? AND right_user = ?) OR (left_user = ? AND right_user = ?);";
    }

    @Override
    protected String getFindAllQuery() {
        return "SELECT * FROM friendship";
    }

    @Override
    protected String getSaveQuery() {
        return "INSERT INTO friendship(local_date_time, left_user, right_user) VALUES (?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE friendship SET local_date_time = ?, WHERE left_user = ? AND right_user = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM friendship WHERE left_user = ? AND right_user = ?";
    }

    @Override
    protected void setSaveParameters(PreparedStatement preparedStatement, Friendship entity) {
        try {
            Utils utils = new Utils();
            preparedStatement.setDate(1, utils.localDateTimeToDate(entity.getDate()));
            preparedStatement.setLong(2, entity.getId().getLeft());
            preparedStatement.setLong(3, entity.getId().getRight());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setGeneratedId(Friendship entity, ResultSet generatedKeys) {
        try {
            Long idLeft = generatedKeys.getLong(2);
            Long idRight = generatedKeys.getLong(3);
            Tuple<Long, Long> compositeKey = new Tuple<>(idLeft, idRight);
            entity.setId(compositeKey);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setUpdateParameters(PreparedStatement preparedStatement, Friendship entity) {
        try {
            Utils utils = new Utils();
            preparedStatement.setDate(1, utils.localDateTimeToDate(entity.getDate()));
            preparedStatement.setLong(2, entity.getId().getLeft());
            preparedStatement.setLong(3, entity.getId().getRight());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setFindOneParameters(PreparedStatement preparedStatement, Tuple<Long, Long> id) {
        try {

            preparedStatement.setLong(1, id.getLeft());
            preparedStatement.setLong(2, id.getRight());
            preparedStatement.setLong(3, id.getRight());
            preparedStatement.setLong(4, id.getLeft());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setDeleteParameters(PreparedStatement preparedStatement, Tuple<Long, Long> id){
        try {

            preparedStatement.setLong(1, id.getLeft());
            preparedStatement.setLong(2, id.getRight());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Friendship mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Utils utils = new Utils();

        LocalDateTime localDateTime = utils.dateToLocalDateTime(resultSet.getDate("local_date_time"));
        Long leftUser = resultSet.getLong("left_user");
        Long rightUser = resultSet.getLong("right_user");

        Friendship friendship = new Friendship(leftUser, rightUser);
        friendship.setDate(localDateTime);
        return friendship;
    }
}