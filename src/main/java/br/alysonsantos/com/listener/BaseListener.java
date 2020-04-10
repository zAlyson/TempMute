package br.alysonsantos.com.listener;

import br.alysonsantos.com.core.controller.UserController;
import br.alysonsantos.com.core.repository.UserRepository;
import br.alysonsantos.com.helper.TempHelper;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
public class BaseListener implements Listener {

    private final UserController controller;
    private final UserRepository repository;

    private static final TempHelper HELPER = TempHelper.getInstance();

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        final Player player = event.getPlayer();

        controller.getAndRemove(user -> user.getName().equals(player.getName()));
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        runAsync(() -> {
            repository.find(player.getName()).ifPresent(controller::addElement);
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();

        controller.getAndRemove($ -> $.getName().equals(player.getName()));
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();

        controller.getByName(player.getName()).ifPresent(user -> {

            if (user.isMuted()) {

                if (user.getExpiration() < System.currentTimeMillis()) {
                    repository.delete(user.getName());
                    controller.removeElement(user);
                    return;
                }

                player.sendMessage(
                        "\n " +
                                "§c Ops! You are muted, wait " + HELPER.getTime(user.getExpiration() - System.currentTimeMillis()) + " to speak in the chat." +
                                "\n ");

                event.setCancelled(true);
            }

        });
    }

    private void runAsync(Runnable runnable) {
        CompletableFuture.runAsync(runnable);
    }
}
