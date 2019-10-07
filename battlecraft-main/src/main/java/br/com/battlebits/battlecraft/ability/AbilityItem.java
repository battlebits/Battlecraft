package br.com.battlebits.battlecraft.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface AbilityItem extends Listener {

    void onReceiveItems(Player player);

}
