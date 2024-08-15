package fr.kenda.tikcontrols.utils;

import org.bukkit.ChatColor;

public class Messages {

    /**
     * Transform message with color
     *
     * @param message String
     * @return String
     */
    public static String transformColor(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
