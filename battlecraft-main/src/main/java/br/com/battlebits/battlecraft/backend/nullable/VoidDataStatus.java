package br.com.battlebits.battlecraft.backend.nullable;

import br.com.battlebits.battlecraft.backend.DataStatus;
import br.com.battlebits.battlecraft.status.StatusAccount;
import br.com.battlebits.battlecraft.status.StatusField;

import java.util.UUID;

public class VoidDataStatus implements DataStatus {

    @Override
    public StatusAccount getStatus(UUID uniqueId) {
        return null;
    }

    @Override
    public StatusAccount getStatus(String name) {
        return null;
    }

    @Override
    public void saveAccount(StatusAccount account, StatusField field) {

    }

    @Override
    public void saveAccount(StatusAccount account) {

    }
}
