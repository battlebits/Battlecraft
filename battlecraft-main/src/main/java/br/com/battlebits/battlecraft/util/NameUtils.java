package br.com.battlebits.battlecraft.util;

public class NameUtils {
    public static String formatString(String string) {
        char[] stringArray = string.toLowerCase().toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        return new String(stringArray);
    }
}
