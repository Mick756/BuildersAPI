package api.builders;

import org.bukkit.ChatColor;

public class Globals {
	
	public static String color(String color) {
		return ChatColor.translateAlternateColorCodes('&', color);
	}
}
