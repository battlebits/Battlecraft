package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpDeathEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.translate.Language;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import static br.com.battlebits.battlecraft.manager.ProtectionManager.isProtected;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class NinjaAbility extends Ability {

    private static final int MAX_DISTANCE = 40;

    private static final String COOLDOWN_KEY = ChatColor.translateAlternateColorCodes('&',"&B&LNINJA");
    private static final long COOLDOWN_DURATION = 4;

    private BiMap<Player, Player> ability;

    public NinjaAbility() {
        this.ability = HashBiMap.create();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDamagePlayerEvent(PlayerDamagePlayerEvent event) {
        if (event.getDamaged().hasMetadata("customDamage") || event.getPlayer().hasMetadata("customDamage")) {
            return;
        }
        if (!isProtected(event.getPlayer()) && !isProtected(event.getDamaged()) && hasAbility(event.getPlayer())) {
            this.ability.put(event.getPlayer(), event.getDamaged());
        }
    }

    @EventHandler
    public void onPlayerWarpDeathEvent(PlayerWarpDeathEvent event) {
        this.ability.remove(event.getPlayer());

        this.ability.inverse().remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerWarpQuitEvent(PlayerWarpQuitEvent event) {
        this.ability.remove(event.getPlayer());

        this.ability.inverse().remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (!event.isSneaking() && this.ability.containsKey(player) && !isProtected(player) && hasAbility(player)) {
            final Player target = this.ability.get(player);
            //Check protect status
            if (target.isOnline() && !isProtected(target)) {
                if (player.getLocation().distance(target.getLocation()) > MAX_DISTANCE) {
                    Language language = Commons.getLanguage(player.getUniqueId());
                    player.sendMessage(tl(language, KIT_NINJA_TAG) + tl(language, KIT_NINJA_DISTANT));
                    return;
                }
                //Check gladiator
                if (!hasCooldown(player, COOLDOWN_KEY)) {
                    player.teleport(target.getLocation());

                    addCooldown(player, COOLDOWN_KEY, COOLDOWN_DURATION);
                    ability.remove(player);

                    player.getWorld().playSound(player.getLocation().add(0.0, 0.5, 0.0), Sound
                            .ENTITY_ENDERDRAGON_FLAP, 0.3F, 1.0F);
                }
            }
        }
    }

}
