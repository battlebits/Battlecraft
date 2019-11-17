package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpDeathEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.translate.Language;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.Map;

import static br.com.battlebits.battlecraft.manager.ProtectionManager.isProtected;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.KIT_NINJA_DISTANT;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.KIT_NINJA_TAG;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class NinjaAbility extends Ability {

    private static final int MAX_DISTANCE = 40;

    private static final String COOLDOWN_KEY = ChatColor.translateAlternateColorCodes('&',"&B&LNINJA");
    private static final long COOLDOWN_DURATION = 4;

    private Map<Player, NinjaHit> ability;

    public NinjaAbility() {
        this.ability = new HashMap<>();
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDamagePlayerEvent(PlayerDamagePlayerEvent event) {
        if (event.getDamaged().hasMetadata("customDamage") || event.getPlayer().hasMetadata("customDamage")) {
            return;
        }
        if (!isProtected(event.getPlayer()) && !isProtected(event.getDamaged()) && hasAbility(event.getPlayer())) {
            NinjaHit ninjaHit = ability.get(event.getPlayer());
            if (ninjaHit == null) {
                ninjaHit = new NinjaHit(event.getDamaged());
                ability.put(event.getPlayer(), ninjaHit);
            } else {
                ninjaHit.setTarget(event.getDamaged());
            }
        }
    }

    @EventHandler
    public void onPlayerWarpDeathEvent(PlayerWarpDeathEvent event) {
        this.ability.remove(event.getPlayer());
        if (event.hasKiller()) {
            this.ability.remove(event.getKiller());
        }
    }

    @EventHandler
    public void onPlayerWarpQuitEvent(PlayerWarpQuitEvent event) {
        ability.remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerToggleSneakEvent(PlayerToggleSneakEvent event) {
        final Player player = event.getPlayer();
        if (event.isSneaking() && this.ability.containsKey(player) && !isProtected(player) && hasAbility(player)) {
            NinjaHit hit = this.ability.get(player);
            if (hit == null)
                return;
            if (hit.getTargetExpires() < System.currentTimeMillis())
                return;
            //Check protect status
            Player target = hit.getTarget();
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

                    player.getWorld().playSound(player.getLocation().add(0.0, 0.5, 0.0), Sound
                            .ENTITY_ENDERDRAGON_FLAP, 0.3F, 1.0F);
                }
            }
        }
    }

    @Getter
    private static class NinjaHit {

        private Player target;
        private long targetExpires;

        public NinjaHit(Player target) {
            this.target = target;
            this.targetExpires = System.currentTimeMillis() + 15000;
        }

        public void setTarget(Player player) {
            this.target = player;
            this.targetExpires = System.currentTimeMillis() + 15000;
        }

    }

}
