package br.com.battlebits.battlecraft.translate;

public enum BattlecraftTranslateTag {

    JOIN_MESSAGE_TITLE,
    JOIN_MESSAGE_SUBTITLE,

    TELEPORT_TAG,
    TELEPORT_IN_PROGRESS("teleport.in-progress"),
    TELEPORT_ON_AIR("teleport.on-air"),
    TELEPORT_COOLDOWN,
    TELEPORT_CANCELED,

    PROTECTION_TAG,
    PROTECTION_LOST,

    DONATOR_JOIN_FULL("donator.join-full"),
    SERVER_FULL("server-full"),

    WARPS_MENU_TITLE,
    WARPS_ITEM_NAME,
    WARPS_ITEM_LORE,

    CURRENTLY_HERE,

    WARP_SPAWN_NAME,
    WARP_SPAWN_DESCRIPTION,
    WARP_LAVACHALLENGE_NAME,
    WARP_LAVACHALLENGE_DESCRIPTION,
    WARP_FPS_NAME,
    WARP_FPS_DESCRIPTION;



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
