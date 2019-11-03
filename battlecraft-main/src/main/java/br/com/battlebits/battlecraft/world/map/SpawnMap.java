package br.com.battlebits.battlecraft.world.map;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.world.WorldMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

@Getter
public class SpawnMap implements WorldMap {

    private World world;

    private Location spawnLocation;

    public SpawnMap() {

    }


    @Override
    public void loadMap() {
        world = Bukkit.getServer().createWorld(new WorldCreator("spawn"));
        world.setAutoSave(true);
        Battlecraft.getInstance().getLogger().info("World 'spawn' loaded!");
        spawnLocation = new Location(world, 0.5, 100, 0.5);
    }

    @Override
    public void unloadMap() {

    }
}
