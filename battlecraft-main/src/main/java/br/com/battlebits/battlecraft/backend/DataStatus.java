package br.com.battlebits.battlecraft.backend;

import br.com.battlebits.battlecraft.status.StatusAccount;

import java.util.UUID;

public interface DataStatus {

    StatusAccount getStatus(UUID uniqueId, String name);

    void saveAccount(StatusAccount account);

}
