package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.AbilityImpl;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class StomperAbility extends AbilityImpl {

    public StomperAbility() {
        super("Stomper", ItemBuilder.create(Material.DIAMOND_BOOTS).build(), 0);
    }

    @Override
    public void onReceiveItems(Player player) {
        //Null
    }
}
