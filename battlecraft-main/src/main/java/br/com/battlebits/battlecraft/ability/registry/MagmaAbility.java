package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import static br.com.battlebits.battlecraft.manager.ProtectionManager.isProtected;

import java.util.Arrays;

public class MagmaAbility extends Ability {

    private static final DamageCause[] MAGMA_NO_DAMAGE_CAUSES =  new DamageCause[]{
            DamageCause.FIRE_TICK,
            DamageCause.FIRE,
            DamageCause.HOT_FLOOR,
            DamageCause.LAVA
    };

    private static final double DAMAGE_FIRE_CHANCE = 0.25;
    private static final int DAMAGE_FIRE_DURATION = 90;

    private static final double WATER_DAMAGE = 2.0;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && Arrays.binarySearch(MAGMA_NO_DAMAGE_CAUSES, event.getCause()) > 0
            && hasAbility((Player) event.getEntity()) ) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamagePlayerEvent(PlayerDamagePlayerEvent event) {
        if (Math.random() <= DAMAGE_FIRE_CHANCE && hasAbility(event.getPlayer())
                && !isProtected(event.getPlayer()) && !isProtected(event.getDamaged())) {
            event.getDamaged().setFireTicks(Math.max(event.getDamaged().getFireTicks(), DAMAGE_FIRE_DURATION));
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.SECOND) {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getLocation().getBlock().getType() == Material.WATER)
                    .filter(p -> hasAbility(p))
                    .filter(p -> !isProtected(p))
                    .forEach(p -> p.damage(WATER_DAMAGE));
        }
    }

}
