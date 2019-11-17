package br.com.battlebits.battlecraft.warp;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.event.fight.PlayerFightFinishEvent;
import br.com.battlebits.battlecraft.event.fight.PlayerFightStartEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpJoinEvent;
import br.com.battlebits.battlecraft.event.warp.PlayerWarpQuitEvent;
import br.com.battlebits.battlecraft.manager.ProtectionManager;
import br.com.battlebits.battlecraft.protocol.OneVsOneFilter;
import br.com.battlebits.battlecraft.status.StatusAccount;
import br.com.battlebits.battlecraft.status.ranking.Queue1v1;
import br.com.battlebits.battlecraft.status.ranking.RankedQueue;
import br.com.battlebits.battlecraft.status.warpstatus.Status1v1;
import br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag;
import br.com.battlebits.battlecraft.util.NameUtils;
import br.com.battlebits.battlecraft.warp.fight.Challenge;
import br.com.battlebits.battlecraft.warp.fight.ChallengeType;
import br.com.battlebits.battlecraft.warp.fight.Fight1v1;
import br.com.battlebits.battlecraft.warp.scoreboard.OneVsOneBoard;
import br.com.battlebits.battlecraft.warp.scoreboard.WarpScoreboard;
import br.com.battlebits.battlecraft.world.WorldMap;
import br.com.battlebits.battlecraft.world.map.OneVsOneMap;
import br.com.battlebits.commons.Commons;
import br.com.battlebits.commons.CommonsConst;
import br.com.battlebits.commons.account.BattleAccount;
import br.com.battlebits.commons.bukkit.api.admin.AdminMode;
import br.com.battlebits.commons.bukkit.api.item.ActionItemStack;
import br.com.battlebits.commons.bukkit.api.item.ActionItemStack.InteractHandler;
import br.com.battlebits.commons.bukkit.api.item.ItemAction;
import br.com.battlebits.commons.bukkit.api.item.ItemBuilder;
import br.com.battlebits.commons.bukkit.api.player.PingAPI;
import br.com.battlebits.commons.bukkit.api.tablist.TabListAPI;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import br.com.battlebits.commons.translate.Language;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Consumer;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.*;
import static br.com.battlebits.commons.translate.TranslationCommon.tl;

public class Warp1v1 extends Warp {

    private Random random = new Random();
    private Map<Player, Map<ChallengeType, Map<Player, Challenge>>> challenges;
    private Set<Player> playersIn1v1;
    private List<OneVsOneMap> maps;

    private OneVsOneBoard board;

    private InteractHandler challenge1v1 = (player, target, itemStack, itemAction) -> {
        if (itemAction != ItemAction.RIGHT_CLICK_PLAYER)
            return false;
        if (target == null)
            return false;
        if (!inWarp(target))
            return false;

        Language l = Commons.getLanguage(player.getUniqueId());
        Language lt = Commons.getLanguage(target.getUniqueId());
        if (in1v1(target)) {
            player.sendMessage(l.tl(WARP_1V1_TAG) + l.tl(WARP_1V1_PLAYER_IN_COMBAT));
            return false;
        }

        if (hasChallenge(player, target, ChallengeType.NORMAL)) {
            Challenge challenge = getChallenge(player, target, ChallengeType.NORMAL);
            if (!challenge.isExpired()) {
                player.sendMessage(l.tl(WARP_1V1_TAG) + l.tl(WARP_1V1_CHALLENGE_ACCEPTED,
                        target.getName()));
                target.sendMessage(lt.tl(WARP_1V1_TAG) + lt.tl(WARP_1V1_YOUR_CHALLENGE_ACCEPTED,
                        player.getName()));
                new Fight1v1(player, target, challenge, Queue1v1.NORMAL);
                return false;
            }
        }
        if (hasChallenge(target, player, ChallengeType.NORMAL)) {
            Challenge challenge = getChallenge(target, player, ChallengeType.NORMAL);
            if (!challenge.isExpired()) {
                player.sendMessage(l.tl(WARP_1V1_TAG) + l.tl(WARP_1V1_WAIT_TIME));
                return false;
            }
        }
        newChallenge(target, player, new Challenge(player, target,
                maps.get(random.nextInt(maps.size()))));
        player.sendMessage(l.tl(WARP_1V1_TAG) + l.tl(WARP_1V1_CHALLENGE_SENT, target.getName()));
        target.sendMessage(lt.tl(WARP_1V1_TAG) + lt.tl(WARP_1V1_CHALLENGE_RECEIVED,
                player.getName()));
        return false;
    };

    public Warp1v1(WorldMap map) {
        super("1v1", Material.BLAZE_ROD, map);
        challenges = new HashMap<>();
        playersIn1v1 = new HashSet<>();
        maps = new ArrayList<>();
        board = new OneVsOneBoard(this);
        ActionItemStack.register(challenge1v1);
        World world = getSpawnLocation().getWorld();
        Location first = new Location(world, 0.5, world.getHighestBlockYAt(0, -13), -13.5);
        Location second = new Location(world, 0.5, world.getHighestBlockYAt(0, 13), 13.5, 180f, 0f);
        maps.add(new OneVsOneMap(first, second));
        ProtocolLibrary.getProtocolManager().addPacketListener(new OneVsOneFilter(Battlecraft.getInstance(), this));
    }

    // Remove o crafing para quem está na warp
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;
        Player p = (Player) event.getWhoClicked();
        if (!inWarp(p) || in1v1(p))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        handleQuit(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerWarpQuitEvent event) {
        handleQuit(event.getPlayer());
    }

    public void handleQuit(Player p) {
        challenges.remove(p);
    }

    @EventHandler
    public void onFightStart(PlayerFightStartEvent event) {
        this.playersIn1v1.addAll(Arrays.asList(event.getPlayers()));
        for (Player player : event.getPlayers())
            ProtectionManager.removeProtection(player);
        OneVsOneMap map = event.getChallenge().getMap();
        event.getPlayers()[0].teleport(map.getFirstLocation());
        event.getPlayers()[1].teleport(map.getSecondLocation());
        getScoreboard().updateOpponent(event.getPlayers()[0], event.getPlayers()[1].getName());
        getScoreboard().updateOpponent(event.getPlayers()[1], event.getPlayers()[0].getName());
    }

    @EventHandler
    public void onFightFinish(PlayerFightFinishEvent event) {
        Player winner = event.getWinner();
        Player loser = event.getLoser();
        playersIn1v1.remove(winner);
        playersIn1v1.remove(loser);
        int winnerSoups = 0;
        for (ItemStack sopa : winner.getInventory().getContents()) {
            if (sopa != null && sopa.getType() != Material.AIR
                    && sopa.getType() == Material.MUSHROOM_SOUP) {
                winnerSoups += sopa.getAmount();
            }
        }
        DecimalFormat dm = new DecimalFormat("##.#");
        String health = dm.format(winner.getHealth() / 2);
        Language winnerLang = Commons.getLanguage(winner.getUniqueId());
        Language loserLang = Commons.getLanguage(loser.getUniqueId());
        winner.sendMessage(winnerLang.tl(WARP_1V1_TAG) + winnerLang.tl(WARP_1V1_WON,
                loser.getName(), health, winnerSoups));
        loser.sendMessage(loserLang.tl(WARP_1V1_TAG) + loserLang.tl(WARP_1V1_LOST,
                winner.getName(), health, winnerSoups));
        updatePlayerStatus(loser, event.getQueue(), RankedQueue::addDefeat);
        updatePlayerStatus(winner, event.getQueue(), RankedQueue::addVictory);
        applyTabList(loser);
        applyTabList(winner);
        getScoreboard().updateDefeat(loser);
        getScoreboard().updateVictory(winner);
        getScoreboard().resetOpponent(loser);
        getScoreboard().resetOpponent(winner);
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
                ItemBuilder.create(Material.BLAZE_ROD).name(tl(language, WARP_1V1_DIRECT_NAME)).lore("", tl(language,
                        WARP_1V1_DIRECT_LORE)).interact(challenge1v1);
        inv.setItem(4, builder.build());
        ProtectionManager.addProtection(event.getPlayer());

        StatusAccount status = Battlecraft.getInstance().getStatusManager().get(p.getUniqueId());
        if (!status.containsWarpStatus(this)) {
            status.putWarpStatus(this, new Status1v1());
        }
    }

    private void updatePlayerStatus(Player player, Queue1v1 queue,
                                    Consumer<RankedQueue> consumer) {
        Battlecraft.getInstance().getStatusManager().get(player.getUniqueId()).save(statusAccount -> consumer.accept(((Status1v1) statusAccount.getWarpStatus(this)).getQueueStatus(queue)));
    }

    private Status1v1 getPlayerStatus(Player player) {
        return (Status1v1) Battlecraft.getInstance().getStatusManager().get(player.getUniqueId()).getWarpStatus(this);
    }

    private boolean in1v1(Player player) {
        return playersIn1v1.contains(player);
    }

    public Challenge getChallenge(Player desafiado, Player desafiante, ChallengeType type) {
        return challenges.get(desafiado).get(type).get(desafiante);
    }

    public void newChallenge(Player desafiado, Player desafiante, Challenge challenge) {
        Map<ChallengeType, Map<Player, Challenge>> map = challenges.getOrDefault(desafiado,
                new HashMap<>());
        Map<Player, Challenge> playerChallenges = map.getOrDefault(challenge.getChallengeType(),
                new HashMap<>());
        playerChallenges.put(desafiante, challenge);
        map.put(challenge.getChallengeType(), playerChallenges);
        challenges.put(desafiado, map);
    }


    private boolean hasChallenge(Player target, Player player, ChallengeType type) {
        return challenges.containsKey(target)
                && challenges.get(target).containsKey(type)
                && challenges.get(target).get(type).containsKey(player);
    }

    @EventHandler
    public void onTick(UpdateEvent event) {
        if(event.getType() == UpdateEvent.UpdateType.TICK && event.getCurrentTick() % 7 == 0) {
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

    @Override
    protected OneVsOneBoard getScoreboard() {
        return (OneVsOneBoard) board;
    }

    @Override
    protected void applyTabList(Player player) {
        int ping = PingAPI.getPing(player);
        int players = Bukkit.getOnlinePlayers().size() - AdminMode.playersInAdmin();
        Status1v1 status = getPlayerStatus(player);
        RankedQueue defaultQueue = status.getQueueStatus(Queue1v1.NORMAL);
        BattleAccount account = Commons.getAccount(player.getUniqueId());
        String header =
                account.getLanguage().tl(BattlecraftTranslateTag.WARP_1V1_TABLIST_HEADER,
                        defaultQueue.getVictory(), defaultQueue.getDefeat(),
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
    }
}
