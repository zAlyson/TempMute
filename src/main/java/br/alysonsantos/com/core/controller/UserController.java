package br.alysonsantos.com.core.controller;

import br.alysonsantos.com.core.model.User;
import br.alysonsantos.com.util.Cache;
import org.bukkit.entity.Player;

import java.util.Optional;

public class UserController extends Cache<User> implements Controller {

    @Override
    public User getByPlayer(Player player) {
        return get(user -> user.getName().equals(player.getUniqueId()));
    }

    @Override
    public Optional<User> getByName(String name) {
        return getElements().stream().filter(user -> user.getName().equals(name)).findAny();
    }
}
