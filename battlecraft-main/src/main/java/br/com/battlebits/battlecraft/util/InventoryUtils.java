package br.com.battlebits.battlecraft.util;

import org.bukkit.entity.Player;

public class InventoryUtils {

    /**
     * Limpa o inventário, cursor e armadura
     * @param player
     */
    public static void clearInventory(Player player) {
        player.getInventory().setArmorContents(null);
        player.getInventory().clear();
        player.setItemOnCursor(null);
    }

}
