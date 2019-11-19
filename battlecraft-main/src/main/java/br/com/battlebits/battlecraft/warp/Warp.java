package br.com.battlebits.battlecraft.warp;

import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag;
import br.com.battlebits.battlecraft.warp.scoreboard.WarpScoreboard;
import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
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

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.valueOf;

@Getter
public abstract class Warp implements Listener, CommandClass {

    private static final int SCOREBOARD_TICK_UPDATE = 7;

    @Getter
    protected Set<Kit> kits;
    private String name;
    @Getter
    private Material material;
    private Set<Player> players;
    @Getter
    private Location spawnLocation;

    private WorldMap worldMap;

    public Warp(String name, Material material, WorldMap map) {
        this.name = name;
        this.material = material;
        this.players = new HashSet<>();
        this.kits = new HashSet<>();
        this.worldMap = map;
        this.worldMap.loadMap();
        this.spawnLocation = map.getSpawnLocation();
    }

    public String getId() {
        return this.name.toLowerCase().trim().replace(" ", ".");
    }

    public BattlecraftTranslateTag getNameTag() {
        return valueOf("WARP_" + getId().toUpperCase() + "_NAME");
    }

    public BattlecraftTranslateTag getDescriptionTag() {
        return valueOf("WARP_" + getId().toUpperCase() + "_DESCRIPTION");
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
    public void onApplyTabList(PlayerWarpJoinEvent event) {
        Player p = event.getPlayer();
        if (inWarp(p)) {
            applyTabList(p);
            applyScoreboard(p);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void removePlayer(PlayerWarpQuitEvent event) {
        if (inWarp(event.getPlayer())) {
            players.remove(event.getPlayer());
            if (players.isEmpty())
                HandlerList.unregisterAll(this);
        }
    }

    @EventHandler
    public void onTick(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.TICK && event.getCurrentTick() % SCOREBOARD_TICK_UPDATE == 0) {
            getScoreboard().updateTitleText();
            for (Player player : getPlayers()) {
                getScoreboard().updateTitle(player);
            }
        }
        if (event.getType() != UpdateEvent.UpdateType.SECOND)
            return;
        for (Player player : getPlayers()) {
            applyTabList(player);
        }
    }

    protected abstract void applyTabList(Player player);

    protected abstract void applyScoreboard(Player player);

    protected abstract WarpScoreboard getScoreboard();

    protected boolean isWarpKit(Kit kit) {
        return this.kits.contains(kit);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Warp))
            return false;
        Warp compare = (Warp) obj;
        return compare.getId().equals(this.getId());
    }

}
