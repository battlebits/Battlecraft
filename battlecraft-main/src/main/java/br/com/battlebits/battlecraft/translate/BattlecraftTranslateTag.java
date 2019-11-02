package br.com.battlebits.battlecraft.translate;

public enum BattlecraftTranslateTag {

    MOTD,

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

    WARP_1V1_NAME,
    WARP_1V1_DESCRIPTION,
    WARP_1V1_TAG,
    WARP_1V1_PLAYER_IN_COMBAT("warp.1v1.player-in-combat"),
    WARP_1V1_DIRECT_NAME,
    WARP_1V1_DIRECT_LORE,
    WARP_1V1_CHALLENGE_ACCEPTED("warp.1v1.challenge-accepted"),
    WARP_1V1_YOUR_CHALLENGE_ACCEPTED("warp.1v1.your-challenge-accepted"),
    WARP_1V1_WAIT_TIME("warp.1v1.wait-time"),
    WARP_1V1_LEFT,
    WARP_1V1_WON,
    WARP_1V1_LOST,

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
    WARP_LAVACHALLENGE_NAME,
    WARP_LAVACHALLENGE_DESCRIPTION,
    WARP_FPS_NAME,
    WARP_FPS_DESCRIPTION,

    ITEM_FRAME_SOUP("item-frame.soup"),
    ITEM_FRAME_RED("item-frame.red"),
    ITEM_FRAME_BROWN("item-frame.brown"),
    ITEM_FRAME_BOWL("item-frame.bowl"),
    ITEM_FRAME_DEFAULT("item-frame.default");



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
