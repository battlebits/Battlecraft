package br.com.battlebits.battlecraft.kits.service;

import br.com.battlebits.battlecraft.crud.Crud;
import br.com.battlebits.battlecraft.kits.Kit;

public interface KitService extends Crud<Kit> {

    Kit get(String name);

}
