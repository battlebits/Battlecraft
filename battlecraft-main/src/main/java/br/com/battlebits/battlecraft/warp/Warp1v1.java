package br.com.battlebits.battlecraft.warp;

import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.manager.ProtectionManager;
import br.com.battlebits.battlecraft.warp.fight.Challenge;
import br.com.battlebits.battlecraft.warp.fight.ChallengeType;
import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.bukkit.api.item.ActionItemStack;
import br.com.battlebits.commons.bukkit.api.item.ActionItemStack.InteractHandler;
import br.com.battlebits.commons.bukkit.api.item.ItemAction;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.translate.Language;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class Warp1v1 extends Warp {

    private Map<Player, Map<ChallengeType, Map<Player, Challenge>>> challenges;
    private Set<Player> playersIn1v1;

    private InteractHandler challenge1v1 = (player, target, itemStack, itemAction) -> {
        if(itemAction != ItemAction.RIGHT_CLICK_PLAYER)
            return false;
        if (target == null)
            return false;
        Language l = Commons.getLanguage(player.getUniqueId());
        if (in1v1(target)) {
            player.sendMessage(l.tl(WARP_1V1_TAG) + l.tl(PLAYER_IN_1V1));
            return false;
        }

        if (hasChallenge(target, player, ChallengeType.NORMAL)) {
            Challenge challenge = getChallenge(target, player, ChallengeType.NORMAL);
            if (!challenge.isExpired()) {
                player.sendMessage(l.tl(WARP_1V1_TAG) + l.tl(YOU_ACCEPTED_CHALLENGE, target.getName()));
                target.sendMessage(l.tl(WARP_1V1_TAG) + l.tl(PLAYER_ACCEPTED_CHALLENGE, player.getName()));
                // TODO FIGHT
                return false;
            }
        }
        if (hasChallenge(player, target, ChallengeType.NORMAL)) {
            Challenge challenge = getChallenge(player, target, ChallengeType.NORMAL);
            if (!challenge.isExpired()) {
                player.sendMessage(l.tl(WARP_1V1_TAG) + l.tl(WAIT_TO_SEND_AGAIN));
                return false;
            }
        }
        newChallenge(target, player, new Challenge(player, target));
        return false;
    };

    public Warp1v1(Location spawnLocation, WorldMap map) {
        super("1v1", Material.BLAZE_ROD, spawnLocation, map);
        challenges = new HashMap<>();
        playersIn1v1 = new HashSet<>();
        ActionItemStack.register(challenge1v1);
    }

    // Remove o crafing para quem está na warp
    // TODO Rever caso esteja com recraft
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;
        Player p = (Player) event.getWhoClicked();
        if (!inWarp(p))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        handleQuit(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        handleQuit(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        handleQuit(event.getPlayer());
    }

    public void handleQuit(Player p) {
        challenges.remove(p);
    }



    @EventHandler
    public void onWarpTeleport(PlayerWarpJoinEvent event) {
        Player p = event.getPlayer();
        if (!inWarp(p))
            return;
        PlayerInventory inv = p.getInventory();
        inv.clear();
        Language language = Commons.getLanguage(p.getUniqueId());
        ItemBuilder builder =
                ItemBuilder.create(Material.BLAZE_ROD).name(tl(language, FAST_1V1_ITEM_NAME)).lore("", tl(language,
                        FAST_1V1_ITEM_LORE)).interact(challenge1v1);
        inv.setItem(4, builder.build());
        ProtectionManager.addProtection(event.getPlayer());
    }

    // TODO
    private boolean in1v1(Player player) {
        return playersIn1v1.contains(player);
    }

    public Challenge getChallenge(Player desafiado, Player desafiante, ChallengeType type) {
        return challenges.get(desafiado).get(type).get(desafiante);
    }

    public void newChallenge(Player desafiado, Player desafiante, Challenge challenge) {
        Map<ChallengeType, Map<Player, Challenge>> map = challenges.getOrDefault(desafiado, new HashMap<>());
        Map<Player, Challenge> playerChallenges = map.getOrDefault(challenge.getChallengeType(), new HashMap<>());
        playerChallenges.put(desafiante, challenge);
        map.put(challenge.getChallengeType(), playerChallenges);
        challenges.put(desafiado, map);
    }


    private boolean hasChallenge(Player target, Player player, ChallengeType type) {
        return challenges.containsKey(target)
                && challenges.get(target).containsKey(type)
                && challenges.get(target).get(type).containsKey(player);
    }




}
