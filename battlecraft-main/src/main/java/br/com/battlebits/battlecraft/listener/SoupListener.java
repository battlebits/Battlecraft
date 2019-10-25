package br.com.battlebits.battlecraft.listener;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupListener implements Listener {

    private final static double HEALTH = 7d;
    private final static int FOOD = 7;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSoup(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        Action a = event.getAction();
        if (item == null)
            return;
        if (item.getType() != Material.MUSHROOM_STEW)
            return;
        if (!a.toString().contains("RIGHT_CLICK"))
            return;
        AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double MAX_HEALTH = attribute != null ? attribute.getValue() : 20.0d;
        int MAX_FOOD_LEVEL = 20;
        if (p.getHealth() < MAX_HEALTH || p.getFoodLevel() < MAX_FOOD_LEVEL) {
            event.setCancelled(true);
            p.sendMessage(event.useItemInHand() + "");
            if (p.getHealth() < MAX_HEALTH)
                p.setHealth(Math.min(p.getHealth() + HEALTH, MAX_HEALTH));
            else if (p.getFoodLevel() < MAX_FOOD_LEVEL)
                p.setFoodLevel(Math.min(p.getFoodLevel() + FOOD, MAX_FOOD_LEVEL));
            item.setType(Material.BOWL);
        }
    }
}
