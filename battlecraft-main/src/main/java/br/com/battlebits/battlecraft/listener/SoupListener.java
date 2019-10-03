package br.com.battlebits.battlecraft.listener;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SoupListener implements Listener {

    private final static double HEALTH = 7d;
    private final static int FOOD = 7;

    @EventHandler
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
        if (p.getHealth() < 20 || p.getFoodLevel() < 20) {
            event.setCancelled(true);
            double max_health = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            if (p.getHealth() < max_health)
                if (p.getHealth() + HEALTH <= max_health)
                    p.setHealth(p.getHealth() + HEALTH);
                else
                    p.setHealth(max_health);
            else if (p.getFoodLevel() < 20)
                if (p.getFoodLevel() + FOOD <= 20)
                    p.setFoodLevel(p.getFoodLevel() + FOOD);
                else
                    p.setFoodLevel(20);
            item.setType(Material.BOWL);
        }
    }
}
