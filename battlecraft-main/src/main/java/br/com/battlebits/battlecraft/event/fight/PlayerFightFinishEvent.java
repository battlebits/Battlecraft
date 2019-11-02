package br.com.battlebits.battlecraft.event.fight;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerFightFinishEvent extends Event {
    @Getter
    public static final HandlerList handlerList = new HandlerList();

    private Player[] players;
    private Player winner;

    public PlayerFightFinishEvent(Player loser, Player winner) {
        this.players = new Player[] { loser, winner };
        this.winner = winner;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }
}
