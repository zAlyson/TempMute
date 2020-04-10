package br.alysonsantos.com;

import br.alysonsantos.com.command.TempMuteCommand;
import br.alysonsantos.com.command.UnmuteCommand;
import br.alysonsantos.com.core.controller.UserController;
import br.alysonsantos.com.core.data.Credentials;
import br.alysonsantos.com.core.data.MySQLDatabase;
import br.alysonsantos.com.core.data.SQLDatabase;
import br.alysonsantos.com.core.repository.UserRepository;
import br.alysonsantos.com.listener.BaseListener;
import br.alysonsantos.com.util.JsonDocument;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private UserController userController;
    private UserRepository userRepository;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        startDatabase();

        Bukkit.getPluginManager().registerEvents(
                new BaseListener(userController, userRepository), this
        );

        getCommand("tempmute").setExecutor(
                new TempMuteCommand(userController, userRepository)
        );

        getCommand("unmute").setExecutor(
                new UnmuteCommand(userController, userRepository)
        );
    }

    @Override
    public void onDisable() {
    }

    @SneakyThrows
    private void startDatabase() {
        SQLDatabase database = new MySQLDatabase(Credentials.of(
                JsonDocument.of(this.getDataFolder(), "credentials.db")
        ));

        database.openConnection();
        database.makeTable();

        userController = new UserController();
        userRepository = new UserRepository(database);
    }
}
