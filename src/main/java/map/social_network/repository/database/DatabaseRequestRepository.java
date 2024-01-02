package map.social_network.repository.database;

import map.social_network.domain.StatusRequest;
import map.social_network.domain.entities.Request;
import map.social_network.domain.entities.User;
import map.social_network.repository.paging.Page;
import map.social_network.repository.paging.Pageable;
import map.social_network.repository.paging.Paginator;
import map.social_network.repository.paging.PagingRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRequestRepository extends DatabaseAbstractRepository<Long, Request> implements PagingRepository<Long, Request> {

    DatabaseUserRepository userRepository;

    public DatabaseRequestRepository(String url, String username, String password) {
        super(url, username, password);
        userRepository = new DatabaseUserRepository(url, username, password);
    }

    @Override
    protected Request mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("request_id");
        Long from = resultSet.getLong("from_user");
        User fromUser = userRepository.findOne(from).get();
        Long to = resultSet.getLong("to_user");
        User toUser = userRepository.findOne(to).get();
        String status = resultSet.getString("status");
        StatusRequest stR = null;
        switch (status) {
            case "pending":
                stR = StatusRequest.PENDING;
                break;
            case "approved":
                stR = StatusRequest.APPROVED;
                break;
            case "rejected":
                stR = StatusRequest.REJECTED;
                break;
        }


        Request request = new Request(fromUser, toUser, stR);
        request.setId(id);
        return request;
    }

    @Override
    protected String getFindOneQuery() {
        return "SELECT * FROM request WHERE request_id = ?";
    }

    @Override
    protected String getFindAllQuery() {
        return "SELECT * FROM request";
    }

    @Override
    protected String getSaveQuery() {
        return "INSERT INTO request(from_user, to_user) VALUES (?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE request SET status = ? WHERE request_id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM request WHERE request_id = ?";
    }

    @Override
    protected void setSaveParameters(PreparedStatement preparedStatement, Request entity) {
        try {
            preparedStatement.setLong(1, entity.getFrom().getId());
            preparedStatement.setLong(2, entity.getTo().getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void setGeneratedId(Request entity, ResultSet generatedKeys) {
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
    protected void setUpdateParameters(PreparedStatement preparedStatement, Request entity) {
        try {
            preparedStatement.setString(1, entity.getStatus().toString());
            preparedStatement.setLong(2, entity.getId());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Request> findAll(Pageable pageable) {
//        Paginator<Request> paginator = new Paginator<Request>(pageable, this.findAll());
//         paginator.paginate();

        List<Request> entities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement( "SELECT * FROM request ORDER BY request_id LIMIT ? OFFSET ?")) {
            statement.setInt(1, pageable.getPageSize());
            statement.setInt(2, pageable.getPageNumber() * pageable.getPageSize());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
            }
            Paginator<Request> paginator = new Paginator<Request>(pageable, entities);
            return paginator.paginate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<Request> findAll(Pageable pageable, User user) {
        List<Request> entities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement( "SELECT * FROM request WHERE to_user = ? ORDER BY request_id LIMIT ? OFFSET ?")) {
            statement.setLong(1, user.getId());
            statement.setInt(2, pageable.getPageSize());
            statement.setInt(3, pageable.getPageNumber() * pageable.getPageSize());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    entities.add(mapResultSetToEntity(resultSet));
                }
            }
            Paginator<Request> paginator = new Paginator<Request>(pageable, entities);
            return paginator.paginate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
