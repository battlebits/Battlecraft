package br.com.battlebits.battlecraft.warp.scoreboard;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.manager.KitManager;
import br.com.battlebits.battlecraft.status.warpstatus.StatusMain;
import br.com.battlebits.battlecraft.util.NameUtils;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.CommonsConst;
import br.com.battlebits.commons.bukkit.account.BukkitAccount;
import br.com.battlebits.commons.bukkit.scoreboard.BattleScoreboard;
import br.com.battlebits.commons.bukkit.util.string.StringScroller;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.entity.Player;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;

public class MainBoard extends WarpScoreboard {

    public MainBoard(String warpName) {
        super(new StringScroller("BattleCraft - " + warpName + " -", 14, 1));
    }

    private StatusMain getStatus(Player player) {
        return (StatusMain) Battlecraft.getInstance().getStatusManager().get(player.getUniqueId()).getWarpStatus(Battlecraft.getInstance().getWarpManager().getDefaultWarp());
    }

    public void updateKills(Player player) {
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.getLineById("kills").setText(Commons.getLanguage(player.getUniqueId()).tl(BOARD_MAIN_KILLS, getStatus(player).getKills())));
        updateKillstreak(player);
    }

    public void updateDeaths(Player player) {
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.getLineById("deaths").setText(Commons.getLanguage(player.getUniqueId()).tl(BOARD_MAIN_DEATHS, getStatus(player).getDeaths())));
        updateKillstreak(player);
    }

    private void updateKillstreak(Player player) {
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.getLineById(
                "killstreak").setText(Commons.getLanguage(player.getUniqueId()).tl(BOARD_MAIN_KILLSTREAK, getStatus(player).getKillstreak())));
    }

    public void updateKit(Player player, Kit kit) {
        BukkitAccount account = (BukkitAccount) Commons.getAccount(player.getUniqueId());
        Language l = account.getLanguage();
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.getLineById("kit").setText(l.tl(BOARD_MAIN_KIT, NameUtils.formatString(kit.getName()))));
    }

    public void resetTopKillstreak(Player player) {
        updateTopKillstreak(player,
                Commons.getLanguage(player.getUniqueId()).tl(PLAYER_NONE), 0);
    }

    public void updateTopKillstreak(Player player, String playerName, int killstreak) {
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.getLineById(
                "topksplayer").setText(Commons.getLanguage(player.getUniqueId()).tl(BOARD_MAIN_TOPKS_PLAYER, playerName, killstreak)));
    }

    @Override
    public void applyScoreboard(Player player) {
        super.applyScoreboard(player);
        BukkitAccount account = (BukkitAccount) Commons.getAccount(player.getUniqueId());
        StatusMain main = getStatus(player);
        Language l = account.getLanguage();
        BattleScoreboard board = account.getBattleboard();
        String kitName = account.getLanguage().tl(KIT_NONE);
        if (KitManager.containsKit(player))
            kitName = NameUtils.formatString(KitManager.getCurrentPlayerKit(player).getName());
        int nullLine = 0;
        board.addLine("n" + ++nullLine, "");
        board.addLine("kills", l.tl(BOARD_MAIN_KILLS, main.getKills()));
        board.addLine("deaths", l.tl(BOARD_MAIN_DEATHS, main.getDeaths()));
        board.addLine("killstreak", l.tl(BOARD_MAIN_KILLSTREAK, main.getKillstreak()));
        board.addLine("level", l.tl(BOARD_MAIN_LEVEL, account.getLevel()));
        board.addLine("n" + ++nullLine, "");
        board.addLine("topks", l.tl(BOARD_MAIN_TOPKS));
        String noone = l.tl(PLAYER_NONE);
        board.addLine("topksplayer", l.tl(BOARD_MAIN_TOPKS_PLAYER, noone, 0));
        board.addLine("n" + ++nullLine, "");
        board.addLine("kit", l.tl(BOARD_MAIN_KIT, kitName));
        board.addLine("n" + ++nullLine, "");
        board.addLine("website", l.tl(BOARD_MAIN_WEBSITE, CommonsConst.WEBSITE));
        board.update();
    }
}
