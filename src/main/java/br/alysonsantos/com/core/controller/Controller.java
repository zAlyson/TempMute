package br.alysonsantos.com.core.controller;

import br.alysonsantos.com.core.model.User;
import org.bukkit.entity.Player;

import java.util.Optional;

public interface Controller {

    Optional<User> getByName(String playerName);

    User getByPlayer(Player player);

}
