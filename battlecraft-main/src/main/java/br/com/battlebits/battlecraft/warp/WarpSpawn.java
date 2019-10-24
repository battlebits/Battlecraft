package br.com.battlebits.battlecraft.warp;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.ability.registry.KangarooAbility;
import br.com.battlebits.battlecraft.ability.registry.NinjaAbility;
import br.com.battlebits.battlecraft.event.PlayerKitEvent;
import br.com.battlebits.battlecraft.event.RealMoveEvent;
import br.com.battlebits.battlecraft.event.protection.PlayerProtectionRemoveEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpDeathEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.inventory.KitSelector;
import br.com.battlebits.battlecraft.inventory.WarpSelector;
import br.com.battlebits.battlecraft.manager.KitManager;
import br.com.battlebits.battlecraft.manager.ProtectionManager;
import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.api.item.ActionItemStack;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.battlebits.battlecraft.manager.AbilityManager.getAbilityByClass;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class WarpSpawn extends Warp {

    private Kit defaultKit;

    public WarpSpawn(Location spawnLocation, WorldMap map) {
        super("Spawn", Material.GRASS_BLOCK, spawnLocation, map);
        createKits();
    }

    @EventHandler
    public void onWarpJoin(PlayerWarpJoinEvent event) {
        Player p = event.getPlayer();
        if (!inWarp(p))
            return;
        PlayerInventory inv = p.getInventory();
        inv.clear();
        Language l = Commons.getLanguage(p.getUniqueId());
        ItemBuilder builder =
                ItemBuilder.create(Material.ENDER_CHEST).name(tl(l, KITSELECTOR_ITEM_NAME)).lore("", tl(l,
                        KITSELECTOR_ITEM_LORE));
        ActionItemStack  item = new ActionItemStack(builder.build(), (player, itemStack, action) -> {
            new KitSelector(l, this).open(p);
            return false;
        });
        inv.setItem(1, item.getItemStack());
        builder =
                ItemBuilder.create(Material.COMPASS).name(tl(l, WARPSELECTOR_ITEM_NAME)).lore("", tl(l,
                        WARPSELECTOR_ITEM_LORE));
        item = new ActionItemStack(builder.build(), (player, itemStack, action) -> {
            new WarpSelector(l, this).open(p);
            return false;
        });
        inv.setItem(2, item.getItemStack());
        inv.setHeldItemSlot(1);
        ProtectionManager.addProtection(p);
    }

    @EventHandler
    public void onKit(PlayerKitEvent event) {
        if(!isWarpKit(event.getKit()))
            return;
        PlayerInventory inv = event.getPlayer().getInventory();
        inv.setHelmet(new ItemStack(Material.IRON_HELMET));
        inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        inv.setBoots(new ItemStack(Material.IRON_BOOTS));
        ItemStack diamond = new ItemStack(Material.DIAMOND_SWORD);
        diamond.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        inv.setItem(0, diamond);
        // TODO Criar tipos de kits para espadas diferentes
    }

    @EventHandler
    public void onDeathWarp(PlayerWarpDeathEvent event) {
        if (inWarp(event.getPlayer()))
            KitManager.removeKit(event.getPlayer());
    }

    @EventHandler
    public void onRemoveProtection(PlayerProtectionRemoveEvent event) {
        Player p = event.getPlayer();
        if (!inWarp(p))
            return;
        Language lang = Commons.getLanguage(p.getUniqueId());
        p.sendMessage(tl(lang, PROTECTION_TAG) + tl(lang, PROTECTION_LOST));
        if (!KitManager.containsKit(p)) {
            KitManager.giveKit(p, defaultKit);
        }
    }

    @EventHandler
    public void onMove(RealMoveEvent event) {
        Player p = event.getPlayer();
        if (!inWarp(p))
            return;
        if (!ProtectionManager.isProtected(p))
            return;
        Location to = event.getTo();
        Block above = to.clone().subtract(0, 0.1, 0).getBlock();
        if (above.getType() == Material.GRASS_BLOCK) {
            ProtectionManager.removeProtection(p);
        }
    }

    @EventHandler
    public void onWarpLeave(PlayerWarpQuitEvent event) {
        Player p = event.getPlayer();
        if (!inWarp(p))
            return;
        KitManager.removeKit(event.getPlayer());
    }

    private void createKits() {
        int DEFAULT_PRICE = 2000;
        ItemStack icon = new ItemStack(Material.DIAMOND_SWORD);
        defaultKit = new Kit("pvp", new HashSet<>(), icon, DEFAULT_PRICE);
        this.kits.add(defaultKit);

        Stream<Ability> abilities = Stream.of(getAbilityByClass(NinjaAbility.class));
        icon = new ItemStack(Material.NETHER_STAR);
        this.kits.add(new Kit("ninja", abilities.collect(Collectors.toSet()), icon, DEFAULT_PRICE));

        abilities = Stream.of(getAbilityByClass(KangarooAbility.class));
        icon = new ItemStack(Material.FIREWORK_ROCKET);
        this.kits.add(new Kit("kangaroo", abilities.collect(Collectors.toSet()), icon, DEFAULT_PRICE));
    }
}
