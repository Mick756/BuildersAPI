package api.builders.misc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class BlockUtils {
	
	/**
	 * Fill a large area of blocks.
	 * @param plugin The task is done async requiring the plugin to run the task on.
	 * @param corner1 One corner of the region
	 * @param corner2 Another corner of the region
	 * @param material The material to set the blocks to.
	 */
	public static void fill(JavaPlugin plugin, Location corner1, Location corner2, Material material) {
		fill(plugin, getLocations(corner1, corner2), material);
	}
	
	/**
	 * Fill a large area of blocks.
	 * @param plugin The task is done async requiring the plugin to run the task on.
	 * @param locations A list of locations to set.
	 * @param material The material to set the blocks to.
	 */
	public static void fill(JavaPlugin plugin, List<Location> locations, Material material) {
		
		TaskUtils.doAsync(plugin, () -> {
			for (Location location : locations) {
				location.getBlock().setType(material);
			}
		});
		
	}
	
	/**
	 * Get a list of locations within a region.
	 * @param corner1 One corner of the region.
	 * @param corner2 Another corner of the region.
	 * @return The list of locations within the defined region.
	 */
	public static List<Location> getLocations(Location corner1, Location corner2) {
		List<Location> locations = new ArrayList<>();
		
		World w = corner1.getWorld();
		
		int maxX = Math.max(corner1.getBlockX(), corner2.getBlockX());
		int minX = Math.min(corner1.getBlockX(), corner2.getBlockX());
		int maxY = Math.max(corner1.getBlockY(), corner2.getBlockY());
		int minY = Math.min(corner1.getBlockY(), corner2.getBlockY());
		int maxZ = Math.max(corner1.getBlockZ(), corner2.getBlockZ());
		int minZ = Math.min(corner1.getBlockZ(), corner2.getBlockZ());
		
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				for (int z = minZ; z < maxZ; z++) {
					locations.add(new Location(w, x, y, z));
				}
			}
		}
		
		return locations;
	}
}
