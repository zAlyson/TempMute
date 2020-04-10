package br.alysonsantos.com.core.repository;

import br.alysonsantos.com.core.data.SQLDatabase;
import br.alysonsantos.com.core.model.User;
import lombok.AllArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository implements Repository<String, User> {

    private SQLDatabase sqlDatabase;

    @Override
    public Optional<User> find(String id) {
        User user = null;

        try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("SELECT * FROM `tempmute` WHERE `userName`=?")) {
            statement.setString(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                user = new User(
                        resultSet.getString("userName"),
                        resultSet.getLong("expiration"),
                        resultSet.getBoolean("muted")
                );

            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(user);
    }

    @Override
    public void insert(String id, User user) {
        try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("INSERT INTO tempmute (userName, expiration, muted) VALUES (?, ?, ?)")) {

            statement.setString(1, id);
            statement.setLong(2, user.getExpiration());
            statement.setBoolean(3, user.isMuted());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(String id, User user) {
        try (PreparedStatement statement = sqlDatabase.getConnection().prepareStatement("UPDATE tempmute SET expiration=?, muted=? WHERE userName=?")) {

            statement.setLong(1, user.getExpiration());
            statement.setBoolean(2, user.isMuted());
            statement.setString(3, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        try (PreparedStatement stmt = sqlDatabase.getConnection().prepareStatement("DELETE FROM `tempmute` WHERE `userName`=?")) {

            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
