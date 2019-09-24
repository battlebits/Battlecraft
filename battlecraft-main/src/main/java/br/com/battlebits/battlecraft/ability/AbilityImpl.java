package br.com.battlebits.battlecraft.ability;

import br.com.battlebits.battlecraft.manager.CooldownManager;
import br.com.battlebits.battlecraft.manager.ProtectionManager;
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
    public boolean isProtected(Player player) {
        return ProtectionManager.isProtected(player);
    }

    @Override
    public boolean isUsing(Player player) {
        return false;
    }

    @Override
    public boolean hasCooldown(Player player) {
        return CooldownManager.hasCooldown(player, "ability." + name.toLowerCase());
    }

    @Override
    public long getCooldown(Player player) {
        return CooldownManager.getCooldown(player, "ability." + name.toLowerCase()).getTime();
    }

    @Override
    public void addCooldown(Player player, long time) {
        CooldownManager.addCooldown(CooldownManager.Cooldown.builder()
                .name("ability." + name.toLowerCase())
                .player(player).time(System.currentTimeMillis() + time)
                .build());
    }
}
