package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.ability.AbilityItem;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
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

    private static final String JUMP_COOLDOWN = ChatColor.translateAlternateColorCodes('&',"&6&LJUMP");
    private static final double MAX_FALL_DAMAGE = 7.0;
    private static final long COOLDOWN_TIME = 6;
    private static ItemStack ITEM_COOLDOWN = null;

    public static final float JUMP_MULTIPLY_NORMAL = 1F;
    public static final float JUMP_MULTIPLY_NERFED = 0.3F;
    public static final float JUMP_Y_SNEAKING = 0.55F;
    public static final float JUMP_Y_GROUND = 0.9F;
    public static final float JUMP_Y_AIR = 0.85F;

    private static final float FALL_DISTANCE = -1;

    private Set<Player> doubleJump;

    public KangarooAbility() {
        doubleJump = new HashSet<>();
        ITEM_COOLDOWN = ItemBuilder.create(Material.FIREWORK).name(
                ChatColor.GOLD + "Kangaroo Boost").build();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(event.getAction().name().contains("RIGHT")) {
            Player player = event.getPlayer();
            ItemStack item = event.getItem();
            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            boolean cancel = false;
            if (itemInMainHand.getType() == Material.AIR) {
                cancel = kangaroo(player, player.getInventory().getItemInOffHand());
            } else if (item != null && player.getInventory().getItemInMainHand().getType() == item.getType()) {
                cancel = kangaroo(player, itemInMainHand);
                if (!cancel && event.useItemInHand() != Event.Result.DENY) {
                    cancel = kangaroo(player, player.getInventory().getItemInOffHand());
                }
            } else if (item != null && item.getType() == player.getInventory().getItemInOffHand().getType() && item.getType() == Material.FIREWORK) {
                cancel = true;
            }
            event.setCancelled(cancel);
        }
    }

    @EventHandler
    public void onAnimation(PlayerAnimationEvent event) {
        if(event.getAnimationType() == PlayerAnimationType.ARM_SWING && event.getPlayer().getGameMode() == GameMode.ADVENTURE) {
            kangaroo(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
        }
    }

    private boolean kangaroo(Player player, ItemStack item) {
        if (item!= null && item.getType() == Material.FIREWORK && hasAbility(player)) {
            if (hasCooldown(player, JUMP_COOLDOWN)) {
                return true;
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
            player.setFallDistance(FALL_DISTANCE);
            return true;
        }
        return false;
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
                addItemCooldown(player, ITEM_COOLDOWN, JUMP_COOLDOWN, COOLDOWN_TIME);
            }
        }
    }

    @Override
    public List<ItemStack> getItems() {
        return Collections.singletonList(ITEM_COOLDOWN);
    }
}
