package br.com.battlebits.battlecraft.warp.fight;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.fight.PlayerFightFinishEvent;
import br.com.battlebits.battlecraft.event.fight.PlayerFightStartEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpDeathEvent;
import br.com.battlebits.battlecraft.util.InventoryUtils;
import br.com.battlebits.commons.bukkit.api.vanish.VanishAPI;
import br.com.battlebits.commons.bukkit.event.vanish.PlayerShowToPlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Fight1v1 implements Listener {

    private Player player;
    private Player target;


    public Fight1v1(Player player, Player target, Challenge challenge) {
        this.player = player;
        this.target = target;

        InventoryUtils.clearInventory(this.player);
        InventoryUtils.clearInventory(this.target);

        challenge.applyChallengeKit(this.player);
        challenge.applyChallengeKit(this.target);

        Bukkit.getPluginManager().callEvent(new PlayerFightStartEvent(player, target, challenge));

        Bukkit.getPluginManager().registerEvents(this, Battlecraft.getInstance());
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (!inPvP(online)) {
                this.player.hidePlayer(Battlecraft.getInstance(), online);
                this.target.hidePlayer(Battlecraft.getInstance(), online);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeathStatus(PlayerWarpDeathEvent event) {
        Player p = event.getPlayer();
        if (!inPvP(p))
            return;
        handleDeath(p);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player recebe = (Player) event.getEntity();
            Player faz = (Player) event.getDamager();
            if (inPvP(faz) && inPvP(recebe))
                return;
            if (inPvP(faz) && !inPvP(recebe))
                event.setCancelled(true);
            else if (!inPvP(faz) && inPvP(recebe))
                event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        handleDeath(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onKick(PlayerKickEvent event) {
        handleDeath(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerShow(PlayerShowToPlayerEvent event) {
        if (event.isCancelled() && inPvP(event.getToPlayer()) && inPvP(event.getPlayer()))
            event.setCancelled(false);
    }

    public void handleDeath(Player dead) {
        if (!inPvP(dead))
            return;
        Player killer;
        if (dead == this.player)
            killer = this.player;
        else
            killer = this.target;
        destroy();
        Bukkit.getPluginManager().callEvent(new PlayerFightFinishEvent(dead, killer));
        InventoryUtils.clearInventory(dead);
        InventoryUtils.clearInventory(killer);
        dead.setHealth(20D);
        killer.setHealth(20D);
        dead.updateInventory();
        killer.updateInventory();
        VanishAPI.updateVanishToPlayer(dead);
        VanishAPI.updateVanishToPlayer(killer);
    }

    public boolean inPvP(Player player) {
        return player == this.player || player == this.target;
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
    }
}
