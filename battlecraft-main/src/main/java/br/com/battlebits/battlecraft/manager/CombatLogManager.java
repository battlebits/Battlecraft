package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.Battlecraft;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class CombatLogManager {

    private final static String COMBATLOG_PLAYER = "combatlog.player";
    private final static String COMBATLOG_EXPIRE = "combatlog.time";

    private final static long COMBATLOG_TIME = 10000L; // Miliseconds

    public static void newCombatLog(Player damager, Player damaged) {
        setCombatLog(damager, damaged);
        setCombatLog(damaged, damager);
    }


    public static void removeCombatLog(Player player) {
        Battlecraft plugin = Battlecraft.getInstance();

        if (player.hasMetadata(COMBATLOG_PLAYER))
            player.removeMetadata(COMBATLOG_PLAYER, plugin);
        if (player.hasMetadata(COMBATLOG_EXPIRE))
            player.removeMetadata(COMBATLOG_EXPIRE, plugin);
    }

    private static void setCombatLog(Player player1, Player player2) {
        Battlecraft plugin = Battlecraft.getInstance();

        removeCombatLog(player1);

        player1.setMetadata(COMBATLOG_PLAYER, new FixedMetadataValue(plugin, player2.getName()));
        player1.setMetadata(COMBATLOG_EXPIRE, new FixedMetadataValue(plugin, System.currentTimeMillis()));

    }

    public static CombatLog getCombatLog(Player player) {
        Battlecraft plugin = Battlecraft.getInstance();
        String playerName = "";
        long time = 0L;
        if (player.hasMetadata(COMBATLOG_PLAYER))
            playerName = player.getMetadata(COMBATLOG_PLAYER).get(0).asString();
        if (player.hasMetadata(COMBATLOG_EXPIRE))
            time = player.getMetadata(COMBATLOG_EXPIRE).get(0).asLong();
        Player combatLogged = Bukkit.getPlayer(playerName);
        return new CombatLog(combatLogged, time);
    }

    @Data
    @RequiredArgsConstructor
    public static class CombatLog {
        private final Player combatLogged;
        private final long time;


        public boolean isFighting() {
            return System.currentTimeMillis() < time + COMBATLOG_TIME;
        }

    }
}
