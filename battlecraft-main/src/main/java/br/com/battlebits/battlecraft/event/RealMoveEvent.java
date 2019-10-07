package br.com.battlebits.battlecraft.event;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class RealMoveEvent extends PlayerEvent {
	@Getter
	public static HandlerList handlersList = new HandlerList();
	private Location from;
	private Location to;

	public RealMoveEvent(Player player, Location from, Location to) {
		super(player);
		this.from = from;
		this.to = to;
	}



	public HandlerList getHandlers() {
		return handlersList;
	}


}