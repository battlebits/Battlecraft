package br.com.battlebits.battlecraft.warp.fight;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.fight.PlayerFightFinishEvent;
import br.com.battlebits.battlecraft.event.fight.PlayerFightStartEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpDeathEvent;
import br.com.battlebits.battlecraft.util.InventoryUtils;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.api.vanish.VanishAPI;
import br.com.battlebits.commons.bukkit.event.vanish.PlayerShowToPlayerEvent;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.WARP_1V1_LEFT;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.WARP_1V1_TAG;

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
        Player winner;
        if(event.getPlayer() == player)
            winner = target;
        else
            winner = player;
        Language l = Commons.getLanguage(winner.getUniqueId());
        winner.sendMessage(l.tl(WARP_1V1_TAG) + l.tl(WARP_1V1_LEFT, event.getPlayer().getName()));
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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        this.target.hidePlayer(Battlecraft.getInstance(), event.getPlayer());
        this.player.hidePlayer(Battlecraft.getInstance(), event.getPlayer());
    }

    public void handleDeath(Player dead) {
        if (!inPvP(dead))
            return;
        Player killer;
        if (dead == this.player)
            killer = this.target;
        else
            killer = this.player;
        destroy();
        for (Player online : Bukkit.getOnlinePlayers()) {
            this.player.showPlayer(Battlecraft.getInstance(), online);
            this.target.showPlayer(Battlecraft.getInstance(), online);
        }
        Bukkit.getPluginManager().callEvent(new PlayerFightFinishEvent(killer, dead));
        dead.setHealth(20D);
        killer.setHealth(20D);
        dead.updateInventory();
        killer.updateInventory();
        Battlecraft.getInstance().getWarpManager().joinWarp(killer, Battlecraft.getInstance().getWarpManager().getWarp("1v1"));
    }

    public boolean inPvP(Player player) {
        return player == this.player || player == this.target;
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
    }
}
