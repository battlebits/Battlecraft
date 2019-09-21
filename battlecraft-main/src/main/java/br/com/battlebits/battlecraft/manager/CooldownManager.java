package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.commons.bukkit.event.update.UpdateEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CooldownManager implements Listener {

    private static Set<Cooldown> cooldowns;

    private CooldownManager() {
        cooldowns = new HashSet<>();
        Battlecraft.getInstance().getServer().getPluginManager().registerEvents(this, Battlecraft.getInstance());
    }

    public static void create() {
        new CooldownManager();
    }

    public static void addCooldown(Cooldown cooldown) {
        Objects.requireNonNull(cooldown, "cooldown can't be null.");
        cooldowns.add(cooldown);
    }

    public static void removeCooldown(Cooldown cooldown) {
        Objects.requireNonNull(cooldown, "cooldown can't be null.");
        cooldowns.remove(cooldown);
    }

    public static boolean hasCooldown(Player player, String name) {
        return cooldowns.stream().anyMatch(c -> c.getPlayer().equals(player) && c.getName().equals(name));
    }

    public static Cooldown getCooldown(Player player, String name) {
        return cooldowns.stream().filter(c -> c.getPlayer().equals(player) && c.getName().equals(name))
                .findFirst().orElse(null);
    }

    public static void removeAllCooldowns(Player player) {
        for (Cooldown cooldown : cooldowns.stream().filter(c -> c.getPlayer().equals(player)).collect(Collectors.toSet())) {
            removeCooldown(cooldown);
        }
    }

    @EventHandler
    public void onUpdateEvent(UpdateEvent event) {
        if (event.getType() == UpdateEvent.UpdateType.SECOND) {
            for (Cooldown cooldown : cooldowns.stream().filter(c -> c.getTime() < System.currentTimeMillis())
                    .collect(Collectors.toSet())) {
                removeCooldown(cooldown);
            }
        }
    }

    @AllArgsConstructor
    @Getter
    public static class Cooldown {

        private Player player;
        private String name;
        private long time;
    }
}
