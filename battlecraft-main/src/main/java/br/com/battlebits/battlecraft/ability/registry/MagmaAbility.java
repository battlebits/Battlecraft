package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Random;

public class MagmaAbility extends Ability {

    private Random random;

    public MagmaAbility() {
        random = new Random();
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && hasAbility((Player) event.getEntity()) && (event.getCause() ==
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
        if (hasAbility(damager) && random.nextInt(100) <= 25) {
            player.setFireTicks(Math.max(player.getFireTicks(), 90));
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.SECOND) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (hasAbility(player) && player.getLocation().getBlock().getType().name().contains("WATER")) {
                    player.damage(2);
                }
            }
        }
    }

}
