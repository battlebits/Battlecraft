package br.com.battlebits.battlecraft.listener;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class DamageFixListener implements Listener {

    private final static double ENCHANTMENT_MULTIPLIER = 1d;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamageEvent(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        Player p = (Player) event.getDamager();
        ItemStack sword = p.getInventory().getItemInMainHand();
        double damage = event.getDamage();
        double danoEspada = getDamage(sword.getType());
        if (p.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            for (PotionEffect effect : p.getActivePotionEffects()) {
                if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
                    double minus;
                    if (isCrital(p)) {
                        minus = (danoEspada + (danoEspada / 2)) * 1.3 * (effect.getAmplifier() + 1);
                    } else {
                        minus = danoEspada * 1.3 * (effect.getAmplifier() + 1);
                    }
                    damage = damage - minus;
                    damage += 2 * (effect.getAmplifier() + 1);
                    break;
                }
            }
        }
        if (!sword.getEnchantments().isEmpty()) {
            if (sword.containsEnchantment(Enchantment.DAMAGE_ARTHROPODS) && isArthropod(event.getEntityType())) {
                damage = damage - (1.5 * sword.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS));
                damage += ENCHANTMENT_MULTIPLIER * sword.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS);
            }
            if (sword.containsEnchantment(Enchantment.DAMAGE_UNDEAD) && isUndead(event.getEntityType())) {
                damage = damage - (1.5 * sword.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD));
                damage += ENCHANTMENT_MULTIPLIER * sword.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD);
            }
            if (sword.containsEnchantment(Enchantment.DAMAGE_ALL)) {
                damage = damage - 1.25 * sword.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
                damage += ENCHANTMENT_MULTIPLIER * sword.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
            }
        }
        boolean critical = isCrital(p);
        if (critical) {
            // Redução do dano de critico
            damage = damage - (danoEspada / 2);
        }
        if (event.getDamage(DamageModifier.BLOCKING) < 0) {
            // Calcula o dano pré critico
            event.setDamage(DamageModifier.BLOCKING, -(damage * 0.25));
        }
        if (critical) {
            // Aumenta o dano pós redução do escudo
            damage += (danoEspada / 3);
        }
        event.setDamage(DamageModifier.BASE, damage);
    }

    private boolean isCrital(Player p) {
        return p.getFallDistance() > 0 && !p.isOnGround() && !p.hasPotionEffect(PotionEffectType.BLINDNESS);
    }

    private boolean isArthropod(EntityType type) {
        switch (type) {
            case CAVE_SPIDER:
            case SPIDER:
            case SILVERFISH:
                return true;
            default:
                return false;
        }
    }

    private boolean isUndead(EntityType type) {
        switch (type) {
            case SKELETON:
            case ZOMBIE:
            case WITHER_SKULL:
            case PIG_ZOMBIE:
            default:
                return false;
        }
    }

    private double getDamage(Material type) {
        double damage = 1.0;
        if (type.toString().contains("DIAMOND_")) {
            damage = 8.0;
        } else if (type.toString().contains("IRON_")) {
            damage = 7.0;
        } else if (type.toString().contains("STONE_")) {
            damage = 6.0;
        } else if (type.toString().contains("WOOD_")) {
            damage = 5.0;
        } else if (type.toString().contains("GOLD_")) {
            damage = 5.0;
        }
        if (!type.toString().contains("_SWORD")) {
            damage--;
            if (!type.toString().contains("_AXE")) {
                damage--;
                if (!type.toString().contains("_PICKAXE")) {
                    damage--;
                    if (!type.toString().contains("_SPADE")) {
                        damage = 1.0;
                    }
                }
            }
        }
        return damage;
    }

    @EventHandler
    public void onKnockback(EntityKnockbackByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (!(event.getHitBy() instanceof Player))
            return;
        Vector v = event.getAcceleration();
        Player damager = (Player) event.getHitBy();
        Player damaged = (Player) event.getEntity();
        if(!damaged.isOnGround()) {
            v.setY(0.4D);
            double y = damaged.getVelocity().getY();
            double total = y + v.getY();
            if (total > 0.4000000059604645D)
                v.setY(0.4000000059604645D - y);
        }
    }
}
