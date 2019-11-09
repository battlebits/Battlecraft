package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.event.RealMoveEvent;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent.UpdateType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class MoveListener implements Listener {
    private Map<Player, Location> locations;

    public MoveListener() {
        locations = new HashMap<>();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        locations.remove(event.getPlayer());
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() != UpdateType.TICK)
            return;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (locations.containsKey(p)) {
                Location from = locations.get(p);
                if (from.getX() == p.getLocation().getX() && from.getZ() == p.getLocation().getZ()
                        && from.getY() == p.getLocation().getY())
                    continue;
                Bukkit.getServer().getPluginManager().callEvent(new RealMoveEvent(p, from, p.getLocation()));
            }
            locations.put(p, p.getLocation());
        }
    }
}
