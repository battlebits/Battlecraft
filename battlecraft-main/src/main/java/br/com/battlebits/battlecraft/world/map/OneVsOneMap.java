package br.com.battlebits.battlecraft.world.map;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.world.WorldMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;

@Getter
public class OneVsOneMap implements WorldMap {

    private World world;
    private Location spawnLocation;

    private Location firstLocation;
    private Location secondLocation;

    public OneVsOneMap() {

    }

    public OneVsOneMap(Location firstLocation, Location secondLocation) {
        this.firstLocation = firstLocation;
        this.secondLocation = secondLocation;
    }

    @Override
    public void loadMap() {
        world = Bukkit.getServer().createWorld(new WorldCreator("1v1"));
        world.setAutoSave(true);
        Battlecraft.getInstance().getLogger().info("World '1v1' loaded!");
        spawnLocation = new Location(world, 0.5, 64, 0.5);
    }

    @Override
    public void unloadMap() {

    }
}
