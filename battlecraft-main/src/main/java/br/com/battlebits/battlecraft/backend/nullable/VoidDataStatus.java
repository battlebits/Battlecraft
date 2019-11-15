package br.com.battlebits.battlecraft.backend.nullable;

import br.com.battlebits.battlecraft.backend.DataStatus;
import br.com.battlebits.battlecraft.status.StatusAccount;

import java.util.UUID;

public class VoidDataStatus implements DataStatus {

    @Override
    public StatusAccount getStatus(UUID uniqueId, String name) {
        return null;
    }

    @Override
    public void saveAccount(StatusAccount account) {

    }
}
