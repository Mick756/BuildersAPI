package api.builders.misc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class TaskUtils {
	
	public static BukkitTask doAsync(JavaPlugin plugin, Runnable runnable) {
		return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
	}
	
	public static BukkitTask doSync(JavaPlugin plugin, Runnable runnable) {
		return Bukkit.getScheduler().runTask(plugin, runnable);
	}
	
	public static BukkitTask doAsyncLater(JavaPlugin plugin, Runnable runnable, long wait) {
		return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, wait);
	}
	
	public static BukkitTask doSyncLater(JavaPlugin plugin, Runnable runnable, long wait) {
		return Bukkit.getScheduler().runTaskLater(plugin, runnable, wait);
	}
	
	public static BukkitTask doAsyncTimer(JavaPlugin plugin, Runnable runnable, long delay, long interval) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval);
	}
	
	public static BukkitTask doSyncTimer(JavaPlugin plugin, Runnable runnable, long delay, long interval) {
		return Bukkit.getScheduler().runTaskTimer(plugin, runnable, delay, interval);
	}
	
	public static BukkitTask doAsyncTimerOneMinute(JavaPlugin plugin, Runnable runnable) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0, (20 * 60));
	}
	
	public static BukkitTask doAsyncTimerThirtySecond(JavaPlugin plugin, Runnable runnable) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0, (20 * 30));
	}
	
	public static BukkitTask doAsyncTimerOneSecond(JavaPlugin plugin, Runnable runnable) {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, 0, 20);
	}
	
	public static BukkitTask doSyncTimerOneMinute(JavaPlugin plugin, Runnable runnable) {
		return Bukkit.getScheduler().runTaskTimer(plugin, runnable, 0, (20 * 60));
	}
	
	public static BukkitTask doSyncTimerThirtySecond(JavaPlugin plugin, Runnable runnable) {
		return Bukkit.getScheduler().runTaskTimer(plugin, runnable, 0, (20 * 30));
	}
	
	public static BukkitTask doSyncTimerOneSecond(JavaPlugin plugin, Runnable runnable) {
		return Bukkit.getScheduler().runTaskTimer(plugin, runnable, 0, 20);
	}
	
}
