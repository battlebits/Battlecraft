package br.com.battlebits.battlecraft.warp.fight;

import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public Challenge(Player player, Player target) {
        this(player, target, ItemBuilder.create(Material.DIAMOND_SWORD).enchantment(Enchantment.DAMAGE_ALL, 1).build(), ArmorType.IRON, new HashMap<>(), ChallengeType.NORMAL, false,  false, false, false);
        this.expire = System.currentTimeMillis() + 10000;
    }

    public Challenge(Player player, Player target, ItemStack sword, ArmorType armorType,
                     Map<Enchantment, Integer> armorEnchantments, ChallengeType challengeType,
                     boolean refil, boolean recraft, boolean speed, boolean strenght) {
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
    }

    public boolean isExpired() {
        return expire < System.currentTimeMillis();
    }
}
