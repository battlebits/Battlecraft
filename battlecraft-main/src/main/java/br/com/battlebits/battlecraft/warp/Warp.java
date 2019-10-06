package br.com.battlebits.battlecraft.warp;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface Warp {

    String getId();

    void onLoad();

    boolean isInWarp(Player player);

    void onUnload();

    Location getSpawnLocation();

    Material getMaterial();

}
