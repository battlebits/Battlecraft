package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.manager.CooldownManager;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CooldownListener implements Listener {

    @EventHandler
    public void onUpdateEvent(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.SECOND) {
            CooldownManager.checkCooldowns();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CooldownManager.removeAllCooldowns(event.getPlayer());
    }
}
