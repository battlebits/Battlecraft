package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.ability.AbilityItem;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static br.com.battlebits.battlecraft.manager.ProtectionManager.isProtected;

public class KangarooAbility extends Ability implements AbilityItem {

    private static final String JUMP_COOLDOWN = "jump";
    private static final double MAX_FALL_DAMAGE = 7.0;
    private static final long COOLDOWN_TIME = 6000;

    public static final float JUMP_MULTIPLY_NORMAL = 1F;
    public static final float JUMP_MULTIPLY_NERFED = 0.3F;
    public static final float JUMP_Y_SNEAKING = 0.65F;
    public static final float JUMP_Y_GROUND = 0.9F;
    public static final float JUMP_Y_AIR = 0.85F;

    private Set<Player> doubleJump;

    public KangarooAbility() {
        doubleJump = new HashSet<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() == Material.FIREWORK_ROCKET && hasAbility(player)) {
            if (hasCooldown(player, JUMP_COOLDOWN)) {
                return;
            }
            if (player.isOnGround()) {
                Vector vector = player.getEyeLocation().getDirection();
                if (!player.isSneaking()) {
                    vector.multiply(JUMP_MULTIPLY_NERFED);
                    vector.setY(JUMP_Y_GROUND);
                } else {
                    vector.multiply(JUMP_MULTIPLY_NERFED);
                    vector.setY(JUMP_Y_SNEAKING);
                }
                player.setVelocity(vector);
                doubleJump.remove(player);
            } else {
                if (!doubleJump.contains(player)) {
                    Vector vector = player.getEyeLocation().getDirection();
                    if (!player.isSneaking()) {
                        vector.multiply(JUMP_MULTIPLY_NERFED);
                        vector.setY(JUMP_Y_AIR);
                        player.setVelocity(vector);
                        doubleJump.add(player);
                    } else {
                        vector.multiply(JUMP_MULTIPLY_NORMAL);
                        vector.setY(JUMP_Y_SNEAKING);
                        player.setVelocity(vector);
                        doubleJump.add(player);
                    }
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (this.doubleJump.contains(player) && player.isOnGround()) {
            this.doubleJump.remove(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && hasAbility(player)) {
                final double damage = event.getDamage();
                if (damage > MAX_FALL_DAMAGE) {
                    event.setDamage(MAX_FALL_DAMAGE);
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
            if (!isProtected(player)) {
                addCooldown(player, JUMP_COOLDOWN, COOLDOWN_TIME);
            }
        }
    }

    @Override
    public List<ItemStack> getItems() {
        return Collections.singletonList(ItemBuilder.create(Material.FIREWORK_ROCKET).name(
                ChatColor.GOLD + "Kangaroo Boost").build());
    }
}
