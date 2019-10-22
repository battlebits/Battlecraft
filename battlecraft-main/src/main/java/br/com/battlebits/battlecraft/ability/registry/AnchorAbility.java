package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class AnchorAbility extends Ability {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && hasAbility(player)) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Battlecraft.getInstance(), () ->
                        player.setVelocity(new Vector(0D, 0D, 0D)), 1L);
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final Player damager = (Player) event.getDamager();
            if (hasAbility(player) || hasAbility(damager)) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Battlecraft.getInstance(), () -> {
                    player.setVelocity(new Vector(0D, 0D, 0D));
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
                    damager.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
                }, 1L);
            }
        } else if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (hasAbility(player)) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Battlecraft.getInstance(), () -> {
                    player.setVelocity(new Vector(0D, 0D, 0D));
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
                }, 1L);
            }
        }
    }

}
