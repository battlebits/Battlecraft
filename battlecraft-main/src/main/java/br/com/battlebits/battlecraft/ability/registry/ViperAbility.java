package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.event.PlayerDamagePlayerEvent;
import org.bukkit.Effect;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static br.com.battlebits.battlecraft.manager.ProtectionManager.isProtected;

public class ViperAbility extends Ability {

    private static final PotionEffectType EFFECT_TYPE = PotionEffectType.POISON;
    private static final int EFFECT_DURATION = 100;
    private static final int EFFECT_AMPLIFIER = 0;

    private static final double EFFECT_CHANCE = 0.33;

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerDamagePlayerListener(PlayerDamagePlayerEvent event) {
        if (Math.random() <= EFFECT_CHANCE && hasAbility(event.getPlayer())
                && !isProtected(event.getPlayer()) && !isProtected(event.getDamaged())) {

            event.getDamaged().addPotionEffect(new PotionEffect(EFFECT_TYPE, EFFECT_DURATION, EFFECT_AMPLIFIER));

            event.getDamaged().getLocation().getWorld().playEffect(event.getDamaged().getLocation()
                    .add(0.0D, 0.4D, 0.0D), Effect.STEP_SOUND, 159, (short) 13);
        }
    }

}
