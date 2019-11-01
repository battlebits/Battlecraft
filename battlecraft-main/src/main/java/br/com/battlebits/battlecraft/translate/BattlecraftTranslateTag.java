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

    WARP_MENU_TITLE,
    WARPSELECTOR_ITEM_NAME,
    WARPSELECTOR_ITEM_LORE,

    KITSELECTOR_ITEM_NAME,
    KITSELECTOR_ITEM_LORE,

    FAST_1V1_ITEM_NAME,
    FAST_1V1_ITEM_LORE,

    WARP_1V1_TAG,
    PLAYER_IN_1V1,
    YOU_ACCEPTED_CHALLENGE,
    PLAYER_ACCEPTED_CHALLENGE,
    WAIT_TO_SEND_AGAIN,

    KIT_MENU_TITLE,
    KIT_MENU_ERROR,
    KIT_MENU_PREVIOUS_PAGE("kit.menu.previous-page"),
    KIT_MENU_PREVIOUS_LORE("kit.menu.previous-lore"),
    KIT_MENU_NO_PREVIOUS_PAGE("kit.menu.no-previous-page"),
    KIT_MENU_NEXT_PAGE("kit.menu.next-page"),
    KIT_MENU_NEXT_LORE("kit.menu.next-lore"),
    KIT_MENU_NO_NEXT_PAGE("kit.menu.no-next-page"),
    KIT_ITEM_NOT_ENABLED("kit.item.not-enabled"),

    KIT_KANGAROO_DESCRIPTION,
    KIT_FISHERMAN_TAG,
    KIT_FISHERMAN_CAUGHT,
    KIT_NINJA_DESCRIPTION,
    KIT_NINJA_TAG,
    KIT_NINJA_DISTANT,
    KIT_NINJA_COOLDOWN,
    KIT_PVP_DESCRIPTION,

    CURRENTLY_HERE,

    WARP_SPAWN_NAME,
    WARP_SPAWN_DESCRIPTION,
    WARP_1V1_NAME,
    WARP_1V1_DESCRIPTION,
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
