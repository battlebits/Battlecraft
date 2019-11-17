package br.com.battlebits.battlecraft.backend.mongo;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.backend.DataStatus;
import br.com.battlebits.battlecraft.status.StatusAccount;
import br.com.battlebits.battlecraft.status.ranking.RankedQueue;
import br.com.battlebits.battlecraft.status.warpstatus.Status1v1;
import br.com.battlebits.battlecraft.status.warpstatus.StatusMain;
import br.com.battlebits.commons.backend.mongodb.MongoDatabase;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;

import java.util.List;
import java.util.UUID;

public class MongoStorageDataStatus implements DataStatus {

    private Datastore datastore;

    public MongoStorageDataStatus(MongoDatabase storage) {
        Morphia morphia = new Morphia();
        morphia.map(StatusAccount.class, StatusMain.class, Status1v1.class, RankedQueue.class);
        morphia.getMapper().setOptions(MapperOptions.builder().classLoader(Battlecraft.getInstance().getClass().getClassLoader()).build());
        datastore = morphia.createDatastore(storage.getClient(), "test");
        datastore.ensureIndexes();
    }

    @Override
    public StatusAccount getStatus(UUID uniqueId, String name) {
        List<StatusAccount> accounts = datastore.createQuery(StatusAccount.class).field("uniqueId"
        ).equal(uniqueId).find().toList();
        StatusAccount account = null;
        if (accounts.size() == 0) {
            account = new StatusAccount(uniqueId, name);
            datastore.save(account);
        } else {
            account = accounts.get(0);
        }
        if (!account.getName().equals(name)) {
            account.setName(name);
            saveAccount(account);
        }
        return account;
    }

    @Override
    public void saveAccount(StatusAccount account) {
        datastore.save(account);
    }
}
