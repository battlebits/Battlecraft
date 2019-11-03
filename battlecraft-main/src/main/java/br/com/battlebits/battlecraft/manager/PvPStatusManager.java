package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.backend.DataStatus;
import br.com.battlebits.battlecraft.backend.mongo.MongoStorageDataStatus;
import br.com.battlebits.battlecraft.backend.status.PvPAccount;
import br.com.battlebits.commons.backend.mongodb.MongoDatabase;

import java.util.*;
import java.util.function.Predicate;

public class PvPStatusManager {

    private Map<UUID, PvPAccount> accounts;
    private DataStatus dataStatus;

    public PvPStatusManager() {
        MongoDatabase database = new MongoDatabase("localhost", "test", "test",
                "test", 27017);
        database.connect();
        dataStatus = new MongoStorageDataStatus(database);
        accounts = new HashMap<>();
    }

    public PvPAccount get(UUID uuid) {
        return accounts.get(uuid);
    }

    public PvPAccount search(Predicate<PvPAccount> predicate) {
        return accounts.values().stream().filter(predicate).findFirst().orElse(null);
    }

    public void insert(PvPAccount account) {
        Objects.requireNonNull(account, "account can't be null.");
        accounts.put(account.getUniqueId(), account);
    }

    public void remove(PvPAccount account) {
        Objects.requireNonNull(account, "account can't be null.");
        accounts.remove(account.getUniqueId());
    }

    public Set<PvPAccount> all() {
        return new HashSet<>(accounts.values());
    }

    public DataStatus dataStatus() {
        return dataStatus;
    }
}
