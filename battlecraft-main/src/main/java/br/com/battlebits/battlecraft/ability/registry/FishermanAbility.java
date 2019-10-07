package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.AbilityImpl;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;

public class FishermanAbility extends AbilityImpl {

    public FishermanAbility() {
        super("Fisherman", ItemBuilder.create(Material.FISHING_ROD).build(), 0);
    }

    @EventHandler
    public void onPlayerFishListener(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            final Player player = event.getPlayer();
            if (isUsing(player) && event.getCaught() instanceof Player && !isProtected(player)) {
                Player c = (Player) event.getCaught();
                if (!isProtected(c)) {
                    c.sendMessage("§eUm fisherman te puxou!");
                    c.teleport(player.getLocation());
                }
            }
        }
    }

    @Override
    public void onReceiveItems(Player player) {
        player.getInventory().addItem(ItemBuilder.create(Material.FISHING_ROD).flag(ItemFlag.HIDE_UNBREAKABLE).
                unbreakable(true).name("§aFisherman").build());
    }
}
