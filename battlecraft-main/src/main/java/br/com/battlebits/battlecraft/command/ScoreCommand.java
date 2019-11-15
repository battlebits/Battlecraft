package br.com.battlebits.battlecraft.command;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.manager.WarpManager;
import br.com.battlebits.battlecraft.status.StatusAccount;
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
        if(currentWarp == warpManager.getDefaultWarp()) {
            StatusSpawn statusSpawn = (StatusSpawn) status.getWarpStatus(currentWarp);
            p.sendMessage("Kills: " + statusSpawn.getKills());
            p.sendMessage("Deaths: " + statusSpawn.getDeaths());
            p.sendMessage("Killstreak: " + statusSpawn.getKillstreak());
        }

    }
}
