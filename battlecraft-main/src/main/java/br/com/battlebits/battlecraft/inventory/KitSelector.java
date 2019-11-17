package br.com.battlebits.battlecraft.inventory;

import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.manager.KitManager;
import br.com.battlebits.battlecraft.util.NameUtils;
import br.com.battlebits.battlecraft.warp.Warp;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.bukkit.api.menu.ClickType;
import br.com.battlebits.commons.bukkit.api.menu.MenuClickHandler;
import br.com.battlebits.commons.bukkit.api.menu.MenuInventory;
import br.com.battlebits.commons.bukkit.api.menu.MenuItem;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;

public class KitSelector extends MenuInventory {

    private static final int ITEMS_PER_PAGE = 21;
    private static final int PREVIOUS_PAGE_SLOT = 27;
    private static final int NEXT_PAGE_SLOT = 35;
    private static final int CENTER = 31;

    private static final int KITS_PER_ROW = 7;

    public KitSelector(Language l, Warp warp) {
        this(l, warp, 1);
    }

    public KitSelector(Language l, Warp warp, int page) {
        this(l, warp, page, (warp.getKits().size() / ITEMS_PER_PAGE) + 1);
    }


    public KitSelector(Language l, Warp warp, int page, int maxPages) {
        super(l.tl(KIT_MENU_TITLE, page, maxPages), 6);
        List<Kit> kitList = new ArrayList<>(warp.getKits());

        kitList.sort(Comparator.comparing(Kit::getName));
        List<MenuItem> items = new ArrayList<>();
        final boolean playerHaveKit = true;
        for (Kit kit : kitList) {
            // CHECK IF PLAYER HAVE KIT
            if (playerHaveKit) {
                items.add(new MenuItem(
                        ItemBuilder.create(kit.getIcon().getType())
                                .name(ChatColor.GREEN + "Kit " + NameUtils.formatString(kit.getName()))
                                .lore(ChatColor.DARK_GRAY + l.tl(kit.getDescriptionTag()))
                                .build(), new KitSelectHandler(kit)));
            } else {
                ItemStack item = ItemBuilder.create(Material.STAINED_GLASS_PANE).durability(14)//
                        .name(ChatColor.GREEN + "Kit " + NameUtils.formatString(kit.getName()))
                        .lore(Collections.singletonList(ChatColor.DARK_GRAY + l.tl(kit.getDescriptionTag(), "", l.tl(KIT_ITEM_NOT_ENABLED)))).build();
                items.add(new MenuItem(item, new KitSelectHandler(kit)));
            }
        }

        int pageStart = 0;
        int pageEnd = ITEMS_PER_PAGE;
        if (page > 1) {
            pageStart = ((page - 1) * ITEMS_PER_PAGE);
            pageEnd = (page * ITEMS_PER_PAGE);
        }
        if (pageEnd > items.size()) {
            pageEnd = items.size();
        }

        if (page == 1) {
            setItem(ItemBuilder.create(Material.INK_SACK).durability(8).name(l.tl(KIT_MENU_NO_PREVIOUS_PAGE)).build(), PREVIOUS_PAGE_SLOT);
        } else {
            setItem(new MenuItem(
                    ItemBuilder.create(Material.INK_SACK).durability(10)
                            .name(l.tl(KIT_MENU_PREVIOUS_PAGE))
                            .lore(l.tl(KIT_MENU_PREVIOUS_LORE))
                            .build(), (player, arg1, arg2, arg3, arg4) -> new KitSelector(l, warp, page - 1).open(player)), PREVIOUS_PAGE_SLOT);
        }

        if ((items.size() / ITEMS_PER_PAGE) + 1 <= page) {
            setItem(ItemBuilder.create(Material.INK_SACK).durability(8).name(l.tl(KIT_MENU_NO_NEXT_PAGE)).build(), NEXT_PAGE_SLOT);
        } else {
            setItem(new MenuItem(
                    ItemBuilder.create(Material.INK_SACK).durability(10)
                            .name(l.tl(KIT_MENU_NEXT_PAGE))
                            .lore(l.tl(KIT_MENU_NEXT_LORE))
                            .build(), (player, arg1, arg2, arg3, arg4) -> new KitSelector(l, warp, page + 1).open(player)), NEXT_PAGE_SLOT);
        }

        int kitSlot = 19;

        for (int i = pageStart; i < pageEnd; i++) {
            MenuItem item = items.get(i);
            setItem(item, kitSlot);
            if (kitSlot % 9 == KITS_PER_ROW) {
                kitSlot += 3;
                continue;
            }
            kitSlot += 1;
        }
        if (items.size() == 0) {
            setItem(ItemBuilder.create(Material.PAINTING).name("§c§lOps!").lore(l.tl(KIT_MENU_ERROR)).build(), CENTER);
        }

        // TODO Check for frontend
//        for (int i = 0; i < 9; i++) {
//            if (getItem(i) == null)
//                setItem(nullItem, i);
//        }

    }

    private static final boolean RIGHT_CLICK_ENABLED = false;

    private static class KitSelectHandler implements MenuClickHandler {

        private Kit kit;

        public KitSelectHandler(Kit kit) {
            this.kit = kit;
        }

        @Override
        public void onClick(Player player, Inventory arg1, ClickType clickType, ItemStack arg3, int arg4) {
            if (ClickType.RIGHT == clickType && RIGHT_CLICK_ENABLED) {
                return;
            }
            KitManager.giveKit(player, kit);
            player.closeInventory();
        }

    }

}
