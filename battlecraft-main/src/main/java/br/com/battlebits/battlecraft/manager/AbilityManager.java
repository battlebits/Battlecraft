package br.com.battlebits.battlecraft.manager;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.ability.Ability;
import br.com.battlebits.battlecraft.ability.AbilityImpl;
import br.com.battlebits.commons.util.ClassGetter;

import java.lang.reflect.Constructor;
import java.util.*;

public class AbilityManager {

    private static Battlecraft battlecraft;
    private static Set<Ability> abilities;

    private AbilityManager() {
        battlecraft = Battlecraft.getInstance();
        abilities = new HashSet<>();
    }

    public static void create() {
        new AbilityManager();
    }

    public static void registerKits() {
        List<Class<?>> list = ClassGetter.getClassesForPackage(battlecraft.getClass(), "br.com.battlebits.battlecraft.ability");
        list.forEach(clazz -> {
            if (clazz != Ability.class && clazz != AbilityImpl.class && Ability.class.isAssignableFrom(clazz)) {
                try {
                    Constructor<?> constructor = clazz.getConstructor();
                    if (constructor != null) {
                        Ability kit = (Ability) constructor.newInstance();
                        abilities.add(kit);
                    }
                } catch (Exception e) {
                    battlecraft.getLogger().warning("Failed to register " + clazz.getSimpleName() + " kit");
                    e.printStackTrace();
                }
            }
        });
        abilities.forEach(ability -> battlecraft.getServer().getPluginManager().registerEvents(ability, battlecraft));
    }

    public static Ability getAbilityByName(String name) {
        return abilities.stream().filter(ability -> ability.getName().equals(name)).findFirst().orElse(null);
    }

    public static Ability getRandomKit() {
        int index = new Random().nextInt(abilities.size());
        Iterator<Ability> iterator = abilities.iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        return iterator.next();
    }
}
