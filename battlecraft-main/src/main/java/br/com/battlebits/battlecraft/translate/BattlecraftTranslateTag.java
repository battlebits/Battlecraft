package br.com.battlebits.battlecraft.translate;

public enum BattlecraftTranslateTag {

    TELEPORT_TAG,
    TELEPORT_IN_PROGRESS("teleport.in-progress"),
    TELEPORT_ON_AIR("teleport.on-air"),
    TELEPORT_COOLDOWN,
    PROTECTION_TAG,
    PROTECTION_LOST,
    DONATOR_JOIN_FULL("donator.join-full"),
    SERVER_FULL("server-full");




    private String key;

    BattlecraftTranslateTag(String key) {
        this.key = key;
    }

    BattlecraftTranslateTag() {
    }

    private String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key == null ? name().replace("_", ".").replace("-", ".").toLowerCase() : key;
    }
}
