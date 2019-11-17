package br.com.battlebits.battlecraft.warp.scoreboard;

import org.bukkit.entity.Player;

public interface WarpScoreboard {

    void applyScoreboard(Player player);

    void updateTitleText();

    void updateTitle(Player player);

}
