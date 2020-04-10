package br.alysonsantos.com.command;

import br.alysonsantos.com.core.controller.UserController;
import br.alysonsantos.com.core.model.User;
import br.alysonsantos.com.core.repository.UserRepository;
import br.alysonsantos.com.util.UtilFormat;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class TempMuteCommand implements CommandExecutor {

    private final UserController controller;
    private final UserRepository repository;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("command.tempmute")) {
            sender.sendMessage(
                    "\n " +
                            "§c You are not permission. " +
                            "\n "
            );
            return true;
        }

        if (args.length != 2) {
            sender.sendMessage(
                    "\n " +
                            "§cUsage: /tempmute <player> <time>." +
                            "\n ");
            return false;
        }

        String name = args[0];
        String time = args[1];

        User user = controller.getByName(name).orElse(
                new User(
                        name,
                        0,
                        false
                )
        );

        if (user.isMuted()) {
            sender.sendMessage("§cThis player is already mutated.");
            return true;
        }

        int seconds = UtilFormat.toSeconds(time);
        if (seconds == -1) {
            sender.sendMessage("§cPlease enter a valid time.");
            return false;
        }

        runAsync(() -> {

            user.setExpiration(System.currentTimeMillis() + (seconds * 1000));
            user.setMuted(true);

            if (!repository.find(user.getName()).isPresent()) {
                repository.insert(user.getName(), user);
            } else {
                repository.update(user.getName(), user);
            }

            controller.addElement(user);
            sender.sendMessage("§aPlayer fined successfully!");
        });

        return false;
    }

    private void runAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
    }
}
