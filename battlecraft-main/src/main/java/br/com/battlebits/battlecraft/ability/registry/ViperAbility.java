package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class ViperAbility extends Ability {

    private Random random;

    public ViperAbility() {
        super("Viper", ItemBuilder.create(Material.SPIDER_EYE).build(), 0);
        random = new Random();
    }

    @EventHandler
    public void onPlayerDamagePlayerListener(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player && hasAbility(((Player) event
                .getDamager()).getPlayer()) && random.nextInt(2) == 0) {
            event.getEntity().getLocation().getWorld().playEffect(event.getEntity().getLocation().add(0.0D,
                    0.4D, 0.0D), Effect.STEP_SOUND, 159, (short) 13);
            ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 0));
        }
    }

}
