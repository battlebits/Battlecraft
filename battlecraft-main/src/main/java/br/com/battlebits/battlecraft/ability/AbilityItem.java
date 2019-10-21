package br.com.battlebits.battlecraft.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Use this if an ability has items
 * TODO Change this name
 */
public interface AbilityItem extends Listener {

    /**
     * Ao receber items, tome cuidado ao setar items em slots
     * <p>
     * Tente sempre adicionar os items ao inventário.
     *
     * @param player Jogador com o kit
     */
    void onReceiveItems(Player player);

}
