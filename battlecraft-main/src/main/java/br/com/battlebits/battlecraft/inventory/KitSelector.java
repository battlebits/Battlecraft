package br.com.battlebits.battlecraft.inventory;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.manager.TeleportManager;
import br.com.battlebits.battlecraft.manager.WarpManager;
import br.com.battlebits.battlecraft.warp.Warp;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.bukkit.api.menu.MenuInventory;
import br.com.battlebits.commons.bukkit.api.menu.MenuItem;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.CURRENTLY_HERE;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.WARPS_MENU_TITLE;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class KitSelector extends MenuInventory {

    public KitSelector(Language l, Warp warp) {
        super(l.tl(WARPS_MENU_TITLE), 1);

        WarpManager manager = Battlecraft.getInstance().getWarpManager();
        ItemBuilder builder;
        MenuItem menuItem;
        int slot = 0;
        List<Kit> warpList = new ArrayList<>(warp.getKits());
        
    }
}
