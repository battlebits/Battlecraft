package br.com.battlebits.battlecraft.backend.mongo;

import br.com.battlebits.battlecraft.backend.DataStatus;
import br.com.battlebits.battlecraft.status.StatusAccount;
import br.com.battlebits.battlecraft.status.ranking.RankedQueue;
import br.com.battlebits.battlecraft.status.warpstatus.Status1v1;
import br.com.battlebits.battlecraft.status.warpstatus.StatusMain;
import br.com.battlebits.commons.backend.mongodb.MongoDatabase;
import org.bukkit.entity.Player;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.UUID;

public class MongoStorageDataStatus implements DataStatus {

    private MongoStatusDAO statusDAO;

    public MongoStorageDataStatus(MongoDatabase storage) {
        Morphia morphia = new Morphia();
        morphia.map(StatusAccount.class, StatusMain.class, Status1v1.class, RankedQueue.class);
        Datastore datastore = morphia.createDatastore(storage.getClient(), "battlecraft");
        datastore.ensureIndexes();

        statusDAO = new MongoStatusDAO(StatusAccount.class, datastore);
    }

    @Override
    public StatusAccount getStatus(UUID uniqueId, String name) {
        StatusAccount account = statusDAO.findOne("uniqueId", uniqueId);
        if (account == null) {
            account = new StatusAccount(uniqueId, name);
            saveAccount(account);
        }
        if (!account.getName().equals(name)) {
            account.setName(name);
            saveAccount(account);
        }
        return account;
    }

    @Override
    public void saveAccount(StatusAccount account) {
        statusDAO.save(account);
    }
}
