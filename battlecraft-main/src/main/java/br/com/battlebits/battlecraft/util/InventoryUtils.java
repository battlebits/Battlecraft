package br.com.battlebits.battlecraft.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtils {

    /**
     * Limpa o inventário, cursor e armadura
     * @param player the player
     */
    public static void clearInventory(Player player) {
        player.getInventory().setArmorContents(null);
        player.getInventory().clear();
        player.setItemOnCursor(null);
    }

    /**
     * Repara toda a armadura do jogador
     * @param player jogador
     */
    public static void repairArmor(Player player) {
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            repairItem(armor);
        }
    }

    /**
     * Repara a durabilidade de um item
     * @param item item
     */
    public static void repairItem(ItemStack item) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if(meta != null) {
                ((Damageable)meta).setDamage(0);
            }
        }
    }

    /**
     * Dropa os items de um jogador em um determinado lugar
     * @param p jogador
     * @param l lugar
     */
    public static void dropItems(Player p, Location l) {
        List<ItemStack> itens = new ArrayList<>();
        for (ItemStack item : p.getInventory().getContents()) {
            if (item != null && item.getType() != Material.AIR) {
                itens.add(item.clone());
            }
        }
        for (ItemStack item : p.getInventory().getArmorContents()) {
            if (item != null && item.getType() != Material.AIR) {
                itens.add(item.clone());
            }
        }
        if (p.getItemOnCursor().getType() != Material.AIR) {
            itens.add(p.getItemOnCursor().clone());
        }
        dropItems(p, itens, l);
    }

    /**
     * Dropa uma lista de items em uma localização e limpa o inventário do jogador
     * @param p jogador
     * @param items lista de items
     * @param l localização
     */
    public static void dropItems(Player p, List<ItemStack> items, Location l) {
        World world = l.getWorld();
        if (world != null)
            for (ItemStack item : items) {
                if (item != null && item.getType() != Material.AIR) {
                    if (item.hasItemMeta()) {
                        world.dropItemNaturally(l, item.clone()).getItemStack().setItemMeta(item.getItemMeta());
                    } else {
                        world.dropItemNaturally(l, item);
                    }
                }
            }
        clearInventory(p);
        for (PotionEffect pot : p.getActivePotionEffects()) {
            p.removePotionEffect(pot.getType());
        }
        items.clear();
    }


}
