package br.com.battlebits.battlecraft.warp;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.ability.registry.KangarooAbility;
import br.com.battlebits.battlecraft.ability.registry.NinjaAbility;
import br.com.battlebits.battlecraft.event.RealMoveEvent;
import br.com.battlebits.battlecraft.event.protection.PlayerProtectionRemoveEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.inventory.WarpSelector;
import br.com.battlebits.battlecraft.manager.ProtectionManager;
import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.api.item.ActionItemStack;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Set;

import static br.com.battlebits.battlecraft.manager.AbilityManager.getAbilityByClass;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class WarpSpawn extends Warp {

    public WarpSpawn(Location spawnLocation, WorldMap map) {
        super("Spawn", Material.GRASS_BLOCK, spawnLocation, map);
        createKits();
    }

    @EventHandler
    public void onWarpJoin(PlayerWarpJoinEvent event) {
        Player p = event.getPlayer();
        if (!inWarp(p))
            return;
        Language l = Commons.getLanguage(p.getUniqueId());
        ItemBuilder builder =
                ItemBuilder.create(Material.COMPASS).name(tl(l, WARPS_ITEM_NAME)).lore("", tl(l,
                        WARPS_ITEM_LORE));
        ActionItemStack item = new ActionItemStack(builder.build(), (player, itemStack, action) -> {
            new WarpSelector(l, this).open(p);
            return false;
        });
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setItem(2, item.getItemStack());
        inv.setHeldItemSlot(1);
    }

    @EventHandler
    public void onRemoveProtection(PlayerProtectionRemoveEvent event) {
        Player p = event.getPlayer();
        if (!inWarp(p))
            return;
        Language lang = Commons.getLanguage(p.getUniqueId());
        p.sendMessage(tl(lang, PROTECTION_TAG) + tl(lang, PROTECTION_LOST));
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
        // TODO Remove kit
    }

    private void createKits() {
        int DEFAULT_PRICE = 2000;
        Set<Ability> abilities = Set.of(getAbilityByClass(NinjaAbility.class));
        ItemStack icon = new ItemStack(Material.NETHER_STAR);
        this.kits.add(new Kit("ninja", abilities, icon, DEFAULT_PRICE));

        abilities = Set.of(getAbilityByClass(KangarooAbility.class));
        icon = new ItemStack(Material.FIREWORK_ROCKET);
        this.kits.add(new Kit("kangaroo", abilities, icon, DEFAULT_PRICE));
    }
}
