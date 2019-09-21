package br.com.battlebits.battlecraft.ability.list;

import br.com.battlebits.battlecraft.ability.AbilityImpl;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class KangarooAbility extends AbilityImpl {

    private Set<Player> ability;

    public KangarooAbility() {
        super("Kangaroo", ItemBuilder.create(Material.FIREWORK_ROCKET).build(), 0);

        ability = new HashSet<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.FIREWORK_ROCKET && player.getGameMode() ==
                GameMode.SURVIVAL && isUsing(player)) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                event.setCancelled(true);
            }
            if (hasCooldown(player)) {
                //Send cooldown message
                player.setVelocity(new Vector(0.0D, -1.0D, 0.0D));
                return;
            }
            //Check cooldown
            if (!this.ability.contains(player)) {
                Vector v;
                if (player.isSneaking()) {
                    v = player.getEyeLocation().getDirection().multiply(1.75f).setY(0.4f);
                } else {
                    v = player.getEyeLocation().getDirection().multiply(0.2f).setY(0.8f);
                }
                player.setVelocity(v);
                this.ability.add(player);
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (this.ability.contains(player)) {
            Block b = player.getLocation().getBlock();
            if (b.getType() != Material.AIR || b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
                this.ability.remove(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && isUsing(player)) {
                final double damage = event.getDamage();
                if (damage > 4.0D) {
                    event.setCancelled(true);
                    event.setDamage(4.0D);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player player = (Player) event.getEntity();
            //Check if using and protect status
            //Add cooldown
        }
    }

    @Override
    public void onReceiveItems(Player player) {
        player.getInventory().addItem(ItemBuilder.create(Material.FIREWORK_ROCKET).name("§aKangaroo").build());
    }
}
