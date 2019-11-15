package br.com.battlebits.battlecraft.backend.mongo;

import br.com.battlebits.battlecraft.backend.mongo.MongoStorageDataStatus;
import br.com.battlebits.battlecraft.status.StatusAccount;

import java.util.UUID;

public class MongoStatusAccount extends StatusAccount {

    private MongoStorageDataStatus dataStatus;

    public MongoStatusAccount(MongoStorageDataStatus status, UUID uniqueId, String name) {
        super(uniqueId, name);

    }
}
