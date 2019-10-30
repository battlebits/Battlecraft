package br.com.battlebits.battlecraft.ability.registry;

import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.ability.AbilityItem;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

import static br.com.battlebits.battlecraft.manager.ProtectionManager.isProtected;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.KIT_FISHERMAN_CAUGHT;
import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.KIT_FISHERMAN_TAG;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class FishermanAbility extends Ability implements AbilityItem {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerFishListener(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY && event.getCaught() instanceof Player) {
            final Player player = event.getPlayer();
            if (hasAbility(player) && !isProtected(player)) {
                Player caughtPlayer = (Player) event.getCaught();
                if (!isProtected(caughtPlayer)) {
                    caughtPlayer.teleport(player.getLocation());

                    Language language = Commons.getLanguage(caughtPlayer.getUniqueId());
                    caughtPlayer.sendMessage(tl(language, KIT_FISHERMAN_TAG) + tl(language, KIT_FISHERMAN_CAUGHT));
                }
            }
        }
    }

    @Override
    public List<ItemStack> getItems() {
        return Collections.singletonList(ItemBuilder.create(Material.FISHING_ROD).flag(ItemFlag.HIDE_UNBREAKABLE).
                unbreakable(true).name(ChatColor.GOLD + "Fisherman").build());
    }
}
