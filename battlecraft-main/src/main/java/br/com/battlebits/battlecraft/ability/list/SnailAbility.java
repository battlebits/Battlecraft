package br.com.battlebits.battlecraft.ability.list;

import br.com.battlebits.battlecraft.ability.AbilityImpl;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class SnailAbility extends AbilityImpl {

    private Random random;

    public SnailAbility() {
        super("Snail", ItemBuilder.create(Material.STRING).build(), 0);
        random = new Random();
    }

    @EventHandler
    public void onPlayerDamagePlayerListener(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player && isUsing(((Player) event
                .getDamager()).getPlayer()) && random.nextInt(2) == 0) {
            event.getEntity().getLocation().getWorld().playEffect(event.getEntity().getLocation().add(0.0D,
                    0.4D, 0.0D), Effect.STEP_SOUND, 159, (short) 13);
            ((Player) event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 0));
        }
    }

    @Override
    public void onReceiveItems(Player player) {
        //Null
    }
}
