package map.social_network.repository.database;

import map.social_network.domain.Tuple;
import map.social_network.domain.entities.Friendship;
import map.social_network.domain.entities.User;
import map.social_network.repository.paging.Page;
import map.social_network.repository.paging.Pageable;
import map.social_network.repository.paging.Paginator;
import map.social_network.repository.paging.PagingRepository;
import map.social_network.utils.Utils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseFriendshipRepository extends DatabaseAbstractRepository<Tuple<Long, Long>, Friendship> implements PagingRepository<Tuple<Long, Long>, Friendship> {

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

    @Override
    public Page<Friendship> findAll(Pageable pageable) {
        List<Friendship> entities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement( "SELECT * FROM friendship ORDER BY left_user LIMIT ? OFFSET ?")) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
            }
            Paginator<Friendship> paginator = new Paginator<Friendship>(pageable, entities);
            return paginator.paginate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Friendship> findAll(Pageable pageable, User user) {
        List<Friendship> entities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement( "SELECT * FROM friendship WHERE left_user = ? OR right_user = ? ORDER BY left_user LIMIT ? OFFSET ?")) {

            statement.setLong(1, user.getId());
            statement.setLong(2,user.getId());
            statement.setInt(3, pageable.getPageSize());
            statement.setInt(4, pageable.getPageNumber() * pageable.getPageSize());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {

                    entities.add(mapResultSetToEntity(resultSet));
                }
            }
            Paginator<Friendship> paginator = new Paginator<Friendship>(pageable, entities);
            return paginator.paginate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}