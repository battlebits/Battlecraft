package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.manager.PvPStatusManager;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpDeathEvent;
import br.com.battlebits.battlecraft.manager.WarpManager;
import br.com.battlebits.battlecraft.warp.Warp;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.account.BukkitAccount;
import br.com.battlebits.commons.bukkit.scoreboard.BattleScoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class DataListener implements Listener {

    @EventHandler
    public void onPlayerWarpDeathEvent(PlayerWarpDeathEvent event) {
        final PvPStatusManager statusManager = Battlecraft.getInstance().getStatusManager();
        final Player player = event.getPlayer();
        final UUID uniqueId = player.getUniqueId();
        statusManager.get(uniqueId).with(account -> {
            account.getStatus(event.getWarp()).save("deaths", (int) account.get("deaths") + 1);
            account.save("killstreak", 0);
            final BukkitAccount bukkitAccount = (BukkitAccount) Commons.getAccount(uniqueId);
            bukkitAccount.getBattleboard().update();
            Commons.getPlatform().runAsync(() -> {
                statusManager.dataStatus().saveAccount(account, "warpStatus");
                statusManager.dataStatus().saveAccount(account, "globalValues");
            });
        });
        final Player killer = event.getKiller();
        if (killer != null) {
            statusManager.get(killer.getUniqueId()).with(account -> {
                account.getStatus(event.getWarp()).save("kills", (int) account.get("kills") + 1);
                account.save("killstreak", (int) account.get("killstreak") + 1);
                final BukkitAccount bukkitAccount = (BukkitAccount) Commons.getAccount(killer.getUniqueId());
                bukkitAccount.getBattleboard().update();
                Commons.getPlatform().runAsync(() -> {
                    statusManager.dataStatus().saveAccount(account, "warpStatus");
                    statusManager.dataStatus().saveAccount(account, "globalValues");
                });
            });
        }
    }

    @EventHandler
    public void onPlayerWarpJoinEvent(PlayerWarpJoinEvent event) {
        final BukkitAccount bukkitAccount = (BukkitAccount) Commons.getAccount(event.getPlayer().getUniqueId());
        bukkitAccount.getBattleboard().update();
    }

    @EventHandler
    public void onPlayerWarpQuitEvent(PlayerWarpQuitEvent event) {
        final BukkitAccount bukkitAccount = (BukkitAccount) Commons.getAccount(event.getPlayer().getUniqueId());
        bukkitAccount.getBattleboard().update();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerScoreboardEvent(PlayerJoinEvent event) {
        final BukkitAccount bukkitAccount = (BukkitAccount) Commons.getAccount(event.getPlayer().getUniqueId());
        final BattleScoreboard scoreboard = bukkitAccount.getBattleboard();

        scoreboard.setDisplayName("§8§l>> §6§lBattleCraft §8§l<<");
        WarpManager manager = Battlecraft.getInstance().getWarpManager();
        final Warp playerWarp = manager.getPlayerWarp(event.getPlayer());
        scoreboard.setLineConsumer(playerWarp.getScoreboardLines());
    }
}
