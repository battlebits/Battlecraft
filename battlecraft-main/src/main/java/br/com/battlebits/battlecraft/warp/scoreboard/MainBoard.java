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

public class MainBoard implements WarpScoreboard {

    private StringScroller scroller;
    private String title;

    public MainBoard(String warpName) {
        scroller = new StringScroller("BattleCraft - " + warpName + " -", 14, 1);
        title = scroller.next();
    }

    public void updateTitleText() {
        title = scroller.next();
    }

    public void updateTitle(Player player) {
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.setDisplayName(Commons.getLanguage(player.getUniqueId()).tl(BOARD_MAIN_TITLE, title)));
    }

    private StatusMain getStatus(Player player) {
        return (StatusMain) Battlecraft.getInstance().getStatusManager().get(player.getUniqueId()).getWarpStatus(Battlecraft.getInstance().getWarpManager().getDefaultWarp());
    }

    private BattleScoreboard getBattleBoard(Player player) {
        return ((BukkitAccount) Commons.getAccount(player.getUniqueId())).getBattleboard();
    }

    public void updateKills(Player player) {
        BukkitAccount account = (BukkitAccount) Commons.getAccount(player.getUniqueId());
        Language l = account.getLanguage();
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.getLineById("kills").setText(l.tl(BOARD_MAIN_KILLS, getStatus(player).getKills())));
    }

    public void updateKit(Player player, Kit kit) {
        BukkitAccount account = (BukkitAccount) Commons.getAccount(player.getUniqueId());
        Language l = account.getLanguage();
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.getLineById("kit").setText(l.tl(BOARD_MAIN_KIT, NameUtils.formatString(kit.getName()))));
    }

    @Override
    public void applyScoreboard(Player player) {
        BukkitAccount account = (BukkitAccount) Commons.getAccount(player.getUniqueId());
        StatusMain main = getStatus(player);
        Language l = account.getLanguage();
        BattleScoreboard board = account.getBattleboard();
        board.setDisplayName(l.tl(BOARD_MAIN_TITLE, title));
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
        String noone = l.tl(BOARD_MAIN_TOPKS_NOONE);
        board.addLine("topksplayer", l.tl(BOARD_MAIN_TOPKS_PLAYER, noone, 0));
        board.addLine("n" + ++nullLine, "");
        board.addLine("kit", l.tl(BOARD_MAIN_KIT, kitName));
        board.addLine("n" + ++nullLine, "");
        board.addLine("website", l.tl(BOARD_MAIN_WEBSITE, CommonsConst.WEBSITE));
        board.update();
    }
}
