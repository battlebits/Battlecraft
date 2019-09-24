package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.warp.Warp;
import br.com.battlebits.battlecraft.warp.WarpLocation;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpManager {

    private Plugin plugin;
    private Map<String, Warp> warps;

    private String defaultWarp;

    public WarpManager(Plugin plugin) {
        this.plugin = plugin;
        this.warps = new HashMap<>();
    }

    public Warp getWarp(String id) {
        return warps.getOrDefault(id, null);
    }

    public Warp getPlayerWarp(Player player) {
        if (player.hasMetadata("warp")) {
            List<MetadataValue> list = player.getMetadata("warp");
            return getWarp(list.size() > 0 ? list.get(0).asString() : null);
        }
        return null;
    }

    public void joinWarp(Player player, Warp warp) {
        Warp lastWarp = getPlayerWarp(player);
        boolean cancelled = false;
        if(lastWarp != null) {
            PlayerWarpQuitEvent event = new PlayerWarpQuitEvent(player, lastWarp);
            plugin.getServer().getPluginManager().callEvent(event);
            cancelled = event.isCancelled();
        }
        PlayerWarpJoinEvent event = new PlayerWarpJoinEvent(player, warp);
        event.setCancelled(cancelled);
        plugin.getServer().getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            player.setMetadata("warp", new FixedMetadataValue(plugin, warp.getId()));
            player.teleport(warp.getSpawnLocation());
        }
    }

    public void leaveWarp(Player p) {
        Warp lastWarp = getPlayerWarp(p);
        if(lastWarp == null)
            return;
        PlayerWarpQuitEvent event = new PlayerWarpQuitEvent(p, lastWarp);
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public void addWarp(WarpLocation warp) {
        warps.put(warp.getId(), warp);
        this.plugin.getServer().getPluginManager().registerEvents(warp, plugin);
    }

    public void setDefaultWarp(Warp warp) {
        this.defaultWarp = warp.getId();
    }

    public Warp getDefaultWarp() {
        return getWarp(defaultWarp);
    }
}
