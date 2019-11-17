package br.com.battlebits.battlecraft.listener;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.PlayerDamagePlayerEvent;
import br.com.battlebits.battlecraft.event.RealMoveEvent;
import br.com.battlebits.battlecraft.manager.ProtectionManager;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.CommonsConst;
import br.com.battlebits.commons.account.BattleAccount;
import br.com.battlebits.commons.account.Group;
import br.com.battlebits.commons.bukkit.event.admin.PlayerAdminModeEvent;
import br.com.battlebits.commons.bukkit.event.admin.PlayerAdminModeEvent.AdminMode;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class PlayerListener implements Listener {

    private static final String NOFALL_TAG = "nofall";
    private static final int BATTLECOIN_LOGIN = 10; // TODO Procurar pela melhor quantidade de Battlecoins

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player damager = null;
        if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            Projectile pr = (Projectile) event.getDamager();
            if (pr.getShooter() != null && pr.getShooter() instanceof Player) {
                damager = (Player) pr.getShooter();
            }
        }
        if (damager == null)
            return;
        if (event.isCancelled())
            return;
        PlayerDamagePlayerEvent event2 = new PlayerDamagePlayerEvent(damager, (Player) event.getEntity(), event.getDamage(), event.getFinalDamage());
        Bukkit.getPluginManager().callEvent(event2);
        event.setCancelled(event2.isCancelled());
    }

    @EventHandler
    public void onStarve(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().toLowerCase().startsWith("/kill "))
            event.setCancelled(true);
        if (event.getMessage().equalsIgnoreCase("/kill"))
            event.setCancelled(true);
    }

    @EventHandler
    public void onAdminMonde(PlayerAdminModeEvent event) {
        if (event.getAdminMode() == AdminMode.PLAYER) {
            event.setGameMode(GameMode.ADVENTURE);
        }
    }

    @EventHandler
    public void onLeaves(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (!(event.getEntity() instanceof Player))
            return;
        if (ProtectionManager.isProtected((Player) event.getEntity())) {
            event.setCancelled(true);
            return;
        }
        if (item.getItemMeta().hasDisplayName()) {
            event.setCancelled(true);
            return;
        }
        if (item.getType().toString().contains("SWORD") || item.getType().toString().contains("AXE")) {
            event.setCancelled(true);
            return;
        }
        if (item.getType().toString().contains("HELMET") || item.getType().toString().contains("CHESTPLATE")
                || item.getType().toString().contains("LEGGING") || item.getType().toString().contains("BOOTS")) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.SECOND)
            for (World world : Bukkit.getServer().getWorlds()) {
                for (Entity e : world.getEntitiesByClass(Item.class)) {
                    if (!(e instanceof Item))
                        continue;
                    if (e.getTicksLived() >= 200) {
                        e.remove();
                    }
                }
            }
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMotd(tl(MOTD));
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        final Item drop = event.getItemDrop();
        ItemStack item = drop.getItemStack();
        if (item.hasItemMeta()) {
            event.setCancelled(true);
        } else if (item.toString().contains("SWORD") || item.toString().contains("AXE")) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        if (!Commons.getAccountCommon().getBattleAccount(p.getUniqueId()).hasGroupPermission(Group.ADMIN) || p.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        if (!Commons.getAccountCommon().getBattleAccount(p.getUniqueId()).hasGroupPermission(Group.ADMIN) || p.getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(RealMoveEvent event) {
        Player p = event.getPlayer();
        if (!p.isOnGround())
            return;
        Location standBlock = p.getLocation().clone().add(0, -0.00001, 0);
        if (standBlock.getBlock().getType() == Material.ENDER_PORTAL_FRAME) {
            double xvel = 0.0D;
            double yvel = 3.0D;
            double zvel = 0.0D;
            p.setVelocity(new Vector(xvel, yvel, zvel));
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 10.0f, 1.0f);
            p.setMetadata(NOFALL_TAG, new FixedMetadataValue(Battlecraft.getInstance(), true));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() != DamageCause.FALL)
            return;
        if (!(event.getEntity() instanceof Player))
            return;
        Player p = (Player) event.getEntity();
        if (p.hasMetadata(NOFALL_TAG)) {
            event.setCancelled(true);
            p.removeMetadata(NOFALL_TAG, Battlecraft.getInstance());
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player p = event.getPlayer();
        BattleAccount account = Commons.getAccount(p.getUniqueId());
        if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers() + br.com.battlebits.commons.bukkit.api.admin.AdminMode.playersInAdmin()) {
            if (account.getBattleCoins() >= BATTLECOIN_LOGIN) {
                account.removeBattleCoin(BATTLECOIN_LOGIN);
                p.sendMessage(tl(account.getLanguage(), DONATOR_JOIN_FULL, BATTLECOIN_LOGIN));
            } else
                event.disallow(Result.KICK_FULL,
                        tl(account.getLanguage(), SERVER_FULL, CommonsConst.STORE.toUpperCase()));
        }

        if (!Bukkit.hasWhitelist() || Bukkit.getWhitelistedPlayers().contains(p)) {
            event.allow();
        } else {
            event.disallow(Result.KICK_WHITELIST, "O servidor esta em manutencao!");
        }
    }

    @EventHandler
    public void onPlayerInteractListener(PlayerInteractEvent e) {
        if ((e.getPlayer().getGameMode() != GameMode.CREATIVE)
                && (e.getAction() == Action.PHYSICAL && e.getClickedBlock() != null
                && e.getClickedBlock().getType() != Material.STONE_PLATE
                && e.getClickedBlock().getType() != Material.WOOD_PLATE)
                || (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null
                && (e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE
                || e.getClickedBlock().getType() == Material.CHEST
                || e.getClickedBlock().getType() == Material.ENDER_CHEST))) {
            e.setCancelled(true);
        }
    }


    private Set<Player> shielded = new HashSet<>();

    @EventHandler
    public void onChangeHand(PlayerItemHeldEvent event) {
        checkShield(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()));
    }

    @EventHandler
    public void onChange(PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getMainHandItem();
        if (item.getType().name().contains("SWORD")) {
            if (event.getOffHandItem().getType() == Material.AIR && !shielded.contains(event.getPlayer())) {
                event.setOffHandItem(new ItemStack(Material.SHIELD));
                shielded.add(event.getPlayer());
            }
        } else if (shielded.contains(event.getPlayer())) {
            event.setMainHandItem(null);
            shielded.remove(event.getPlayer());
        }
    }

    private static final int OFF_HAND_SLOT = 40;

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof  Player) {
            Player p = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            if(item == null)
                return;
            if(event.getInventory().getType() != InventoryType.CRAFTING)
                return;
            if (event.getSlot() == OFF_HAND_SLOT && shielded.contains(p)) {
                if(event.getCursor().getType() != Material.AIR) {
                    event.setCurrentItem(null);
                    shielded.remove(p);
                    return;
                } else
                    event.setCancelled(true);
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    checkShield(p, p.getInventory().getItemInMainHand());
                }
            }.runTask(Battlecraft.getInstance());
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        checkShield(player, player.getInventory().getItemInMainHand());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSwordDrop(PlayerDropItemEvent event) {
        checkShield(event.getPlayer(), event.getPlayer().getInventory().getItemInMainHand());
    }

    private void checkShield(Player player, ItemStack item) {
        PlayerInventory inv = player.getInventory();
        if (item != null && item.getType().name().contains("SWORD")) {
            if (inv.getItemInOffHand().getType() == Material.AIR && !shielded.contains(player)) {
                inv.setItemInOffHand(new ItemStack(Material.SHIELD));
                shielded.add(player);
            }
        } else if (shielded.remove(player)) {
            inv.setItemInOffHand(null);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRemoveMessage(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
}
