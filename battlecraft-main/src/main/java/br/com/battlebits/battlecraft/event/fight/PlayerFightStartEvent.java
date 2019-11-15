package br.com.battlebits.battlecraft.event.fight;

import br.com.battlebits.battlecraft.status.ranking.Queue1v1;
import br.com.battlebits.battlecraft.warp.fight.Challenge;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerFightStartEvent extends Event {

    @Getter
    public static final HandlerList handlerList = new HandlerList();

    private Player[] players;
    private Challenge challenge;
    private Queue1v1 queue;

    public PlayerFightStartEvent(Player player1, Player player2, Challenge challenge, Queue1v1 queue) {
        this.players = new Player[] { player1, player2 };
        this.challenge = challenge;
        this.queue = queue;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }
}