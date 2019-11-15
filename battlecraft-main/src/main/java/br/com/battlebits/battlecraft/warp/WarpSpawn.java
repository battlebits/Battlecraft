package br.com.battlebits.battlecraft.warp;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.ability.registry.*;
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
import br.com.battlebits.battlecraft.status.StatusAccount;
import br.com.battlebits.battlecraft.status.warpstatus.StatusSpawn;
import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.api.item.ActionItemStack;
import br.com.battlebits.commons.bukkit.api.item.ActionItemStack.InteractHandler;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.battlebits.battlecraft.manager.AbilityManager.getAbilityByClass;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class WarpSpawn extends Warp {

    private static final double SPAWN_RADIUS = 15;
    private static final double SPAWN_RADIUS_SQUARED = SPAWN_RADIUS * SPAWN_RADIUS;

    private Kit defaultKit;

    private InteractHandler kitSelectorHandler = (player, player1, itemStack, itemAction) -> {
        if (itemAction.name().contains("RIGHT"))
            new KitSelector(Commons.getLanguage(player.getUniqueId()), this).open(player);
        return false;
    };

    private InteractHandler warpSelectorHandler = (player, player1, itemStack, itemAction) -> {
        if (itemAction.name().contains("RIGHT"))
            new WarpSelector(Commons.getLanguage(player.getUniqueId()), this).open(player);
        return false;
    };

    public WarpSpawn(WorldMap map) {
        super("Spawn", Material.GRASS, map);
        createKits();
        ActionItemStack.register(kitSelectorHandler);
        ActionItemStack.register(warpSelectorHandler);
    }

    @EventHandler
    public void onWarpJoin(PlayerWarpJoinEvent event) {
        Player p = event.getPlayer();
        if (!inWarp(p))
            return;
        PlayerInventory inv = p.getInventory();
        Language l = Commons.getLanguage(p.getUniqueId());
        ItemBuilder builder =
                ItemBuilder.create(Material.ENDER_CHEST).name(tl(l, KITSELECTOR_ITEM_NAME)).lore("", tl(l,
                        KITSELECTOR_ITEM_LORE)).interact(kitSelectorHandler);
        inv.setItem(1, builder.build());
        builder =
                ItemBuilder.create(Material.COMPASS).name(tl(l, WARPSELECTOR_ITEM_NAME)).lore("", tl(l,
                        WARPSELECTOR_ITEM_LORE)).interact(warpSelectorHandler);
        inv.setItem(2, builder.build());
        inv.setHeldItemSlot(1);
        ProtectionManager.addProtection(p);

        StatusAccount status = Battlecraft.getInstance().getStatusManager().get(p.getUniqueId());
        if (!status.containsWarpStatus(this)) {
            status.putWarpStatus(this, new StatusSpawn(this, 0, 0, 0, 0));
        }
    }

    /**
     * Insta kill when player in Spawn takes void damage
     */
    @EventHandler
    public void onVoidDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID)
            return;
        Player player = (Player) event.getEntity();
        if (!inWarp(player))
            return;
        event.setDamage(Double.MAX_VALUE);
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
        double distX = to.getX() - getSpawnLocation().getX();
        double distZ = to.getZ() - getSpawnLocation().getZ();

        double distance = (distX * distX) + (distZ * distZ);
        if (above.getType() == Material.GRASS || distance > SPAWN_RADIUS_SQUARED) {
            ProtectionManager.removeProtection(p);
        }
    }

    @EventHandler
    public void onPlayerKill(PlayerWarpDeathEvent event) {
        Player killed = event.getPlayer();
        if (!inWarp(killed))
            return;
        getPlayerStatus(killed).addDeath();
        if(event.hasKiller()) {
            if (!inWarp(event.getKiller()))
                return;
            getPlayerStatus(event.getKiller()).addKill();
        }
    }

    private StatusSpawn getPlayerStatus(Player player) {
        return (StatusSpawn) Battlecraft.getInstance().getStatusManager().get(player.getUniqueId()).getWarpStatus(this);
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
        icon = new ItemStack(Material.FIREWORK);
        this.kits.add(new Kit("kangaroo", abilities.collect(Collectors.toSet()), icon, DEFAULT_PRICE));

        abilities = Stream.of(getAbilityByClass(StomperAbility.class));
        icon = new ItemStack(Material.IRON_BOOTS);
        this.kits.add(new Kit("stomper", abilities.collect(Collectors.toSet()), icon, DEFAULT_PRICE));

        abilities = Stream.of(getAbilityByClass(MagmaAbility.class));
        icon = new ItemStack(Material.LAVA_BUCKET);
        this.kits.add(new Kit("magma", abilities.collect(Collectors.toSet()), icon, DEFAULT_PRICE));

        abilities = Stream.of(getAbilityByClass(SnailAbility.class));
        icon = new ItemStack(Material.WEB);
        this.kits.add(new Kit("snail", abilities.collect(Collectors.toSet()), icon, DEFAULT_PRICE));

        abilities = Stream.of(getAbilityByClass(ViperAbility.class));
        icon = new ItemStack(Material.FERMENTED_SPIDER_EYE);
        this.kits.add(new Kit("viper", abilities.collect(Collectors.toSet()), icon, DEFAULT_PRICE));

        abilities = Stream.of(getAbilityByClass(FishermanAbility.class));
        icon = new ItemStack(Material.FISHING_ROD);
        this.kits.add(new Kit("fisherman", abilities.collect(Collectors.toSet()), icon, DEFAULT_PRICE));

//        abilities = Stream.of(getAbilityByClass(AnchorAbility.class));
//        icon = new ItemStack(Material.ANVIL);
//        this.kits.add(new Kit("anchor", abilities.collect(Collectors.toSet()), icon, DEFAULT_PRICE));
    }
}
