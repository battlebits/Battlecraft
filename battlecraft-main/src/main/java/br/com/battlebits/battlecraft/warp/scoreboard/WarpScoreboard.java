package br.com.battlebits.battlecraft.warp.scoreboard;

import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.account.BukkitAccount;
import br.com.battlebits.commons.bukkit.scoreboard.BattleScoreboard;
import br.com.battlebits.commons.bukkit.util.string.StringScroller;
import lombok.Getter;
import org.bukkit.entity.Player;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.BOARD_MAIN_TITLE;

public abstract class WarpScoreboard {

    protected StringScroller scroller;
    @Getter
    private String title;

    public WarpScoreboard(StringScroller scroller) {
        this.scroller = scroller;
        this.updateTitleText();
    }

    public void applyScoreboard(Player player) {
        getBattleBoard(player).getLines().clear();
        updateTitle(player);
    }

    public void updateTitleText() {
        title = scroller.next();
    }

    public void updateTitle(Player player) {
        getBattleBoard(player).update(battleScoreboard -> battleScoreboard.setDisplayName(Commons.getLanguage(player.getUniqueId()).tl(BOARD_MAIN_TITLE, title)));
    }

    protected BattleScoreboard getBattleBoard(Player player) {
        return ((BukkitAccount) Commons.getAccount(player.getUniqueId())).getBattleboard();
    }

}
