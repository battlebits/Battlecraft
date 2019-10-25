package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.event.PlayerKitEvent;
import br.com.battlebits.battlecraft.util.InventoryUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

public class KitManager {

    private static Map<Player, Kit> playerKits = new HashMap<>();


    private static boolean hasKit(Player player, String kitName) {
        return true;
    }

    public static boolean containsKit(Player player) {
        return playerKits.containsKey(player);
    }

    /**
     * Limpa o inventário do jogador, registra nas habilidades e seta o resto do inventário com sopa.
     * @param player
     * @param kit
     */
    public static void giveKit(Player player, Kit kit) {
        PlayerKitEvent event = new PlayerKitEvent(player, kit);
        InventoryUtils.clearInventory(player);
        Battlecraft.getInstance().getServer().getPluginManager().callEvent(event);
        playerKits.put(player, kit);
        kit.getAbilities().forEach(ability -> ability.registerPlayer(player));
        PlayerInventory inv = player.getInventory();
        for (ItemStack is : kit.getItems()) {
            inv.addItem(is);
        }
        for (ItemStack is : inv.getContents()) {
            if (is == null)
                inv.addItem(new ItemStack(Material.MUSHROOM_STEW));
        }
    }

    /**
     * Limpa inventário do jogador e desregistra as habilidades
     * @param player
     */
    public static void removeKit(Player player) {
        player.closeInventory();
        InventoryUtils.clearInventory(player);
        Kit kit = playerKits.remove(player);
        if(kit != null)
            kit.getAbilities().forEach(ability -> ability.unregisterPlayer(player));
    }
}
