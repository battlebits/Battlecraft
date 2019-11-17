package br.com.battlebits.battlecraft.status.ranking;

import org.bukkit.ChatColor;

public enum League {
    NONE("", Integer.MAX_VALUE), //
    UNRANKED(ChatColor.WHITE + "-", Integer.MAX_VALUE), //
    PRIMARY(ChatColor.GREEN + "☰", 2000), //
    ADVANCED(ChatColor.YELLOW + "☲", 1100), //
    EXPERT(ChatColor.DARK_BLUE + "☷", 1200), //
    SILVER(ChatColor.GRAY + "✶", 1300), //
    GOLD(ChatColor.GOLD + "✷", 1500), //
    DIAMOND(ChatColor.AQUA + "✦", 1600), //
    ELITE(ChatColor.DARK_PURPLE + "✹", 1800), //
    MASTER(ChatColor.RED + "✫", 2000), //
    LEGENDARY(ChatColor.DARK_RED + "✪", Integer.MAX_VALUE), //
    CHALLENGER(ChatColor.DARK_AQUA + "☯", Integer.MAX_VALUE);

    private String symbol;
    private int maxXp;

    League(String symbol, int xp) {
        this.symbol = symbol;
        this.maxXp = xp;
    }

    public int getMaxXp() {
        return maxXp;
    }

    public String getSymbol() {
        return symbol;
    }

    public League getNextLeague() {
        return League.values()[ordinal() + 1];
    }

    public League getPreviousLeague() {
        return League.values()[ordinal() - 1];
    }

}
