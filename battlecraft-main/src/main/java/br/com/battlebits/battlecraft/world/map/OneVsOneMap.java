package br.com.battlebits.battlecraft.world.map;

import br.com.battlebits.battlecraft.world.WorldMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;

@Getter
@AllArgsConstructor
public class OneVsOneMap implements WorldMap {

    private Location firstLocation;
    private Location secondLocation;

    @Override
    public void loadMap() {

    }

    @Override
    public void unloadMap() {

    }
}
