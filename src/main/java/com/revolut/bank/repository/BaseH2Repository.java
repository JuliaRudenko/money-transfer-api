package com.revolut.bank.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

abstract class BaseH2Repository<T> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseH2Repository.class);

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:revolut;DB_CLOSE_DELAY=-1;";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    abstract String getTableName();

    abstract List<T> mapResultSetToList(ResultSet resultSet) throws SQLException;

    abstract T mapResultSetToObject(ResultSet resultSet) throws SQLException;

    abstract String getInsertQuery();

    abstract void fillInsertPreparedStatement(PreparedStatement ps, T entity) throws SQLException;

    abstract String getUpdateQuery(T entity);

    abstract void fillUpdatePreparedStatement(PreparedStatement ps, T entity) throws SQLException;

    List<T> findAll() throws SQLException {
        try (Connection connection = getDBConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + getTableName());
        ) {
            ResultSet rs = ps.executeQuery();
            return mapResultSetToList(rs);
        } catch (SQLException e) {
            LOG.warn("Can't select all objects from H2 db! ", e.getMessage());
            throw e;
        }
    }

    T findById(Long id) throws SQLException {
        try (Connection connection = getDBConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM " + getTableName() + " WHERE id = ?");
        ) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            return mapResultSetToObject(rs);
        } catch (SQLException e) {
            LOG.warn("Can't select object by id from H2 db! ", e.getMessage());
            throw e;
        }
    }

    Long create(T entity) throws SQLException {
        try (Connection conn = getDBConnection();
             PreparedStatement ps = conn.prepareStatement(getInsertQuery(), Statement.RETURN_GENERATED_KEYS)
        ) {
            fillInsertPreparedStatement(ps, entity);
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            LOG.warn("Can't create object in H2 db! ", e.getMessage());
            throw e;
        }
        return 0L;
    }

    void update(T entity) throws SQLException {
        try (Connection conn = getDBConnection();
             PreparedStatement ps = conn.prepareStatement(getUpdateQuery(entity))
        ) {
            fillUpdatePreparedStatement(ps, entity);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.warn("Can't update object in H2 db! ", e.getMessage());
            throw e;
        }
    }

    void delete(Long id) throws SQLException {
        try (Connection conn = getDBConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM " + getTableName() + " WHERE id = ?")
        ) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.warn("Can't delete object in H2 db! ", e.getMessage());
            throw e;
        }
    }

    Connection getDBConnection() throws SQLException {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.warn("Can't find H2 driver! ", e.getMessage());
        }
        try {
            return DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            LOG.warn("Can't create H2 in-memory connection! ", e.getMessage());
            throw e;
        }
    }

    public void fillDbWithData() throws SQLException {
        try (Connection connection = getDBConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "RUNSCRIPT FROM 'classpath:db-scripts/createAndFillTables.sql'");
        ) {
            ps.executeUpdate();
        } catch (SQLException e) {
            LOG.warn("Can't fill H2 db with data! ", e.getMessage());
            throw e;
        }
    }
}