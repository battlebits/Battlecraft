package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.manager.StatusManager;
import br.com.battlebits.battlecraft.status.StatusAccount;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataListener implements Listener {

    @EventHandler
    public void onStatusLoad(AsyncPlayerPreLoginEvent event) {
        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;
        try {
            StatusManager manager = Battlecraft.getInstance().getStatusManager();
            StatusAccount account = manager.dataStatus().getStatus(event.getUniqueId(), event.getName());
            if (account == null) {
                account = new StatusAccount(event.getUniqueId(), event.getName());
                manager.dataStatus().saveAccount(account);
            }
            manager.insert(account);
        } catch (Exception e) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage("Error");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onKick(AsyncPlayerPreLoginEvent event) {
        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;
        StatusManager manager = Battlecraft.getInstance().getStatusManager();
        if (manager.get(event.getUniqueId()) == null) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage("Error");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(PlayerLoginEvent event) {
        if (event.getResult() != org.bukkit.event.player.PlayerLoginEvent.Result.ALLOWED)
            return;
        StatusManager manager = Battlecraft.getInstance().getStatusManager();
        if (manager.get(event.getPlayer().getUniqueId()) == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "Error");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Battlecraft.getInstance().getStatusManager().remove(event.getPlayer().getUniqueId());
    }
}
