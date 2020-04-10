package br.alysonsantos.com.core.data;

import java.sql.Connection;
import java.sql.SQLException;

public interface SQLDatabase {

    Credentials getCredentials();

    boolean isActive();

    Connection getConnection() throws SQLException;

    void openConnection();

    void makeTable();

    void closeConnection();
}
