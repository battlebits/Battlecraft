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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.DONATOR_JOIN_FULL;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.SERVER_FULL;
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
        // TODO Refazer mensagem no Inicio
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
        if (standBlock.getBlock().getType() == Material.END_PORTAL_FRAME) {
            double xvel = 0.0D;
            double yvel = 3.0D;
            double zvel = 0.0D;
            p.setVelocity(new Vector(xvel, yvel, zvel));
            p.playSound(p.getLocation(), Sound.ENTITY_HORSE_JUMP, 10.0f, 1.0f);
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
        if (Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers() + br.com.battlebits.commons.bukkit.api.admin.AdminMode.getInstance().playersInAdmin()) {
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

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRemoveMessage(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
}
