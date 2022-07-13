package api.builders.scoreboards;

import api.builders.Globals;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class SimpleScoreboardBuilder extends ScoreboardProvider {
	private final Plugin instance;
	
	private final Scoreboard scoreboard;
	
	private final Objective objective;
	
	private final List<ScoreboardProvider.ScoreboardEntry> entries;
	
	public Plugin getInstance() {
		return this.instance;
	}
	
	public SimpleScoreboardBuilder(Plugin instance, String name) {
		this.instance = instance;
		this.scoreboard = Objects.requireNonNull(Bukkit.getServer().getScoreboardManager()).getNewScoreboard();
		this.entries = new ArrayList<>();
		this.objective = this.scoreboard.registerNewObjective("ssb", "dummy", Globals.color(name));
	}
	
	public SimpleScoreboardBuilder setTitle(String value) {
		this.objective.setDisplayName(Globals.color(value));
		return this;
	}
	
	public SimpleScoreboardBuilder add(String value) {
		this.entries.add(new ScoreboardBuilder.SimpleEntry(this, value));
		return this;
	}
	
	public SimpleScoreboardBuilder add(ScoreboardProvider.ScoreboardEntry entry) {
		this.entries.add(entry);
		return this;
	}
	
	public Object getEntries() {
		return this.entries;
	}
	
	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}
	
	public Objective getObjective() {
		return this.objective;
	}
	
	public Scoreboard build() {
		ScoreboardBuilder.placeEntries(this);
		return this.scoreboard;
	}
	
	public void clear() {
		for (ScoreboardProvider.ScoreboardEntry ae : this.entries)
			ae.clear();
		this.entries.clear();
	}
	
	public void send(Player player) {
		if (player != null && player.isOnline())
			player.setScoreboard(this.build());
	}
}
