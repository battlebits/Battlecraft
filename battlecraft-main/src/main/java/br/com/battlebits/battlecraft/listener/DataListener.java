package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.manager.StatusManager;
import br.com.battlebits.battlecraft.status.StatusAccount;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class DataListener implements Listener {

    @EventHandler
    public void onStatusLoad(AsyncPlayerPreLoginEvent event) {
        if(event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED)
            return;
        StatusManager manager = Battlecraft.getInstance().getStatusManager();
        StatusAccount account = manager.dataStatus().getStatus(event.getUniqueId());
        if (account == null) {
            account = new StatusAccount(event.getUniqueId(), event.getName());
            manager.dataStatus().saveAccount(account);
        }
        manager.insert(account);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Battlecraft.getInstance().getStatusManager().remove(event.getPlayer().getUniqueId());
    }
}
