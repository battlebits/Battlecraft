package br.com.battlebits.battlecraft.backend.mongo;

import br.com.battlebits.battlecraft.status.StatusAccount;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

public class MongoStatusDAO extends BasicDAO<StatusAccount, String> {

    public MongoStatusDAO(Class<StatusAccount> entityClass, Datastore ds) {
        super(entityClass, ds);
    }
}
