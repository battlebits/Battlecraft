package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.ability.Ability;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;

import static br.com.battlebits.battlecraft.manager.ProtectionManager.isProtected;

public class StomperAbility extends Ability {

    private static final double FALL_MAX_DAMAGE = 4.0;
    private static final double SNEAK_MAX_DAMAGE = 8.0;

    private static final double STOMPER_DISTANCE = 5.0;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            if (event.getEntity() instanceof Player) {
                Player damager = (Player) event.getEntity();

                if (hasAbility(damager) && !isProtected(damager)) {
                    damager.getWorld().playSound(damager.getLocation(), Sound.BLOCK_ANVIL_LAND, 0.15F, 1.0F);

                    Battlecraft.getInstance().getWarpManager().getPlayerWarp(damager).getPlayers().stream()
                            .filter(target -> !target.equals(damager)
                                    && damager.getLocation().distance(target.getLocation()) <= STOMPER_DISTANCE
                                    && !isProtected(target))
                            .forEach(target -> {
                                double damage = event.getDamage();

                                if (target.isSneaking() && damage > SNEAK_MAX_DAMAGE) {
                                    damage = SNEAK_MAX_DAMAGE;
                                }

                                target.damage(damage, damager);
                            });

                    if (event.getDamage() > FALL_MAX_DAMAGE) {
                        event.setDamage(FALL_MAX_DAMAGE);
                    }
                }
            }
        }
    }

}
