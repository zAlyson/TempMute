package br.alysonsantos.com.core.data;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

import java.sql.*;

@Getter
@RequiredArgsConstructor
public class MySQLDatabase implements SQLDatabase {

    @NonNull
    private Credentials credentials;

    private Connection connection;
    private boolean active;

    @Override
    public void openConnection() {
        try {
            String driver = "com.mysql.jdbc.Driver";
            Class.forName(driver);

            String url = "jdbc:mysql://<ip>:<port>/<database>?autoReconnect=true";

            this.connection = DriverManager.getConnection(
                    url.replaceAll("<ip>", credentials.getHost())
                            .replaceAll("<port>", String.valueOf(credentials.getPort()))
                            .replaceAll("<database>", credentials.getDatabase()),
                    credentials.getUsername(), credentials.getPassword()
            );

            DatabaseMetaData metaData = connection.getMetaData();

            active = true;
        } catch (Exception e) {
            Bukkit.getLogger().severe("Não foi possível efetuar a conexão com o MySQL!");
        }
    }

    @Override
    public void makeTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS `tempmute` (`userName` VARCHAR(16) NOT NULL, `expiration`" +
                            " BIGINT(20) NOT NULL, `muted` BOOLEAN)"
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        if (!active) return;

        try {
            connection.close();
            active = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.connection;
    }

}
