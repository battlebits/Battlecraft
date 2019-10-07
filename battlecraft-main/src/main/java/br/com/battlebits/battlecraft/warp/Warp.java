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
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class Warp implements Listener, CommandClass {

    private String name;
    @Getter
    private Material material;
    private Set<Player> players;

    @Getter
    private Location spawnLocation;
    private BattleScoreboard scoreboard;

    private WorldMap worldMap;

    public Warp(String name, Material material, Location spawnLocation, WorldMap map) {
        this.name = name;
        this.material = material;
        this.players = new HashSet<>();
        this.spawnLocation = spawnLocation;
        this.worldMap = map;
    }

    public String getId() {
        return this.name.toLowerCase().trim().replace(" ", ".");
    }

    public void onLoad() {
        worldMap.loadMap();
    }

    public void onUnload() {
        worldMap.unloadMap();
        players.clear();
    }

    public boolean inWarp(Player player) {
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
        if (players.remove(event.getPlayer()) && players.size() == 0)
            HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void removePlayer(PlayerWarpQuitEvent event) {
        if (inWarp(event.getPlayer())) {
            players.remove(event.getPlayer());
            if (players.size() == 0)
                HandlerList.unregisterAll(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Warp))
            return false;
        Warp compare = (Warp) obj;
        return compare.getId().equals(this.getId());
    }
}
