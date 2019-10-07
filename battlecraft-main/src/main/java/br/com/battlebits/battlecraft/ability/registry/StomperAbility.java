package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.Material;

public class StomperAbility extends Ability {

    public StomperAbility() {
        super("Stomper", ItemBuilder.create(Material.DIAMOND_BOOTS).build(), 0);
    }

}
