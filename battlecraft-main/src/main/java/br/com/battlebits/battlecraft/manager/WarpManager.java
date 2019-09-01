package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.event.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.warp.Warp;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class WarpManager {

    private Plugin plugin;
    private Map<String, Warp> warps;


    public WarpManager(Plugin plugin) {
        this.plugin = plugin;
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
            PlayerWarpQuitEvent event = new PlayerWarpQuitEvent(player, warp);
            plugin.getServer().getPluginManager().callEvent(event);
            if(event.isCancelled()) {
                cancelled = true;
            }
        }
        PlayerWarpJoinEvent event = new PlayerWarpJoinEvent(player, warp);
        event.setCancelled(cancelled);
        plugin.getServer().getPluginManager().callEvent(event);
        if(!event.isCancelled()) {
            player.setMetadata("warp", new FixedMetadataValue(plugin, warp.getId()));
            player.teleport(warp.getSpawnLocation());
        }
    }

    public void addWarp(Warp warp) {
        warps.put(warp.getId(), warp);
    }


}
