package api.builders;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * BuildersAPI uses Paper. BuildersAPI will not run on Spigot.
 */
public class BuildersAPI extends JavaPlugin {
	
	private static @Getter BuildersAPI instance;
	
	@Override
	public void onEnable() {
		instance = this;
		
		logInfo("Loading useful libraries.");
		
	}
	
	public static void logInfo(String message) {
		getInstance().getLogger().info(message);
	}
}
