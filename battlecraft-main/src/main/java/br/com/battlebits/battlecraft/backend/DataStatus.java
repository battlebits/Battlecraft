package br.com.battlebits.battlecraft.backend;

import br.com.battlebits.battlecraft.backend.status.PvPAccount;

import java.util.UUID;

public interface DataStatus {

    PvPAccount getAccount(UUID uuid);

    PvPAccount getAccount(String name);

    void saveAccount(PvPAccount account, String field);

    void saveAccount(PvPAccount account);

}
