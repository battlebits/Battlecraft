package br.com.battlebits.battlecraft.backend.mongo;

import br.com.battlebits.battlecraft.backend.DataStatus;
import br.com.battlebits.battlecraft.status.StatusAccount;
import br.com.battlebits.battlecraft.status.StatusField;
import br.com.battlebits.commons.backend.mongodb.MongoDatabase;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.UUID;

import static br.com.battlebits.commons.util.json.JsonUtils.elementToBson;
import static br.com.battlebits.commons.util.json.JsonUtils.jsonTree;

public class MongoStorageDataStatus implements DataStatus {

    private MongoCollection<StatusAccount> collection;

    public MongoStorageDataStatus(MongoDatabase storage) {
        com.mongodb.client.MongoDatabase database = storage.getDb();
        collection = database.getCollection("pvpaccount", StatusAccount.class);
    }

    @Override
    public StatusAccount getStatus(UUID uuid) {
        return collection.find(Filters.eq("_uniqueId", uuid)).first();
    }

    @Override
    public StatusAccount getStatus(String name) {
        return collection.find(Filters.eq(StatusField.NAME.toString(), name)).first();
    }

    @Override
    public void saveAccount(StatusAccount account, StatusField field) {
        try {
            JsonObject object = jsonTree(account);
            if (object.has(field.toString())) {
                Object value = elementToBson(object.get(field.toString()));
                collection.updateOne(Filters.eq("_id", account.getUniqueId()),
                        new Document("$set", new Document(field.toString(), value)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAccount(StatusAccount account) {
        collection.insertOne(account);
    }
}
