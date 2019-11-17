package br.com.battlebits.battlecraft.world;

import org.bukkit.Location;

public interface WorldMap {

    void loadMap();

    void unloadMap();

    Location getSpawnLocation();

}
