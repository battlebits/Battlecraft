package br.com.battlebits.battlecraft.warp;

import br.com.battlebits.battlecraft.world.WorldMap;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class WarpMain extends WarpLocation {

    public WarpMain(String name, String description, ItemStack itemStack, Location spawnLocation, WorldMap map) {
        super(name, description, itemStack, spawnLocation, map);
    }

}
