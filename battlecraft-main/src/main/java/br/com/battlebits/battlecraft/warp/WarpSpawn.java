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
import br.com.battlebits.battlecraft.status.warpstatus.StatusMain;
import br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag;
import br.com.battlebits.battlecraft.util.NameUtils;
import br.com.battlebits.battlecraft.warp.scoreboard.MainBoard;
import br.com.battlebits.battlecraft.warp.scoreboard.WarpScoreboard;
import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.CommonsConst;
import br.com.battlebits.commons.account.BattleAccount;
import br.com.battlebits.commons.bukkit.api.admin.AdminMode;
import br.com.battlebits.commons.bukkit.api.item.ActionItemStack;
import br.com.battlebits.commons.bukkit.api.item.ActionItemStack.InteractHandler;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.bukkit.api.player.PingAPI;
import br.com.battlebits.commons.bukkit.api.tablist.TabListAPI;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static br.com.battlebits.battlecraft.manager.AbilityManager.getAbilityByClass;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class WarpSpawn extends Warp {

    private static final double SPAWN_RADIUS = 15;
    private static final double SPAWN_RADIUS_SQUARED = SPAWN_RADIUS * SPAWN_RADIUS;

    private Kit defaultKit;
    private Map.Entry<String, Integer> topKillstreak;
    private MainBoard scoreboard;

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
        topKillstreak = null;
        this.scoreboard = new MainBoard(getName());
    }

    @EventHandler
    public void onWarpJoin(PlayerWarpJoinEvent event) {
        Player p = event.getPlayer();
        if (!inWarp(p))
            return;
        PlayerInventory inv = p.getInventory();
        Language l = Commons.getLanguage(p.getUniqueId());
        ItemBuilder builder =
                ItemBuilder.create(Material.ENDER_CHEST).name(tl(l, KITSELECTOR_ITEM_NAME)).lore(
                        "", tl(l,
                                KITSELECTOR_ITEM_LORE)).interact(kitSelectorHandler);
        inv.setItem(1, builder.build());
        builder =
                ItemBuilder.create(Material.COMPASS).name(tl(l, WARPSELECTOR_ITEM_NAME)).lore("",
                        tl(l,
                                WARPSELECTOR_ITEM_LORE)).interact(warpSelectorHandler);
        inv.setItem(2, builder.build());
        inv.setHeldItemSlot(1);
        ProtectionManager.addProtection(p);

        StatusAccount status = Battlecraft.getInstance().getStatusManager().get(p.getUniqueId());
        if (!status.containsWarpStatus(this)) {
            status.putWarpStatus(this, new StatusMain(0, 0, 0, 0));
        }
    }

    private int tickCount = 0;

    @EventHandler
    public void onTick(UpdateEvent event) {
        if(event.getType() == UpdateEvent.UpdateType.TICK &&  tickCount++ % 7 == 0) {
            getScoreboard().updateTitleText();
            for (Player player : getPlayers()) {
                getScoreboard().updateTitle(player);
            }
        }
        if (event.getType() != UpdateEvent.UpdateType.SECOND)
            return;
        for (Player player : getPlayers()) {
            applyTabList(player);
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
        if (!isWarpKit(event.getKit()))
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
        applyTabList(event.getPlayer());
        getScoreboard().updateKit(event.getPlayer(), event.getKit());
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
        updateTopKillstreak();
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
        updatePlayerStatus(killed, StatusMain::addDeath);
        applyTabList(killed);
        getScoreboard().updateDeaths(event.getPlayer());
        if (event.hasKiller()) {
            if (!inWarp(event.getKiller()))
                return;
            updatePlayerStatus(event.getKiller(), StatusMain::addKill);
            applyTabList(event.getKiller());
            getScoreboard().updateKills(event.getKiller());
        }
        updateTopKillstreak();
    }


    private void updatePlayerStatus(Player player, Consumer<StatusMain> consumer) {
        Battlecraft.getInstance().getStatusManager().get(player.getUniqueId()).save(statusAccount -> consumer.accept((StatusMain) statusAccount.getWarpStatus(this)));
    }

    private StatusMain getPlayerStatus(Player player) {
        return (StatusMain) Battlecraft.getInstance().getStatusManager().get(player.getUniqueId()).getWarpStatus(this);
    }

    @EventHandler
    public void onWarpLeave(PlayerWarpQuitEvent event) {
        Player p = event.getPlayer();
        if (!inWarp(p))
            return;
        getPlayerStatus(p).resetKillstreak(); // O Killstreak só é valido enquanto o jogador
        // permanecer vivo e online
        KitManager.removeKit(event.getPlayer());
    }

    @Override
    protected void applyTabList(Player player) {
        int ping = PingAPI.getPing(player);
        int players = Bukkit.getOnlinePlayers().size() - AdminMode.playersInAdmin();
        StatusMain status = getPlayerStatus(player);
        BattleAccount account = Commons.getAccount(player.getUniqueId());
        String kitName = account.getLanguage().tl(KIT_NONE);
        if (KitManager.containsKit(player))
            kitName = NameUtils.formatString(KitManager.getCurrentPlayerKit(player).getName());
        String header =
                account.getLanguage().tl(BattlecraftTranslateTag.WARP_SPAWN_TABLIST_HEADER,
                        status.getKills(), status.getDeaths(), status.getKillstreak(), kitName,
                        NameUtils.formatString(getName()), ping, players, Bukkit.getMaxPlayers());
        String footer =
                account.getLanguage().tl(BattlecraftTranslateTag.WARP_DEFAULT_TABLIST_FOOTER,
                        player.getName(), account.getLevel(), account.getBattleMoney(),
                        account.getBattleCoins(), CommonsConst.WEBSITE);
        TabListAPI.setHeaderAndFooter(player, header, footer);
    }

    @Override
    protected void applyScoreboard(Player player) {
        getScoreboard().applyScoreboard(player);
        if (topKillstreak != null) {
            getScoreboard().updateTopKillstreak(player, topKillstreak.getKey(),
                    topKillstreak.getValue());
        } else {
            getScoreboard().resetTopKillstreak(player);
        }
    }

    private void updateTopKillstreak() {
        new BukkitRunnable() {
            @Override
            public void run() {
                int ks = 0;
                Map.Entry<String, Integer> killStreak = null;
                for (Player player : getPlayers()) {
                    if (!AdminMode.isAdmin(player)) {
                        if (!ProtectionManager.isProtected(player)) {
                            StatusMain status =
                                    getPlayerStatus(player);
                            if (status.getKillstreak() > ks) {
                                ks = status.getKillstreak();
                                killStreak = new AbstractMap.SimpleEntry<>(player.getName(), ks);
                            }
                        }
                    }
                }
                if (killStreak != null) {
                    topKillstreak = killStreak;
                    for (Player player : getPlayers()) {
                        getScoreboard().updateTopKillstreak(player, topKillstreak.getKey(),
                                topKillstreak.getValue());
                    }
                } else {
                    topKillstreak = null;
                    for (Player player : getPlayers()) {
                        getScoreboard().resetTopKillstreak(player);
                    }
                }
            }
        }.runTaskAsynchronously(Battlecraft.getInstance());
    }

    @Override
    protected MainBoard getScoreboard() {
        return (MainBoard) scoreboard;
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
        this.kits.add(new Kit("kangaroo", abilities.collect(Collectors.toSet()), icon,
                DEFAULT_PRICE));

        abilities = Stream.of(getAbilityByClass(StomperAbility.class));
        icon = new ItemStack(Material.IRON_BOOTS);
        this.kits.add(new Kit("stomper", abilities.collect(Collectors.toSet()), icon,
                DEFAULT_PRICE));

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
        this.kits.add(new Kit("fisherman", abilities.collect(Collectors.toSet()), icon,
                DEFAULT_PRICE));

//        abilities = Stream.of(getAbilityByClass(AnchorAbility.class));
//        icon = new ItemStack(Material.ANVIL);
//        this.kits.add(new Kit("anchor", abilities.collect(Collectors.toSet()), icon,
//        DEFAULT_PRICE));
    }
}
