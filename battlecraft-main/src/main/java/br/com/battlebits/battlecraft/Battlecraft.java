package br.com.battlebits.battlecraft;

import br.com.battlebits.battlecraft.manager.StatusManager;
import br.com.battlebits.battlecraft.listener.*;
import br.com.battlebits.battlecraft.manager.AbilityManager;
import br.com.battlebits.battlecraft.manager.CombatLogManager;
import br.com.battlebits.battlecraft.manager.TeleportManager;
import br.com.battlebits.battlecraft.manager.WarpManager;
import br.com.battlebits.battlecraft.protocol.SoundFilter;
import br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag;
import br.com.battlebits.battlecraft.warp.Warp;
import br.com.battlebits.battlecraft.warp.Warp1v1;
import br.com.battlebits.battlecraft.warp.WarpSpawn;
import br.com.battlebits.battlecraft.world.map.OneVsOneMap;
import br.com.battlebits.battlecraft.world.map.SpawnMap;
import br.com.battlebits.commons.backend.properties.PropertiesStorageDataTranslation;
import br.com.battlebits.commons.bukkit.command.BukkitCommandFramework;
import br.com.battlebits.commons.command.CommandLoader;
import br.com.battlebits.commons.translate.TranslationCommon;
import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class Battlecraft extends JavaPlugin {


    @Getter
    private static Battlecraft instance;
    private StatusManager statusManager;
    private WarpManager warpManager;

    @Override
    public void onLoad() {
        if (!this.getServer().getPluginManager().isPluginEnabled("Commons")) {
            this.getLogger()
                    .warning("Commons was not found, disabling Battlecraft");
            this.getPluginLoader().disablePlugin(this);
        }
    }

    @Override
    public void onEnable() {
        if (!this.getServer().getPluginManager().isPluginEnabled("Commons")) {
            this.getLogger().warning("Commons was not enabled, disabling Battlecraft");
            this.getPluginLoader().disablePlugin(this);
            return;
        }
        Bukkit.setDefaultGameMode(GameMode.ADVENTURE);
        instance = this;
        ProtocolLibrary.getProtocolManager().addPacketListener(new SoundFilter(this));
        this.warpManager = new WarpManager(this);
        TranslationCommon.addTranslation(new PropertiesStorageDataTranslation(BattlecraftTranslateTag.class));
        loadManagers();
        loadCommands();
        loadWarps();
        loadListeners();


        for (Player player : Bukkit.getOnlinePlayers()) {
            // Clear some data
            CombatLogManager.removeCombatLog(player);
            TeleportManager.removeTeleporting(player);

            // When reloading teleport back to Spawn
            warpManager.joinWarp(player, warpManager.getDefaultWarp());
        }
    }

    private void loadCommands() {
        new CommandLoader(new BukkitCommandFramework(this))
                .loadCommandsFromPackage(getFile(), "br.com.battlebits.battlecraft.command");
    }

    private void loadWarps() {
        Warp warp = new WarpSpawn(new SpawnMap());
        this.warpManager.addWarp(warp);
        this.warpManager.setDefaultWarp(warp);
        warp = new Warp1v1(new OneVsOneMap());
        this.warpManager.addWarp(warp);
    }

    private void loadListeners() {
        PluginManager manager = getServer().getPluginManager();

        manager.registerEvents(new ProtectionListener(), this);
        manager.registerEvents(new MoveListener(), this);
        manager.registerEvents(new PlayerListener(), this);
        manager.registerEvents(new DamageFixListener(), this);
        manager.registerEvents(new DeathListener(), this);
        manager.registerEvents(new SoupListener(), this);
        manager.registerEvents(new WarpListener(), this);
        manager.registerEvents(new CombatLogListener(), this);
        manager.registerEvents(new TeleportListener(), this);
        manager.registerEvents(new ItemFrameListener(), this);
        manager.registerEvents(new DataListener(), this);
    }

    private void loadManagers() {
        AbilityManager.registerAbilities();
        this.statusManager = new StatusManager();
    }
}
