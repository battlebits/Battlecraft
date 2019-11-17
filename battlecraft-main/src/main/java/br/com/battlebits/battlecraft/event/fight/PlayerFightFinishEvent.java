package br.com.battlebits.battlecraft.event.fight;

import br.com.battlebits.battlecraft.status.ranking.Queue1v1;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerFightFinishEvent extends Event {
    @Getter
    public static final HandlerList handlerList = new HandlerList();

    private Player winner;
    private Player loser;

    private Queue1v1 queue;

    public PlayerFightFinishEvent(Player winner, Player loser, Queue1v1 queue) {
        this.winner = winner;
        this.loser = loser;
        this.queue = queue;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }
}
