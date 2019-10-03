package br.com.battlebits.battlecraft;

import br.com.battlebits.battlecraft.listener.*;
import br.com.battlebits.battlecraft.manager.AbilityManager;
import br.com.battlebits.battlecraft.manager.CooldownManager;
import br.com.battlebits.battlecraft.manager.ProtectionManager;
import br.com.battlebits.battlecraft.manager.WarpManager;
import br.com.battlebits.battlecraft.warp.WarpLocation;
import br.com.battlebits.battlecraft.warp.WarpSpawn;
import br.com.battlebits.battlecraft.world.map.VoidMap;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Battlecraft extends JavaPlugin {


    private WarpManager warpManager;
    private ProtectionManager protectionManager;

    @Getter
    private static Battlecraft instance;

    @Override
    public void onLoad() {
        Plugin dependency = this.getServer().getPluginManager().getPlugin("Commons");
        if (dependency == null) {
            this.getLogger().warning("Commons was not found, disabling Battlecraft");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        this.warpManager = new WarpManager(this);
        loadWarps();
        loadListeners();
        loadManagers();
    }

    private void loadWarps() {
        World world = this.getServer().getWorld("world");
        Location spawnLocation = new Location(world, 0.5, world.getHighestBlockYAt(0,0), 0.5);
        WarpLocation warp = new WarpSpawn(spawnLocation, new VoidMap());
        this.warpManager.addWarp(warp);
        this.warpManager.setDefaultWarp(warp);
    }

    private void loadListeners() {
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new MoveListener(), this);
        manager.registerEvents(new PlayerListener(), this);
        manager.registerEvents(new DamageFixListener(), this);
        manager.registerEvents(new SoupListener(), this);
        manager.registerEvents(new WarpListener(this), this);
        manager.registerEvents(new CombatLogListener(), this);
        manager.registerEvents(new TeleportListener(), this);
    }

    private void loadManagers() {
        AbilityManager.create();
        AbilityManager.registerKits();
        CooldownManager.create();
    }
}
