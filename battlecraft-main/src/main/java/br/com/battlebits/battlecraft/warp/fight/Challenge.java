package br.com.battlebits.battlecraft.warp.fight;

import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.battlecraft.world.map.OneVsOneMap;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Challenge {
    private Player player;
    private Player target;
    private ItemStack sword;
    private ArmorType armorType;
    private Map<Enchantment, Integer> armorEnchantments;
    private ChallengeType challengeType;
    private boolean refil;
    private boolean recraft;
    private boolean speed;
    private boolean strenght;
    private long expire;
    private OneVsOneMap map;

    public Challenge(Player player, Player target, OneVsOneMap map) {
        this(player, target, ItemBuilder.create(Material.DIAMOND_SWORD).enchantment(Enchantment.DAMAGE_ALL, 1).build(), ArmorType.IRON, new HashMap<>(), ChallengeType.NORMAL, false,  false, false, false, map);
        this.expire = System.currentTimeMillis() + 10000;
    }

    public Challenge(Player player, Player target, ItemStack sword, ArmorType armorType,
                     Map<Enchantment, Integer> armorEnchantments, ChallengeType challengeType,
                     boolean refil, boolean recraft, boolean speed, boolean strenght, OneVsOneMap map) {
        this.player = player;
        this.target = target;
        this.sword = sword;
        this.armorType = armorType;
        this.armorEnchantments = armorEnchantments;
        this.challengeType = challengeType;
        this.refil = refil;
        this.recraft = recraft;
        this.speed = speed;
        this.strenght = strenght;
        this.expire = System.currentTimeMillis() + 20000;
        this.map = map;
    }

    public boolean isExpired() {
        return expire < System.currentTimeMillis();
    }

    public void applyChallengeKit(Player player) {
        this.applySword(player);
        this.applyArmor(player);
        this.applySoup(player);
    }

    private void applySword(Player player) {
        player.getInventory().setItem(0, this.sword);
    }

    private void applyArmor(Player player) {
        if (this.armorType != ArmorType.NONE) {
            PlayerInventory inv = player.getInventory();
            inv.setHelmet(new ItemStack(Material.valueOf(this.armorType.name() + "_HELMET")));
            inv.setChestplate(new ItemStack(Material.valueOf(this.armorType.name() + "_CHESTPLATE")));
            inv.setLeggings(new ItemStack(Material.valueOf(this.armorType.name() + "_LEGGINGS")));
            inv.setBoots(new ItemStack(Material.valueOf(this.armorType.name() + "_BOOTS")));
        }
    }

    private void applySoup(Player player) {
        int j = 9;
        if (this.refil)
            j = 39;
        for (int i = 1; i < j; i++) {
            player.getInventory().addItem(new ItemStack(Material.MUSHROOM_SOUP));
        }
    }
}
