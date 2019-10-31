package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpDeathEvent;
import br.com.battlebits.battlecraft.manager.KitManager;
import br.com.battlebits.battlecraft.manager.WarpManager;
import br.com.battlebits.battlecraft.util.InventoryUtils;
import br.com.battlebits.battlecraft.warp.Warp;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

public class DeathListener implements Listener {

    private WarpManager warpManager = Battlecraft.getInstance().getWarpManager();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageListener(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player))
            return;
        Player p = (Player) e.getEntity();
        if (e.getCause() == DamageCause.ENTITY_ATTACK)
            return;
        if (e.getFinalDamage() < p.getHealth())
            return;
        e.setCancelled(true);
        Player killer = null;
        EntityDamageEvent last = p.getLastDamageCause();
        if (last instanceof EntityDamageByEntityEvent) {
            killer = getLastPlayer((EntityDamageByEntityEvent) last);
        }
        Bukkit.getPluginManager()
                .callEvent(new PlayerWarpDeathEvent(p, killer, warpManager.getPlayerWarp(p)));

    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByBlockListener(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player p = (Player) event.getEntity();
        if (event.getFinalDamage() < p.getHealth())
            return;
        event.setCancelled(true);
        Player killer = null;
        EntityDamageEvent last = p.getLastDamageCause();
        if (last instanceof EntityDamageByEntityEvent) {
            killer = getLastPlayer((EntityDamageByEntityEvent) last);
        }
        Bukkit.getPluginManager().callEvent(new PlayerWarpDeathEvent(p, killer, warpManager.getPlayerWarp(p)));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityListener(EntityDamageByEntityEvent event) {
        if (! (event.getEntity() instanceof Player))
            return;
        Player p = (Player) event.getEntity();
        if (event.getFinalDamage() < p.getHealth())
            return;
        event.setCancelled(true);
        Bukkit.getPluginManager().callEvent(new PlayerWarpDeathEvent(p, getLastPlayer(event), warpManager.getPlayerWarp(p)));
    }

    @EventHandler
    public void onPlayerDeathListener(PlayerDeathEvent e) {
       InventoryUtils.dropItems(e.getEntity(), e.getEntity().getLocation());
        e.setDeathMessage(null);
        e.getEntity().spigot().respawn();
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        e.setRespawnLocation(e.getPlayer().getWorld().getSpawnLocation());
        Bukkit.getPluginManager().callEvent(new PlayerWarpDeathEvent(e.getPlayer(), e.getPlayer().getKiller(),
                warpManager.getPlayerWarp(e.getPlayer())));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDeathInWarpListener(PlayerWarpDeathEvent e) {
        Player p = e.getPlayer();
        p.setNoDamageTicks(100);
        if (e.hasKiller()) {
            e.getKiller().playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 0.5F, 1.0F);
        }
        InventoryUtils.dropItems(p, p.getLocation());
        AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double MAX_HEALTH = attribute != null ? attribute.getValue() : 20.0d;
        p.setHealth(MAX_HEALTH);
        p.closeInventory();
        p.setVelocity(new Vector());
        if (KitManager.containsKit(p)) {
            KitManager.removeKit(p);
        }
        for (PotionEffect effect : p.getActivePotionEffects())
            p.removePotionEffect(effect.getType());
        if (e.hasKiller()) {
            InventoryUtils.repairArmor(e.getKiller());
        }
        p.setFallDistance(0);
        p.setNoDamageTicks(20);
        Warp warp = e.getWarp();
        if (warp == null) {
            warp = warpManager.getDefaultWarp();
        }
        warpManager.joinWarp(p, warp);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 0.5F, 1.0F);
        p.setNoDamageTicks(1);
    }

    private Player getLastPlayer(EntityDamageByEntityEvent event) {
        Player killer = null;
        if (event.getDamager() instanceof Player) {
            killer = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            Projectile pr = (Projectile) event.getDamager();
            if (pr.getShooter() != null && pr.getShooter() instanceof Player) {
                killer = (Player) pr.getShooter();
            }
        }
        return killer;
    }
}