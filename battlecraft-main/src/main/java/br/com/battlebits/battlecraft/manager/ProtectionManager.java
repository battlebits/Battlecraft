package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.protection.PlayerProtectionReceiveEvent;
import br.com.battlebits.battlecraft.event.protection.PlayerProtectionRemoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class ProtectionManager {

    private static String PROTECTION_META = "protected";

    public static boolean isProtected(Player player) {
        return player.hasMetadata(PROTECTION_META);
    }

    public static boolean addProtection(Player player) {
        if (isProtected(player))
            return false;
        PlayerProtectionReceiveEvent event = new PlayerProtectionReceiveEvent(player);
        Battlecraft.getInstance().getServer().getPluginManager().callEvent(event);
        if(!event.isCancelled())
            player.setMetadata(PROTECTION_META, new FixedMetadataValue(Battlecraft.getInstance(), true));
        return !event.isCancelled();
    }

    public static boolean removeProtection(Player player) {
        if (!isProtected(player))
            return false;
        PlayerProtectionRemoveEvent event = new PlayerProtectionRemoveEvent(player);
        Battlecraft.getInstance().getServer().getPluginManager().callEvent(event);
        if(!event.isCancelled())
            player.removeMetadata(PROTECTION_META, Battlecraft.getInstance());
        return !event.isCancelled();
    }

}
