package br.com.battlebits.battlecraft.backend;

import br.com.battlebits.battlecraft.status.StatusAccount;
import br.com.battlebits.battlecraft.status.StatusField;

import java.util.UUID;

public interface DataStatus {

    StatusAccount getStatus(UUID uniqueId);

    StatusAccount getStatus(String name);

    void saveAccount(StatusAccount account, StatusField field);

    void saveAccount(StatusAccount account);

}
