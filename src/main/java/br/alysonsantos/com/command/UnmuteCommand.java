package br.alysonsantos.com.command;

import br.alysonsantos.com.core.controller.UserController;
import br.alysonsantos.com.core.model.User;
import br.alysonsantos.com.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class UnmuteCommand implements CommandExecutor {

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

        if (args.length != 1) {
            sender.sendMessage(
                    "\n " +
                            "§cUsage: /unmute <player>." +
                            "\n ");
            return false;
        }

        String name = args[0];

        User user = controller.getByName(name).orElse(
                new User(
                        name,
                        0,
                        false
                )
        );

        if (!user.isMuted()) {
            sender.sendMessage("§cThis player is not mutated.");
            return true;
        }

        runAsync(() -> {

            if (controller.getByName(name).isPresent()) {
                repository.delete(name);

               controller.removeElement(user);

                sender.sendMessage("§aSuccessfully unmuted");
            } else {
                sender.sendMessage("Player is not muted");
            }
        });

        return false;
    }


    private void runAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
    }
}
