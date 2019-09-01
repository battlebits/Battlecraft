package br.com.battlebits.battlecraft.warp;

import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.commons.bukkit.scoreboard.BattleScoreboard;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class WarpLocation implements Warp, Listener {

    private String name;
    private String description;
    private ItemStack icon;
    private List<Player> players;

    @Getter
    private Location spawnLocation;
    private BattleScoreboard scoreboard;

    private WorldMap worldMap;

    public WarpLocation(String name, String description, ItemStack itemStack, Location spawnLocation, WorldMap map) {
        this.name = name;
        this.description = description;
        this.icon = itemStack;
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
}
