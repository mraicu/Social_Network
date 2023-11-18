package map.social_network.repository.database;

import map.social_network.domain.Entity;
import map.social_network.domain.validators.Validator;
import map.social_network.repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class DatabaseAbstractRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {


    private Validator<E> validator;

    String url;
    String username;
    String password;

    public DatabaseAbstractRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }


    @Override
    public Optional<E> findOne(ID id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(getFindOneQuery())) {
            setFindOneParameters(statement, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public Iterable<E> findAll() {
        List<E> entities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(getFindAllQuery());
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                entities.add(mapResultSetToEntity(resultSet));
            }
            return entities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> save(E entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getSaveQuery(), Statement.RETURN_GENERATED_KEYS)) {
            setSaveParameters(preparedStatement, entity);
            int response = preparedStatement.executeUpdate();
            if (response == 0) {
                return Optional.ofNullable(entity);
            } else {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    setGeneratedId(entity, generatedKeys);
                }
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getDeleteQuery())) {
            Optional<E> foundEntity = findOne(id);
            setDeleteParameters(preparedStatement, id);
            int response = preparedStatement.executeUpdate();
            return response == 0 ? foundEntity : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> update(E entity) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(getUpdateQuery())) {
            setUpdateParameters(preparedStatement, entity);
            int response = preparedStatement.executeUpdate();
            return response == 0 ? Optional.of(entity) : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract E mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    protected abstract String getFindOneQuery();

    protected abstract String getFindAllQuery();

    protected abstract String getSaveQuery();

    protected abstract String getUpdateQuery();

    protected abstract String getDeleteQuery();

    protected abstract void setSaveParameters(PreparedStatement preparedStatement, E entity);

    protected abstract void setGeneratedId(E entity, ResultSet generatedKeys);

    protected abstract void setFindOneParameters(PreparedStatement preparedStatement, ID id) throws SQLException;

    protected abstract void setDeleteParameters(PreparedStatement preparedStatement, ID id) throws SQLException;

    protected abstract void setUpdateParameters(PreparedStatement preparedStatement, E entity);
}
