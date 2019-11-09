package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.util.InventoryUtils;
import br.com.battlebits.battlecraft.warp.Warp;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarpManager {

    private static final String WARP_META = "warp";
    private Plugin plugin;

    private Map<String, Warp> warps;
    private Warp defaultWarp;

    public WarpManager(Plugin plugin) {
        this.plugin = plugin;
        this.warps = new HashMap<>();
    }

    public Collection<Warp> getWarps() {
        return warps.values();
    }

    public Warp getWarp(String id) {
        return warps.getOrDefault(id, null);
    }

    public Warp getPlayerWarp(Player player) {
        if (player.hasMetadata(WARP_META)) {
            List<MetadataValue> list = player.getMetadata(WARP_META);
            return getWarp(!list.isEmpty() ? list.get(0).asString() : null);
        }
        return null;
    }

    public void joinWarp(Player player, Warp warp) {
        leaveWarp(player);
        if (warp.getPlayers().isEmpty()) {
            plugin.getServer().getPluginManager().registerEvents(warp, plugin);
        }
        PlayerWarpJoinEvent event = new PlayerWarpJoinEvent(player, warp);
        player.setMetadata(WARP_META, new FixedMetadataValue(plugin, warp.getId()));
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if(attr != null) {
            attr.setBaseValue(20.0);
            player.setHealth(attr.getValue());
        }
        player.setFoodLevel(20);
        InventoryUtils.clearInventory(player);
        player.setVelocity(new Vector());
        player.teleport(warp.getSpawnLocation());
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public void leaveWarp(Player p) {
        Warp lastWarp = getPlayerWarp(p);
        if (lastWarp == null)
            return;
        PlayerWarpQuitEvent event = new PlayerWarpQuitEvent(p, lastWarp);
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public void addWarp(Warp warp) {
        warps.put(warp.getId(), warp);
    }

    public Warp getDefaultWarp() {
        return defaultWarp;
    }

    public void setDefaultWarp(Warp warp) {
        this.defaultWarp = warp;
    }
}
