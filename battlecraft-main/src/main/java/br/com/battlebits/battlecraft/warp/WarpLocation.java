package br.com.battlebits.battlecraft.warp;

import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.commons.bukkit.scoreboard.BattleScoreboard;
import br.com.battlebits.commons.command.CommandClass;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class WarpLocation implements Warp, Listener, CommandClass {

    private String name;
    @Getter
    private Material material;
    private List<Player> players;

    @Getter
    private Location spawnLocation;
    private BattleScoreboard scoreboard;

    private WorldMap worldMap;

    public WarpLocation(String name, Material material, Location spawnLocation, WorldMap map) {
        this.name = name;
        this.material = material;
        this.players = new ArrayList<>();
        this.spawnLocation = spawnLocation;
        this.worldMap = map;
    }

    @Override
    public String getId() {
        return this.name.toLowerCase().trim().replace(" ", ".");
    }

    @Override
    public void onLoad() {
        worldMap.loadMap();
    }

    @Override
    public void onUnload() {
        worldMap.unloadMap();
        players.clear();
    }

    @Override
    public boolean isInWarp(Player player) {
        return players.contains(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void addPlayer(PlayerWarpJoinEvent event) {
        Player p = event.getPlayer();
        if (event.getWarp() != this)
            return;
        players.add(p);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoinMonitor(PlayerWarpJoinEvent event) {
        if (event.isCancelled())
            players.remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void removePlayer(PlayerWarpQuitEvent event) {
        if (event.isCancelled())
            players.remove(event.getPlayer());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof WarpLocation))
            return false;
        WarpLocation compare = (WarpLocation) obj;
        return compare.getId().equals(this.getId());
    }
}
