package br.com.battlebits.battlecraft;

import br.com.battlebits.battlecraft.listener.*;
import br.com.battlebits.battlecraft.manager.AbilityManager;
import br.com.battlebits.battlecraft.manager.CooldownManager;
import br.com.battlebits.battlecraft.manager.WarpManager;
import br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag;
import br.com.battlebits.battlecraft.warp.WarpLocation;
import br.com.battlebits.battlecraft.warp.WarpSpawn;
import br.com.battlebits.battlecraft.world.map.VoidMap;
import br.com.battlebits.commons.backend.properties.PropertiesStorageDataTranslation;
import br.com.battlebits.commons.bukkit.command.BukkitCommandFramework;
import br.com.battlebits.commons.command.CommandLoader;
import br.com.battlebits.commons.translate.TranslationCommon;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Battlecraft extends JavaPlugin {


    @Getter
    private static Battlecraft instance;
    private WarpManager warpManager;

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
        Plugin dependency = this.getServer().getPluginManager().getPlugin("Commons");
        if (dependency == null || !dependency.isEnabled()) {
            this.getLogger().warning("Commons was not enabled, disabling Battlecraft");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        instance = this;
        this.warpManager = new WarpManager(this);
        TranslationCommon.addTranslation(new PropertiesStorageDataTranslation(BattlecraftTranslateTag.class));
        loadCommands();
        loadWarps();
        loadListeners();
        loadManagers();
    }

    private void loadCommands() {
        new CommandLoader(new BukkitCommandFramework(this)).loadCommandsFromPackage(getFile(), "br.com.battlebits.battlecraft.command");
    }

    private void loadWarps() {
        World world = this.getServer().getWorld("world");
        Location spawnLocation = new Location(world, 0.5, world.getHighestBlockYAt(0, 0), 0.5);
        WarpLocation warp = new WarpSpawn(spawnLocation, new VoidMap());
        this.warpManager.addWarp(warp);
        this.warpManager.setDefaultWarp(warp);

    }

    private void loadListeners() {
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new ProtectionListener(), this);
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
