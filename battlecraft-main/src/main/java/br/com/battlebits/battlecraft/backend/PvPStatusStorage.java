package br.com.battlebits.battlecraft.backend;

import br.com.battlebits.battlecraft.backend.status.PvPAccount;

import java.util.*;
import java.util.function.Predicate;

public class PvPStatusStorage {

    private static Map<UUID, PvPAccount> accounts;

    public static void create() {
        new PvPStatusStorage();
    }

    public static PvPAccount get(UUID uuid) {
        return accounts.get(uuid);
    }

    public static PvPAccount search(Predicate<PvPAccount> predicate) {
        return accounts.values().stream().filter(predicate).findFirst().orElse(null);
    }

    public static void insert(PvPAccount account) {
        Objects.requireNonNull(account, "account can't be null.");
        accounts.put(account.getUniqueId(), account);
    }

    public static void remove(PvPAccount account) {
        Objects.requireNonNull(account, "account can't be null.");
        accounts.remove(account.getUniqueId());
    }

    public static Set<PvPAccount> all() {
        return new HashSet<>(accounts.values());
    }

    private PvPStatusStorage() {
        accounts = new HashMap<>();
    }
}
