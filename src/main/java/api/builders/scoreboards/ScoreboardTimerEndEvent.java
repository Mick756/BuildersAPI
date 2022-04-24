package api.builders.scoreboards;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ScoreboardTimerEndEvent extends Event {
	private static final HandlerList handlers = new HandlerList();
	
	private final ScoreboardProvider scoreboardBuilder;
	
	private final ScoreboardBuilder.SimpleEntry entry;
	
	public ScoreboardProvider getScoreboardBuilder() {
		return this.scoreboardBuilder;
	}
	
	public ScoreboardBuilder.SimpleEntry getEntry() {
		return this.entry;
	}
	
	public ScoreboardTimerEndEvent(ScoreboardProvider scoreboardBuilder, ScoreboardBuilder.SimpleEntry entry) {
		this.scoreboardBuilder = scoreboardBuilder;
		this.entry = entry;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
