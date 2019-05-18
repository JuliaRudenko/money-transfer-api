package com.revolut.bank.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

abstract class BaseH2Repository<T> {
    private static final Logger LOG = LoggerFactory.getLogger(BaseH2Repository.class);

    private static final String DB_DRIVER = "org.h2.Driver";
    private static final String DB_CONNECTION = "jdbc:h2:mem:revolut;DB_CLOSE_DELAY=-1;";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    BaseH2Repository() {
    }

    abstract String getTableName();

    abstract List<T> mapResultSetToList(ResultSet resultSet) throws SQLException;

    abstract T mapResultSetToObject(ResultSet resultSet) throws SQLException;

    List<T> findAll() throws SQLException {
        PreparedStatement selectPreparedStatement;
        String selectQuery = "select * from " + getTableName();

        try (Connection connection = getDBConnection()) {
            connection.setAutoCommit(false);

            selectPreparedStatement = connection.prepareStatement(selectQuery);
            ResultSet rs = selectPreparedStatement.executeQuery();
            List<T> list = mapResultSetToList(rs);
            selectPreparedStatement.close();
            connection.commit();
            return list;
        } catch (SQLException e) {
            LOG.warn("Can't select all objects from H2 db! ", e.getMessage());
            throw e;
        }
    }

    T findById(Long id) throws SQLException {
        PreparedStatement selectPreparedStatement;
        String selectQuery = "select * from " + getTableName() + " where id = ?";

        try (Connection connection = getDBConnection()) {
            connection.setAutoCommit(false);

            selectPreparedStatement = connection.prepareStatement(selectQuery);
            selectPreparedStatement.setLong(1, id);
            ResultSet rs = selectPreparedStatement.executeQuery();
            T row = mapResultSetToObject(rs);
            selectPreparedStatement.close();
            connection.commit();
            return row;
        } catch (SQLException e) {
            LOG.warn("Can't select object by id from H2 db! ", e.getMessage());
            throw e;
        }
    }

    private Connection getDBConnection() throws SQLException {
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

    void fillDbWithData() throws SQLException {
        PreparedStatement selectPreparedStatement;
        try (Connection connection = getDBConnection()) {
            connection.setAutoCommit(false);

            selectPreparedStatement = connection.prepareStatement(
                    "RUNSCRIPT FROM 'classpath:db-scripts/createAndFillTables.sql'");
            selectPreparedStatement.executeUpdate();

            selectPreparedStatement.close();
            connection.commit();
        } catch (SQLException e) {
            LOG.warn("Can't fill H2 db with data! ", e.getMessage());
            throw e;
        }
    }
}