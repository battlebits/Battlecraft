package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.ability.AbilityItem;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;

import static br.com.battlebits.battlecraft.manager.ProtectionManager.isProtected;

public class FishermanAbility extends Ability implements AbilityItem {

    @EventHandler
    public void onPlayerFishListener(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            final Player player = event.getPlayer();
            if (hasAbility(player) && event.getCaught() instanceof Player && !isProtected(player)) {
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
