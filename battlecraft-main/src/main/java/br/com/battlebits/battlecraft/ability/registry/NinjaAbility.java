package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.Map;

import static br.com.battlebits.battlecraft.manager.ProtectionManager.isProtected;

public class NinjaAbility extends Ability {

    private Map<Player, Player> ability;

    public NinjaAbility() {
        super("Ninja", ItemBuilder.create(Material.EMERALD).build(), 0);
        ability = new HashMap<>();
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            final Player player = (Player) event.getEntity();
            final Player damager = (Player) event.getDamager();
            if (player.hasMetadata("customDamage") || damager.hasMetadata("customDamage")) {
                return;
            }
            //Check protect status
            if (hasAbility(damager)) {
                this.ability.put(damager, player);
            }
        }
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        this.ability.remove(event.getEntity());
        Player killer = event.getEntity().getKiller();
        if (killer != null) {
            this.ability.remove(killer);
        }
    }


    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (!event.isSneaking() && this.ability.containsKey(player) && !isProtected(player) && hasAbility(player)) {
            final Player target = this.ability.get(player);
            //Check protect status
            if (target.isOnline() && !isProtected(target)) {
                if (player.getLocation().distance(target.getLocation()) > 40) {
                    player.sendMessage("§cO jogador está muito distante!");
                    return;
                }
                //Check gladiator
//                TODO Cooldown
//                if(!hasCooldown(player)) {
//                    player.teleport(target.getLocation());
//                    player.getWorld().playSound(player.getLocation(), Sound
//                    .ENTITY_ENDER_DRAGON_FLAP, 1.0F, 1.0F);
//                    ability.remove(player);
//                    addCooldown(player, 4000);
//                } else {
//                    //Send cooldown message
//                }
            }
        }
    }

}
