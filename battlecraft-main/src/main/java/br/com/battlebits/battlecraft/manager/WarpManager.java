package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.util.InventoryUtils;
import br.com.battlebits.battlecraft.warp.Warp;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WarpManager {

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
        return Battlecraft.getInstance().getStatusManager().get(player.getUniqueId()).getCurrentWarp();
    }

    public void joinWarp(Player player, Warp warp) {
        leaveWarp(player);
        if (warp.getPlayers().isEmpty()) {
            plugin.getServer().getPluginManager().registerEvents(warp, plugin);
        }
        PlayerWarpJoinEvent event = new PlayerWarpJoinEvent(player, warp);
        Battlecraft.getInstance().getStatusManager().get(player.getUniqueId()).joinWarp(warp);
        AttributeInstance attr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (attr != null) {
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
        Warp currentWarp = getPlayerWarp(p);
        PlayerWarpQuitEvent event = new PlayerWarpQuitEvent(p, currentWarp);
        Battlecraft.getInstance().getStatusManager().get(p.getUniqueId()).leaveWarp();
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
