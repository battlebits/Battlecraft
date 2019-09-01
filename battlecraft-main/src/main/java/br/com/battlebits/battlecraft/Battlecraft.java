package br.com.battlebits.battlecraft;

import br.com.battlebits.battlecraft.manager.WarpManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Battlecraft extends JavaPlugin {

    private WarpManager warpManager;

    @Override
    public void onEnable() {
        this.warpManager = new WarpManager(this);
    }
}
