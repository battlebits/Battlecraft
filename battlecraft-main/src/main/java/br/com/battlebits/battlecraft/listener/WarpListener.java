package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.manager.WarpManager;
import br.com.battlebits.battlecraft.warp.Warp;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.api.title.TitleAPI;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.JOIN_MESSAGE_SUBTITLE;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.JOIN_MESSAGE_TITLE;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class WarpListener implements Listener {


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        WarpManager manager = Battlecraft.getInstance().getWarpManager();
        Warp defaultWarp = manager.getDefaultWarp();
        manager.joinWarp(player, defaultWarp);
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.ADVENTURE);
        Language l = Commons.getLanguage(player.getUniqueId());
        TitleAPI.setTitle(player, tl(l, JOIN_MESSAGE_TITLE), tl(l, JOIN_MESSAGE_SUBTITLE)
                , 30, 20, 30,
                true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Battlecraft.getInstance().getWarpManager().leaveWarp(event.getPlayer());
    }

}
