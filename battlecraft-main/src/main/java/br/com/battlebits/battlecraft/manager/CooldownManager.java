package br.com.battlebits.battlecraft.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CooldownManager {

    private static Set<Cooldown> cooldowns;

    private CooldownManager() {
        cooldowns = new HashSet<>();
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

    public static void removeCooldown(Player player, String name) {
        Objects.requireNonNull(player, "player can't be null.");
        Objects.requireNonNull(name, "name can't be null.");
        cooldowns.removeIf(c -> c.getPlayer().equals(player) && c.getName().equals(name));
    }

    public static boolean hasCooldown(Player player, String name) {
        return cooldowns.stream().anyMatch(c -> c.getPlayer().equals(player) && c.getName().equals(name));
    }

    public static Cooldown getCooldown(Player player, String name) {
        return cooldowns.stream().filter(c -> c.getPlayer().equals(player) && c.getName().equals(name))
                .findFirst().orElse(null);
    }

    public static void removeAllCooldowns(Player player) {
        cooldowns.removeIf(c -> c.getPlayer().equals(player));
    }

    public static void checkCooldowns() {
        for (Cooldown cooldown :
                cooldowns.stream().filter(c -> c.getTime() < System.currentTimeMillis())
                        .collect(Collectors.toSet())) {
            removeCooldown(cooldown);
            Player t = cooldown.getPlayer();
            t.playSound(t.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5F, 1.0F);
        }
    }


    @AllArgsConstructor
    @Getter
    @Builder
    public static class Cooldown {
        private Player player;
        private String name;
        private long time;
    }
}
