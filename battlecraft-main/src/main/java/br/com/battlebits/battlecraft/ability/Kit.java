package br.com.battlebits.battlecraft.ability;

import br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.valueOf;

@Getter
public class Kit {

    private String name;
    private Set<Ability> abilities;
    private ItemStack icon;
    private int price;
    private List<ItemStack> items;

    public Kit(String name, Set<Ability> abilities, ItemStack icon, int price) {
        this.name = name;
        this.abilities = abilities;
        this.icon = icon;
        this.price = price;
        this.items = new ArrayList<>();
        abilities.stream().filter(ability -> ability instanceof AbilityItem).forEach(ability -> this.items.addAll(((AbilityItem)ability).getItems()));
    }

    public BattlecraftTranslateTag getDescriptionTag() {
        return valueOf("KIT_" + getName().toUpperCase() + "_DESCRIPTION");
    }
}
