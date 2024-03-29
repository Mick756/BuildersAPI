package api.builders.scoreboards;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public abstract class ScoreboardProvider {
	public abstract Object getEntries();
	
	public abstract Scoreboard getScoreboard();
	
	public abstract Objective getObjective();
	
	public abstract Scoreboard build();
	
	public abstract void send(Player paramPlayer);
	
	public abstract void clear();
	
	interface ScoreboardEntry {
		Team getTeam();
		
		void setTeam(Team param1Team);
		
		String getValue();
		
		void setValue(String param1String);
		
		void clear();
	}
}
