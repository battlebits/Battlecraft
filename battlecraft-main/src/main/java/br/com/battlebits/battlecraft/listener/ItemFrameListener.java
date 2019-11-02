package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;

public class ItemFrameListener implements Listener {

    @EventHandler
    public void onHanging(HangingBreakEvent event) {
        if (event.getCause() != RemoveCause.ENTITY) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHanging(HangingBreakByEntityEvent event) {
        if (!(event.getEntity() instanceof ItemFrame))
            return;
        if (!(event.getRemover() instanceof Player)) {
            event.setCancelled(true);
            return;
        }
        Player p = (Player) event.getRemover();
        if (p.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof ItemFrame)) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            event.setCancelled(true);
            return;
        }
        Player p = (Player) event.getDamager();
        if (p.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerOpenFrame(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame))
            return;
        ItemFrame frame = (ItemFrame) event.getRightClicked();
        Player p = event.getPlayer();
        event.setCancelled(true);
        if (frame.getItem().getType() != Material.AIR) {
            ItemStack item = frame.getItem();
            Material type = item.getType();
            switch (type) {
                case MUSHROOM_SOUP:
                case BROWN_MUSHROOM:
                case RED_MUSHROOM:
                case BOWL:
                    Inventory inv = Bukkit.createInventory(null, 36, Commons.getLanguage(p.getUniqueId()).tl(getTagByMaterial(type)));
                    for (int i = 0; i < 36; i++)
                        inv.setItem(i, new ItemStack(type));
                    p.openInventory(inv);
                    break;
                default:
            }
        }
    }

    @EventHandler
    public void onPlayerEditFrame(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame))
            return;
        ItemFrame frame = (ItemFrame) event.getRightClicked();
        Player p = event.getPlayer();
        event.setCancelled(true);
        Language l = Commons.getLanguage(p.getUniqueId());
        if (frame.getItem().getType() == Material.AIR && p.getGameMode() == GameMode.CREATIVE) {
            ItemStack item;
            Material type = p.getInventory().getItemInMainHand().getType();
            switch (type) {
                case MUSHROOM_SOUP:
                case BROWN_MUSHROOM:
                case RED_MUSHROOM:
                case BOWL:
                    item = ItemBuilder.create(type).name(l.tl(getTagByMaterial(type))).build();
                    break;
                default:
                    item = ItemBuilder.create(Material.AIR).build();
            }
            frame.setItem(item);
            frame.setRotation(Rotation.NONE);
        }
    }

    private BattlecraftTranslateTag getTagByMaterial(Material mat) {
        switch (mat) {
            case MUSHROOM_SOUP:
                return ITEM_FRAME_SOUP;
            case RED_MUSHROOM:
                return ITEM_FRAME_RED;
            case BROWN_MUSHROOM:
                return ITEM_FRAME_BROWN;
            case BOWL:
                return ITEM_FRAME_BOWL;
            default:
                return ITEM_FRAME_DEFAULT;
        }
    }
}
