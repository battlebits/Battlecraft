package br.com.battlebits.battlecraft.ability;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public abstract class AbilityImpl implements Ability {

    private String name;
    private ItemStack icon;
    private int price;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ItemStack getIcon() {
        return icon;
    }

    @Override
    public String getLorePrefix() {
        return "inventory.ability.lore." + name.toLowerCase();
    }

    @Override
    public String getPermission() {
        return "battlecraft.ability." + name.toLowerCase();
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public boolean isUsing(Player player) {
        return false;
    }

    @Override
    public boolean hasCooldown(Player player) {
        return false;
    }

    @Override
    public long getCooldown(Player player) {
        return 0;
    }
}
