package br.com.battlebits.battlecraft;

import br.com.battlebits.battlecraft.kits.service.KitService;
import br.com.battlebits.battlecraft.kits.service.KitServiceImpl;
import br.com.battlebits.battlecraft.util.Services;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Battlecraft extends JavaPlugin {

    @Override
    public void onLoad() {

        /* Services */

        Services.create(this);
        Services.add(KitService.class, new KitServiceImpl());
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }
}
