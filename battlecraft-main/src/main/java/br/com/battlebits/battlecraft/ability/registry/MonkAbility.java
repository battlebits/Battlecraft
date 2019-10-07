package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class MonkAbility extends Ability {

    public MonkAbility() {
        super("Monk", ItemBuilder.create(Material.BLAZE_ROD).build(), 0);
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        if (event.getRightClicked() instanceof Player && player.getInventory().getItemInMainHand().getType() == Material
                .BLAZE_ROD && isUsing(player)) {
            if (hasCooldown(player)) {
                //Send cooldown message
                return;
            }
            Player target = (Player) event.getRightClicked();
            PlayerInventory inv = target.getInventory();
            int slot = new Random().nextInt(35);
            ItemStack itemInHand = inv.getItemInMainHand();
            ItemStack randomItem = inv.getItem(slot);
            if (randomItem == null) {
                randomItem = new ItemStack(Material.AIR);
            }
            inv.setItemInMainHand(randomItem);
            inv.setItem(slot, itemInHand);
            addCooldown(player, 7000);
            player.sendMessage("§aVocê monkou o jogador!");
            target.sendMessage("§aVocê foi monkado!");
            target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
        }
    }

    @Override
    public void onReceiveItems(Player player) {
        player.getInventory().addItem(ItemBuilder.create(Material.BLAZE_ROD).name("§aMonk").build());
    }
}
