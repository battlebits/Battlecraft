package br.com.battlebits.battlecraft.command;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.manager.WarpManager;
import br.com.battlebits.battlecraft.status.StatusAccount;
import br.com.battlebits.battlecraft.status.WarpStatus;
import br.com.battlebits.battlecraft.status.ranking.Queue1v1;
import br.com.battlebits.battlecraft.status.warpstatus.Status1v1;
import br.com.battlebits.battlecraft.status.warpstatus.StatusSpawn;
import br.com.battlebits.battlecraft.warp.Warp;
import br.com.battlebits.commons.bukkit.command.BukkitCommandArgs;
import br.com.battlebits.commons.command.CommandClass;
import br.com.battlebits.commons.command.CommandFramework.Command;
import org.bukkit.entity.Player;

public class ScoreCommand implements CommandClass {


    @Command(name = "score", aliases = "scoreboard")
    public void score(BukkitCommandArgs args) {

    }

    @Command(name = "status")
    public void status(BukkitCommandArgs args) {
        Player p = args.getPlayer();
        StatusAccount status = Battlecraft.getInstance().getStatusManager().get(p.getUniqueId());
        WarpManager warpManager = Battlecraft.getInstance().getWarpManager();
        Warp currentWarp = warpManager.getPlayerWarp(p);
        WarpStatus warpStatus = status.getWarpStatus(currentWarp);
        if (warpStatus instanceof  StatusSpawn) {
            StatusSpawn statusSpawn = (StatusSpawn) warpStatus;
            p.sendMessage("Kills: " + statusSpawn.getKills());
            p.sendMessage("Deaths: " + statusSpawn.getDeaths());
            p.sendMessage("Killstreak: " + statusSpawn.getKillstreak());
        } else if (warpStatus instanceof Status1v1) {
            Status1v1 status1v1 = (Status1v1) warpStatus;
            p.sendMessage("Victory: " + status1v1.getQueueStatus(Queue1v1.NORMAL).getVictory());
            p.sendMessage("Defeat: " + status1v1.getQueueStatus(Queue1v1.NORMAL).getDefeat());
        }

    }
}
