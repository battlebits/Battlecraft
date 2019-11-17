package br.com.battlebits.battlecraft.warp.scoreboard;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.manager.KitManager;
import br.com.battlebits.battlecraft.status.ranking.Queue1v1;
import br.com.battlebits.battlecraft.status.ranking.RankedQueue;
import br.com.battlebits.battlecraft.status.warpstatus.Status1v1;
import br.com.battlebits.battlecraft.status.warpstatus.StatusMain;
import br.com.battlebits.battlecraft.util.NameUtils;
import br.com.battlebits.battlecraft.warp.Warp1v1;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.CommonsConst;
import br.com.battlebits.commons.bukkit.account.BukkitAccount;
import br.com.battlebits.commons.bukkit.scoreboard.BattleScoreboard;
import br.com.battlebits.commons.bukkit.util.string.StringScroller;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.entity.Player;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.BOARD_MAIN_WEBSITE;

public class OneVsOneBoard implements WarpScoreboard {

    private StringScroller scroller;
    private String title;
    private Warp1v1 warp;

    public OneVsOneBoard(Warp1v1 warp) {
        this.warp = warp;
        scroller = new StringScroller("BattleCraft - " + warp.getName() + " -", 14, 1);
        title = scroller.next();
    }

    public void updateTitleText() {
        title = scroller.next();
    }

    public void updateTitle(Player player) {
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.setDisplayName(Commons.getLanguage(player.getUniqueId()).tl(BOARD_MAIN_TITLE, title)));
    }

    private RankedQueue getStatus(Player player) {
        return ((Status1v1)Battlecraft.getInstance().getStatusManager().get(player.getUniqueId()).getWarpStatus(warp)).getQueueStatus(Queue1v1.NORMAL);
    }

    private BattleScoreboard getBattleBoard(Player player) {
        return ((BukkitAccount) Commons.getAccount(player.getUniqueId())).getBattleboard();
    }

    public void updateVictory(Player player) {
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.getLineById("victory").setText(Commons.getLanguage(player.getUniqueId()).tl(BOARD_1v1_VICTORY, getStatus(player).getVictory())));
    }

    public void updateDefeat(Player player) {
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.getLineById("defeat").setText(Commons.getLanguage(player.getUniqueId()).tl(BOARD_1v1_DEFEAT, getStatus(player).getDefeat())));
    }

    public void resetOpponent(Player player) {
        updateOpponent(player,
                Commons.getLanguage(player.getUniqueId()).tl(PLAYER_NONE));
    }

    public void updateOpponent(Player player, String playerName) {
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.getLineById(
                "opponent").setText(Commons.getLanguage(player.getUniqueId()).tl(BOARD_1V1_OPPONENT, playerName)));
    }

    @Override
    public void applyScoreboard(Player player) {
        BukkitAccount account = (BukkitAccount) Commons.getAccount(player.getUniqueId());
        RankedQueue main = getStatus(player);
        Language l = account.getLanguage();
        BattleScoreboard board = account.getBattleboard();
        board.getLines().clear();
        board.update();
        board.setDisplayName(l.tl(BOARD_MAIN_TITLE, title));
        String kitName = account.getLanguage().tl(KIT_NONE);
        if (KitManager.containsKit(player))
            kitName = NameUtils.formatString(KitManager.getCurrentPlayerKit(player).getName());
        int nullLine = 0;
        board.addLine("n" + ++nullLine, "");
        board.addLine("victory", l.tl(BOARD_1v1_VICTORY, main.getVictory()));
        board.addLine("defeat", l.tl(BOARD_1v1_DEFEAT, main.getDefeat()));
        board.addLine("level", l.tl(BOARD_MAIN_LEVEL, account.getLevel()));
        board.addLine("n" + ++nullLine, "");
        String noone = l.tl(PLAYER_NONE);
        board.addLine("fight", l.tl(BOARD_1V1_FIGHT));
        board.addLine("opponent", l.tl(BOARD_1V1_OPPONENT, noone));
        board.addLine("n" + ++nullLine, "");
        board.addLine("website", l.tl(BOARD_MAIN_WEBSITE, CommonsConst.WEBSITE));
        board.update();
    }
}
