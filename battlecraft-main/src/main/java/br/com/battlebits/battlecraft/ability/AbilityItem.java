package br.com.battlebits.battlecraft.ability;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Use this if an ability has items
 * TODO Change this name
 */
public interface AbilityItem extends Listener {

    /**
     * Usado por habilidades que possuem items
     * @return lista de items do kit
     */
    List<ItemStack> getItems();

}
