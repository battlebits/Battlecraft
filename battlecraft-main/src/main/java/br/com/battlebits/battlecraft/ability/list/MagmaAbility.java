package br.com.battlebits.battlecraft.ability.list;

import br.com.battlebits.battlecraft.ability.AbilityImpl;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class MagmaAbility extends AbilityImpl {

    private Random random;

    public MagmaAbility() {
        super("Magma", ItemBuilder.create(Material.MAGMA_CREAM).build(), 0);
        random = new Random();
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && isUsing((Player) event.getEntity()) && (event.getCause() ==
                EntityDamageEvent.DamageCause.LAVA || event.getCause().name().contains("FIRE")) && event.getCause() !=
                EntityDamageEvent.DamageCause.LIGHTNING) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        final Player player = (Player) event.getEntity();
        final Player damager = (Player) event.getDamager();
        if (isUsing(damager) && random.nextInt(100) <= 25) {
            player.setFireTicks(Math.max(player.getFireTicks(), 90));
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.SECOND) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (isUsing(player) && player.getLocation().getBlock().getType().name().contains("WATER")) {
                    player.damage(2);
                }
            }
        }
    }

    @Override
    public void onReceiveItems(Player player) {
        //Null
    }
}
