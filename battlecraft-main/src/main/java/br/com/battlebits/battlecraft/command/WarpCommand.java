package br.com.battlebits.battlecraft.command;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.manager.TeleportManager;
import br.com.battlebits.commons.bukkit.command.BukkitCommandArgs;
import br.com.battlebits.commons.command.CommandClass;
import br.com.battlebits.commons.command.CommandFramework.Command;

public class WarpCommand implements CommandClass {


    @Command(name = "spawn")
    public void spawn(BukkitCommandArgs args) {
        TeleportManager.teleport(args.getPlayer(), Battlecraft.getInstance().getWarpManager().getDefaultWarp());
    }

    @Command(name = "1v1")
    public void onevsone(BukkitCommandArgs args) {
        TeleportManager.teleport(args.getPlayer(), Battlecraft.getInstance().getWarpManager().getWarp("1v1"));
    }
}
