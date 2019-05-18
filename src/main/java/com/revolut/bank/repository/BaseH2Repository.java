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
    private static final String DB_CONNECTION = "jdbc:h2:mem:revolut;DB_CLOSE_DELAY=-1;"
            + "INIT=runscript from 'classpath:db-scripts/createAndFillTables.sql'";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    abstract String getTableName();

    abstract List<T> mapResultSetToList(ResultSet resultSet) throws SQLException;

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

    private Connection getDBConnection() {
        Connection dbConnection;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            LOG.warn("Can't find H2 driver! ", e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;
        } catch (SQLException e) {
            LOG.warn("Can't create H2 in-memory connection! ", e.getMessage());
        }
        throw new RuntimeException("Can't create h2 in-memory db");
    }
}