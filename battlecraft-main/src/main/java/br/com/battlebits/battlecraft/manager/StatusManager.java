package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.backend.DataStatus;
import br.com.battlebits.battlecraft.backend.mongo.MongoStorageDataStatus;
import br.com.battlebits.battlecraft.backend.nullable.VoidDataStatus;
import br.com.battlebits.battlecraft.status.StatusAccount;
import br.com.battlebits.commons.backend.mongodb.MongoDatabase;

import java.util.*;
import java.util.function.Predicate;

public class StatusManager {

    private Map<UUID, StatusAccount> accounts;
    private DataStatus dataStatus;

    public StatusManager() {
//        MongoDatabase database = new MongoDatabase("localhost", "test", "test",
//                "test", 27017);
//        database.connect();
//        dataStatus = new MongoStorageDataStatus(database);
        dataStatus = new VoidDataStatus();
        accounts = new HashMap<>();
    }

    public StatusAccount get(UUID uuid) {
        return accounts.get(uuid);
    }

    public StatusAccount search(Predicate<StatusAccount> predicate) {
        return accounts.values().stream().filter(predicate).findFirst().orElse(null);
    }

    public void insert(StatusAccount account) {
        Objects.requireNonNull(account, "account can't be null.");
        accounts.put(account.getUniqueId(), account);
    }

    public void remove(UUID uniqueId) {
        Objects.requireNonNull(uniqueId, "uniqueId can't be null.");
        accounts.remove(uniqueId);
    }

    public Set<StatusAccount> all() {
        return new HashSet<>(accounts.values());
    }

    public DataStatus dataStatus() {
        return dataStatus;
    }
}
