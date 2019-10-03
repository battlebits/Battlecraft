package br.com.battlebits.battlecraft.test.manager;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.RealMoveEvent;
import br.com.battlebits.battlecraft.manager.CombatLogManager;
import br.com.battlebits.battlecraft.manager.TeleportManager;
import br.com.battlebits.battlecraft.manager.WarpManager;
import br.com.battlebits.battlecraft.warp.Warp;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.powermock.api.mockito.PowerMockito;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

public class TeleportManagerTest {

    /*
     * O problema dessa classe é que não estão sendo feitos testes de unidade e
     * sim testes de integração (que devem ser feitos mais tarde)
     *
     */

    public Battlecraft setupPlugin() {
        Battlecraft plugin = mock(Battlecraft.class);
        plugin.onEnable();
        return plugin;
    }

    public void teleportTest() {
        Battlecraft battlecraft = setupPlugin();

        Warp to = mock(Warp.class);
        Player player = mock(Player.class);

        TeleportManager.teleport(player, to);

        WarpManager warpManager = battlecraft.getWarpManager();

        assertEquals(warpManager.getPlayerWarp(player).getId(), to.getId());
        assertFalse(TeleportManager.isTeleporting(player));
    }

    public void teleportCombatLogTest() {

        Battlecraft battlecraft = setupPlugin();
        when(battlecraft.getName()).thenReturn("Battlecraft");
        System.out.println(battlecraft.getName());
        Warp to = mock(Warp.class);
        Player player = mock(Player.class);
        Player damaged = mock(Player.class);

        ArrayList<MetadataValue> arrayList = new ArrayList<>();
        when(player.getMetadata("warp")).thenReturn(arrayList);

        CombatLogManager.newCombatLog(player, damaged);

        TeleportManager.teleport(player, to);

        WarpManager warpManager = battlecraft.getWarpManager();
        assertNotEquals(warpManager.getPlayerWarp(player), to);
        assertTrue(TeleportManager.isTeleporting(player));
        // TODO Wait 10sec
        assertEquals(warpManager.getPlayerWarp(player), to);
        assertFalse(TeleportManager.isTeleporting(player));
    }

    public void teleportFailedTest() {

        RealMoveEvent mockEvent = PowerMockito.mock(RealMoveEvent.class);

        Player player = mock(Player.class);

        ArrayList<MetadataValue> arrayList = new ArrayList<>();

        when(player.getMetadata("warp")).thenReturn(arrayList);

        Warp to = mock(Warp.class);

        Player damaged = mock(Player.class);

        CombatLogManager.newCombatLog(player, damaged);

        TeleportManager.teleport(player, to);

        WarpManager warpManager = mock(WarpManager.class);
        assertNotEquals(warpManager.getPlayerWarp(player), to);
        assertTrue(TeleportManager.isTeleporting(player));

        // TODO Run onMoveEvent and wait 10sec

        assertNotEquals(warpManager.getPlayerWarp(player), to);
        assertFalse(TeleportManager.isTeleporting(player));
    }
}
