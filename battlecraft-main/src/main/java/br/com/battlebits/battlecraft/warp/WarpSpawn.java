package br.com.battlebits.battlecraft.warp;

import br.com.battlebits.battlecraft.event.RealMoveEvent;
import br.com.battlebits.battlecraft.event.protection.PlayerProtectionRemoveEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.manager.ProtectionManager;
import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.PROTECTION_LOST;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.PROTECTION_TAG;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class WarpSpawn extends WarpLocation {

    public WarpSpawn(Location spawnLocation, WorldMap map) {
        super("Spawn", new ItemStack(Material.NETHER_STAR), spawnLocation, map);
    }

    @EventHandler
    public void onWarpJoin(PlayerWarpJoinEvent event) {
        Player p = event.getPlayer();
        if (!isInWarp(p))
            return;

        p.sendMessage("Bem vindo a warp '" + getName() + "'");
        p.sendMessage("addProtection: " + ProtectionManager.addProtection(p));
        p.sendMessage("isProtected: " + ProtectionManager.isProtected(p));
    }

    @EventHandler
    public void onRemoveProtection(PlayerProtectionRemoveEvent event) {
        Player p = event.getPlayer();
        if (!isInWarp(p))
            return;
        Language lang = Commons.getLanguage(p.getUniqueId());
        p.sendMessage(tl(lang, PROTECTION_TAG) + tl(lang, PROTECTION_LOST));
    }

    @EventHandler
    public void onMove(RealMoveEvent event) {
        Player p = event.getPlayer();
        if (!isInWarp(p))
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
        if (!isInWarp(p))
            return;
        ProtectionManager.removeProtection(p);
    }

}
