package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpTeleportEvent;
import br.com.battlebits.battlecraft.warp.Warp;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.translate.Language;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class TeleportManager {

    private final static String IN_PROGRESS_META = "teleport.in_progress";
    private final static String WARP_META = "teleport.warp";

    private final static long COOLDOWN_TIME = 5000L;

    public static void teleport(Player player, Warp to) {
        Language language = Commons.getLanguage(player.getUniqueId());
        if (isTeleporting(player)) {
            player.sendMessage(tl(language,TELEPORT_TAG) + tl(language, TELEPORT_IN_PROGRESS));
            return;
        }
        if (!player.isOnGround()) {
            player.sendMessage(tl(language,TELEPORT_TAG) + tl(language, TELEPORT_ON_AIR));
            return;
        }

        PlayerWarpTeleportEvent event = new PlayerWarpTeleportEvent(player, to);
        Battlecraft.getInstance().getServer().getPluginManager().callEvent(event);

        if (!ProtectionManager.isProtected(player) && CombatLogManager.getCombatLog(player).isFighting()) {
            player.setMetadata(IN_PROGRESS_META, new FixedMetadataValue(Battlecraft.getInstance(), System.currentTimeMillis()));
            player.setMetadata(WARP_META, new FixedMetadataValue(Battlecraft.getInstance(), to.getId()));

            player.sendMessage(tl(language,TELEPORT_TAG) + tl(language, TELEPORT_COOLDOWN));
            player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_STEP, 1.0f, 1.0f);
            return;
        }
        Battlecraft.getInstance().getWarpManager().joinWarp(player, to);
    }

    public static boolean isTeleporting(Player player) {
        return player.hasMetadata(IN_PROGRESS_META);
    }

    public static void cancelTeleporting(Player player) {
        Battlecraft plugin = Battlecraft.getInstance();

        if (player.hasMetadata(IN_PROGRESS_META))
            player.removeMetadata(IN_PROGRESS_META, plugin);
    }

    public static TeleportProcess getTeleportProcess(Player player) {

        String warp_id = "";
        long time = 0;
        if (player.hasMetadata(WARP_META))
            warp_id = player.getMetadata(WARP_META).get(0).asString();
        if (player.hasMetadata(IN_PROGRESS_META))
            time = player.getMetadata(IN_PROGRESS_META).get(0).asLong();
        Warp warp = Battlecraft.getInstance().getWarpManager().getWarp(warp_id);

        return new TeleportProcess(warp, time);
    }

    @Data
    @RequiredArgsConstructor
    public static class TeleportProcess {
        private final Warp warp;
        private final long time;

        public boolean canTeleport() {
            return System.currentTimeMillis() > time + COOLDOWN_TIME;
        }

    }

}
