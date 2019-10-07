package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.battlecraft.manager.CombatLogManager;
import br.com.battlebits.battlecraft.manager.CombatLogManager.CombatLog;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class CombatLogListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamage(PlayerDamagePlayerEvent e) {
        Player damager = e.getPlayer();
        Player damaged = e.getDamaged();
        CombatLogManager.newCombatLog(damaged, damager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent e) {
        CombatLogManager.removeCombatLog(e.getEntity());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        checkCombatLog(p);
    }

    @EventHandler
    public void onVoidDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (event.getCause() != DamageCause.VOID)
            return;
        Player p = (Player) event.getEntity();
        checkCombatLog(p);
    }

    private void checkCombatLog(Player p) {
        CombatLog log = CombatLogManager.getCombatLog(p);
        if (log.isFighting()) {
            Player combatLogger = log.getCombatLogged();
            if (combatLogger != null)
                if (combatLogger.isOnline())
                    p.damage(Double.MAX_VALUE, combatLogger);
        }
        CombatLogManager.removeCombatLog(p);
    }

}
