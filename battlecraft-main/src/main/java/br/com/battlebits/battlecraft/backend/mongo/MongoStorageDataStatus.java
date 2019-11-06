package br.com.battlebits.battlecraft.backend.mongo;

import br.com.battlebits.battlecraft.backend.DataStatus;
import br.com.battlebits.battlecraft.backend.status.PvPAccount;
import br.com.battlebits.commons.backend.mongodb.MongoDatabase;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.UUID;

import static br.com.battlebits.commons.util.json.JsonUtils.elementToBson;
import static br.com.battlebits.commons.util.json.JsonUtils.jsonTree;

public class MongoStorageDataStatus implements DataStatus {

    private MongoCollection<PvPAccount> collection;

    public MongoStorageDataStatus(MongoDatabase storage) {
        com.mongodb.client.MongoDatabase database = storage.getDb();
        collection = database.getCollection("pvpaccount", PvPAccount.class);
    }

    @Override
    public PvPAccount getAccount(UUID uuid) {
        return collection.find(Filters.eq("_uniqueId", uuid)).first();
    }

    @Override
    public PvPAccount getAccount(String name) {
        return collection.find(Filters.eq("_name", name)).first();
    }

    @Override
    public void saveAccount(PvPAccount account, String field) {
        try {
            JsonObject object = jsonTree(account);
            if (object.has(field)) {
                Object value = elementToBson(object.get(field));
                collection.updateOne(Filters.eq("_id", account.getUniqueId()),
                        new Document("$set", new Document(field, value)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAccount(PvPAccount account) {
        collection.insertOne(account);
    }
}
