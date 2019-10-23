package br.com.battlebits.battlecraft.ability;

import br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.valueOf;

@AllArgsConstructor
@Getter
public class Kit {

    private String id;
    private Set<Ability> abilities;
    private ItemStack icon;
    private int price;

    public BattlecraftTranslateTag getNameTag() {
        return valueOf("KIT_" + getId().toUpperCase() + "_NAME");
    }

    public BattlecraftTranslateTag getDescriptionTag() {
        return valueOf("KIT_" + getId().toUpperCase() + "_DESCRIPTION");
    }
}
