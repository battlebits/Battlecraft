package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.ability.Disableable;
import br.com.battlebits.commons.util.ClassGetter;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbilityManager {

    private static Battlecraft battlecraft;
    private static Map<String, Ability> abilities;

    private AbilityManager() {
        battlecraft = Battlecraft.getInstance();
        abilities = new HashMap<>();
    }

    public static void create() {
        new AbilityManager();
    }

    public static void registerKits() {
        List<Class<?>> list = ClassGetter.getClassesForPackage(battlecraft.getClass(), "br.com" +
                ".battlebits.battlecraft.ability.registry");
        list.forEach(clazz -> {
            if (clazz != Ability.class && Ability.class.isAssignableFrom(clazz)) {
                try {
                    Constructor<?> constructor = clazz.getConstructor();
                    if (constructor != null) {
                        Ability kit = (Ability) constructor.newInstance();
                        abilities.put(kit.getId(), kit);
                        if (Disableable.class.isAssignableFrom(clazz)) {
                            battlecraft.getServer().getPluginManager().registerEvents(kit, battlecraft);
                        }
                    }
                } catch (Exception e) {
                    battlecraft.getLogger().warning("Failed to register " + clazz.getSimpleName() + " kit");
                    e.printStackTrace();
                }
            }
        });
        abilities.values().forEach(ability -> battlecraft.getServer().getPluginManager().registerEvents(ability, battlecraft));
    }

    public static Ability getAbilityByName(String name) {
        return abilities.getOrDefault(name, null);
    }

}
