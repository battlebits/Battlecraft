package br.com.battlebits.battlecraft.ability.list;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.ability.AbilityImpl;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class AnchorAbility extends AbilityImpl {

    public AnchorAbility() {
        super("Anchor", ItemBuilder.create(Material.ANVIL).build(), 0);
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && isUsing(player)) {
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
            if (isUsing(player) || isUsing(damager)) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Battlecraft.getInstance(), () -> {
                    player.setVelocity(new Vector(0D, 0D, 0D));
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
                    damager.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
                }, 1L);
            }
        } else if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (isUsing(player)) {
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Battlecraft.getInstance(), () -> {
                    player.setVelocity(new Vector(0D, 0D, 0D));
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0F, 1.0F);
                }, 1L);
            }
        }
    }

    @Override
    public void onReceiveItems(Player player) {

    }

}
