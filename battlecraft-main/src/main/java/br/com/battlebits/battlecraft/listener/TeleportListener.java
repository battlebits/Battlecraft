package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.battlecraft.event.RealMoveEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpTeleportEvent;
import br.com.battlebits.battlecraft.manager.TeleportManager;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static br.com.battlebits.battlecraft.manager.TeleportManager.*;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.TELEPORT_CANCELED;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.TELEPORT_TAG;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class TeleportListener implements Listener {

    private Set<Player> teleporting = new HashSet<>();

    public void onTeleportProcess(PlayerWarpTeleportEvent event) {
        teleporting.add(event.getPlayer());
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.SECOND) {
            Iterator<Player> iterator = teleporting.iterator();
            while (iterator.hasNext()) {
                Player player = iterator.next();
                if (!isTeleporting(player)) {
                    iterator.remove();
                    continue;
                }
                TeleportProcess process = getTeleportProcess(player);
                if (process.canTeleport()) {
                    Battlecraft.getInstance().getWarpManager().joinWarp(player, process.getWarp());
                    iterator.remove();
                    continue;
                }
            }
        }
    }

    @EventHandler
    public void onRealMove(RealMoveEvent event) {
        checkAndCancelTeleporting(event.getPlayer());
    }

    @EventHandler
    public void onDamage(PlayerDamagePlayerEvent event) {
        checkAndCancelTeleporting(event.getDamaged());
        checkAndCancelTeleporting(event.getDamager());
    }

    @EventHandler
    public void onDamage(PlayerDeathEvent event) {
        checkAndCancelTeleporting(event.getEntity());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (teleporting.contains(event.getPlayer()))
            teleporting.remove(event.getPlayer());
    }

    private void checkAndCancelTeleporting(Player player) {
        if (teleporting.contains(player)) {
            TeleportManager.cancelTeleporting(player);
            Language l = Commons.getLanguage(player.getUniqueId());
            player.sendMessage(tl(l, TELEPORT_TAG) + tl(l, TELEPORT_CANCELED));
            teleporting.remove(player);
        }
    }
}
