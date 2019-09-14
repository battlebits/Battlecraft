package br.com.battlebits.battlecraft.kits.service;

import br.com.battlebits.battlecraft.kits.Kit;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class KitServiceImpl implements KitService {

    private Set<Kit> kits;

    public KitServiceImpl() {
        kits = new HashSet<>();
    }

    @Override
    public void create(Kit model) {
        Objects.requireNonNull(model, "kit cannot be null.");
        kits.add(model);
    }

    @Override
    public void remove(Kit model) {
        Objects.requireNonNull(model, "kit cannot be null.");
        kits.remove(model);
    }

    @Override
    public Kit get(Kit model) {
        return get(model.getName());
    }

    @Override
    public Kit get(String name) {
        return kits.stream().filter(kit -> kit.getName().equalsIgnoreCase(name) && kit.isEnabled()).findFirst().orElse(null);
    }

    @Override
    public Set<Kit> all() {
        return kits;
    }
}
