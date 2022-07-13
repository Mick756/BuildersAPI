package api.builders.scoreboards;

import api.builders.Globals;

import java.util.*;

import api.builders.events.ScoreboardTimerEndEvent;
import api.builders.misc.Animation;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class ScoreboardBuilder extends ScoreboardProvider {
	
	private static Plugin instance;
	
	private final Scoreboard scoreboard;
	
	private Objective objective;
	
	private boolean animatedTitle;
	
	private BukkitRunnable animatedTitleTask;
	
	private final Map<Integer, ScoreboardProvider.ScoreboardEntry> entries;
	
	public boolean isAnimatedTitle() {
		return this.animatedTitle;
	}
	
	public BukkitRunnable getAnimatedTitleTask() {
		return this.animatedTitleTask;
	}
	
	public ScoreboardBuilder(Plugin instance) {
		ScoreboardBuilder.instance = instance;
		this.scoreboard = Objects.requireNonNull(Bukkit.getServer().getScoreboardManager()).getNewScoreboard();
		this.entries = new HashMap<>();
	}
	
	public ScoreboardBuilder createNewObjective(String id, String displayName) {
		this.objective = null;
		this.objective = this.scoreboard.registerNewObjective(id, "dummy", Globals.color(displayName));
		return this;
	}
	
	public ScoreboardBuilder setTitle(String title) {
		this.cancelAnimatedTitleTask();
		if (this.objective != null)
			this.objective.setDisplayName(Globals.color(title));
		return this;
	}
	
	public ScoreboardBuilder setAnimatedTitle(JavaPlugin plugin, long delay, Animation animation) {
		this.cancelAnimatedTitleTask();
		
		this.animatedTitleTask = new BukkitRunnable() {
			@Override
			public void run() {
				
				new BukkitRunnable() {
					@Override
					public void run() {
						
						if (animation.atEnd()) {
							cancel();
						} else {
							objective.setDisplayName(animation.next());
						}
					}
				}.runTaskTimer(plugin, 0, animation.getWait());
				
			}
		};
		
		this.animatedTitle = true;
		this.animatedTitleTask.runTaskTimer(plugin, delay, animation.getWait() * animation.getPhases().size());
		
		return this;
	}
	
	public ScoreboardBuilder setDisplay(DisplaySlot slot) {
		if (this.objective != null)
			this.objective.setDisplaySlot(slot);
		return this;
	}
	
	@Deprecated
	public ScoreboardBuilder setLine(int line, String value) {
		if (this.objective != null)
			this.objective.getScore(Globals.color(value)).setScore(line);
		return this;
	}
	
	public ScoreboardBuilder removeEntry(int line) {
		ScoreboardProvider.ScoreboardEntry currEntry = this.entries.get(line);
		if (currEntry != null)
			currEntry.clear();
		this.entries.remove(line);
		return this;
	}
	
	public List<SimpleEntry> getSimpleEntries() {
		List<SimpleEntry> entries = new ArrayList<>();
		for (ScoreboardProvider.ScoreboardEntry se : this.entries.values()) {
			if (se instanceof SimpleEntry)
				entries.add((SimpleEntry)se);
		}
		return entries;
	}
	
	public List<AnimatedEntry> getAnimatedEntries() {
		List<AnimatedEntry> entries = new ArrayList<>();
		for (ScoreboardProvider.ScoreboardEntry se : this.entries.values()) {
			if (se instanceof AnimatedEntry)
				entries.add((AnimatedEntry)se);
		}
		return entries;
	}
	
	public SimpleEntry getSimpleEntry(int line) {
		ScoreboardProvider.ScoreboardEntry se = this.entries.get(line);
		if (se instanceof SimpleEntry)
			return (SimpleEntry)se;
		return null;
	}
	
	public AnimatedEntry getAnimatedEntry(int line) {
		ScoreboardProvider.ScoreboardEntry se = this.entries.get(line);
		if (se instanceof AnimatedEntry)
			return (AnimatedEntry)se;
		return null;
	}
	
	public ScoreboardBuilder createSimpleEntry(int line, String value, boolean overwrite) {
		Validate.isTrue((line <= 22), "Max amount of lines is 22.");
		SimpleEntry entry = new SimpleEntry(this, Globals.color(value));
		placeEntry(this, overwrite, line, entry);
		return this;
	}
	
	public ScoreboardBuilder createAnimatedEntry(int line, boolean overwrite, long delay, Animation animation) {
		Validate.isTrue((line <= 22), "Max amount of lines is 22.");
		AnimatedEntry entry = new AnimatedEntry(this, delay, animation);
		placeEntry(this, overwrite, line, entry);
		return this;
	}
	
	public ScoreboardBuilder createTimer(int line, String value, int time) {
		Validate.isTrue((line <= 22), "Max amount of lines is 22.");
		SimpleEntry entry = new SimpleEntry(this, null);
		entry.makeTimer(time, Globals.color(value));
		this.entries.put(line, entry);
		return this;
	}
	
	public Map<Integer, ScoreboardProvider.ScoreboardEntry> getEntries() {
		return this.entries;
	}
	
	public Scoreboard getScoreboard() {
		return this.scoreboard;
	}
	
	public Objective getObjective() {
		return this.objective;
	}
	
	public Scoreboard build() {
		placeEntries(this);
		return this.scoreboard;
	}
	
	public void clear() {
		cancelAnimatedTitleTask();
		for (ScoreboardProvider.ScoreboardEntry ae : this.entries.values())
			ae.clear();
		this.entries.clear();
	}
	
	public void send(Player player) {
		if (player != null && player.isOnline())
			player.setScoreboard(build());
	}
	
	public ScoreboardBuilder send(List<Player> players) {
		Scoreboard sb = build();
		for (Player player : players)
			player.setScoreboard(sb);
		return this;
	}
	
	public ScoreboardBuilder send(Player... players) {
		return send(Arrays.asList(players));
	}
	
	public void cancelAnimatedTitleTask() {
		if (this.animatedTitle && this.animatedTitleTask != null) {
			this.animatedTitleTask.cancel();
			this.animatedTitleTask = null;
			this.animatedTitle = false;
		}
	}
	
	public static class SimpleEntry implements ScoreboardProvider.ScoreboardEntry {
		private Team team;
		
		private String value;
		
		private String defaultValue;
		
		private final ScoreboardProvider scoreboard;
		
		private BukkitRunnable timerTask;
		
		private int timeLeft;
		
		public Team getTeam() {
			return this.team;
		}
		
		public void setTeam(Team team) {
			this.team = team;
		}
		
		public String getValue() {
			return this.value;
		}
		
		public String getDefaultValue() {
			return this.defaultValue;
		}
		
		public ScoreboardProvider getScoreboard() {
			return this.scoreboard;
		}
		
		public BukkitRunnable getTimerTask() {
			return this.timerTask;
		}
		
		public int getTimeLeft() {
			return this.timeLeft;
		}
		
		public SimpleEntry(ScoreboardProvider scoreboard, String value) {
			this.scoreboard = scoreboard;
			this.value = value;
		}
		
		public void setValue(String value) {
			this.value = value;
			ScoreboardBuilder.setValue(this.team, value);
		}
		
		public void makeTimer(int timeInSeconds, final String defaultValue) {
			this.defaultValue = Globals.color(defaultValue);
			this.timeLeft = timeInSeconds;
			final SimpleEntry entry = this;
			BukkitRunnable run = new BukkitRunnable() {
				public void run() {
					if (entry.timeLeft < 0) {
						ScoreboardTimerEndEvent event = new ScoreboardTimerEndEvent(entry.scoreboard, entry);
						Bukkit.getPluginManager().callEvent(event);
						cancel();
					} else {
						int min = entry.timeLeft / 60;
						int sec = entry.timeLeft % 60;
						String minutes = (min == 0) ? "00" : ((min <= 9) ? ("0" + min) : (Integer.toString(min)));
						String seconds = (sec == 0) ? "00" : ((sec <= 9) ? ("0" + sec) : (Integer.toString(sec)));
						try {
							entry.setValue(String.format(defaultValue, minutes, seconds));
						} catch (Exception ex) {
							entry.setValue("%s:%s");
						}
						entry.timeLeft--;
					}
				}
			};
			this.timerTask = run;
			run.runTaskTimer(ScoreboardBuilder.instance, 0L, 20L);
		}
		
		public void stopTimer() {
			if (this.timerTask != null)
				this.timerTask.cancel();
		}
		
		public void clear() {
			stopTimer();
			this.value = "";
		}
	}
	
	public static class AnimatedEntry implements ScoreboardProvider.ScoreboardEntry {
		private Team team;
		
		private final ScoreboardBuilder scoreboard;
		
		private String value;
		
		private final Animation animation;
		
		private final long delay;
		
		private final BukkitRunnable animatedTask;
		
		public Team getTeam() {
			return this.team;
		}
		
		public void setTeam(Team team) {
			this.team = team;
		}
		
		public ScoreboardBuilder getScoreboard() {
			return this.scoreboard;
		}
		
		public String getValue() {
			return this.value;
		}
		
		public BukkitRunnable getAnimatedTask() {
			return this.animatedTask;
		}
		
		public AnimatedEntry(ScoreboardBuilder scoreboard, long delay, Animation animation) {
			this.value = "";
			this.scoreboard = scoreboard;
			this.delay = delay;
			this.animation = animation;
			
			final AnimatedEntry entry = this;
			this.animatedTask = new BukkitRunnable() {
				public void run() {
					(new BukkitRunnable() {
						
						public void run() {
							
							if (animation.atEnd()) {
								cancel();
							} else {
								entry.setValue(Globals.color(animation.next()));
							}
						}
					}).runTaskTimer(ScoreboardBuilder.instance, delay, animation.getWait());
				}
			};
			
			this.start();
		}
		
		public void start() {
			if (this.animatedTask != null)
				this.animatedTask.runTaskTimer(ScoreboardBuilder.instance, delay, animation.getWait() * animation.getPhases().size());
		}
		
		public void stop() {
			if (this.animatedTask != null)
				this.animatedTask.cancel();
		}
		
		public void setValue(String val) {
			this.value = val;
			ScoreboardBuilder.setValue(this.team, this.value);
		}
		
		public void clear() {
			stop();
			this.value = "";
		}
	}
	
	private static void setValue(Team team, String val) {
		if (team != null && val != null)
			if (val.length() <= 16) {
				team.setSuffix(Globals.color(val));
				team.setPrefix("");
			} else {
				String[] two = splitHalf(val);
				team.setPrefix(Globals.color(two[0]));
				team.setSuffix(Globals.color(two[1]));
			}
	}
	
	private static void placeEntry(ScoreboardBuilder sb, boolean overwrite, int line, ScoreboardProvider.ScoreboardEntry entry) {
		if (overwrite) {
			ScoreboardProvider.ScoreboardEntry currEntry = sb.entries.get(line);
			if (currEntry != null)
				currEntry.clear();
			sb.entries.put(line, entry);
		} else if (!sb.entries.containsKey(line)) {
			sb.entries.put(line, entry);
		}
	}
	
	private static String[] splitHalf(String str) {
		String[] strs = new String[2];
		ChatColor cc1 = ChatColor.WHITE, cc2 = null;
		Character lastChar = null;
		strs[0] = "";
		for (int i = 0; i < str.length() / 2; i++) {
			char c = str.charAt(i);
			if (lastChar != null) {
				ChatColor cc = charsToChatColor(new char[] {lastChar, c });
				if (cc != null)
					if (cc.isFormat()) {
						cc2 = cc;
					} else {
						cc1 = cc;
						cc2 = null;
					}
			}
			strs[0] = strs[0] + c;
			lastChar = c;
		}
		strs[1] = cc1 + "" + ((cc2 != null) ? cc2 : "") + str.substring(str.length() / 2);
		return strs;
	}
	
	private static ChatColor charsToChatColor(char[] chars) {
		for (ChatColor cc : ChatColor.values()) {
			char[] ccChars = cc.toString().toCharArray();
			int same = 0;
			for (int i = 0; i < 2; i++) {
				if (ccChars[i] == chars[i])
					same++;
			}
			if (same == 2)
				return cc;
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public static void placeEntries(ScoreboardProvider builder) {
		Map<Integer, ScoreboardProvider.ScoreboardEntry> entries;
		if (builder instanceof ScoreboardBuilder) {
			entries = (Map<Integer, ScoreboardProvider.ScoreboardEntry>) builder.getEntries();
		} else {
			entries = new HashMap<>();
			List<ScoreboardProvider.ScoreboardEntry> toAdd = (ArrayList<ScoreboardEntry>) builder.getEntries();
			for (int i = 1; i < toAdd.size(); i++)
				entries.put(i, toAdd.get(i - 1));
		}
		for (int line : entries.keySet()) {
			ScoreboardEntry sen = entries.get(line);
			if (sen.getTeam() == null) {
				Team team = builder.getScoreboard().registerNewTeam(builder.getObjective().getName() + "." + builder.getObjective().getCriteria() + "." + (line - 1));
				team.addEntry(ChatColor.values()[line - 1] + "");
				sen.setTeam(team);
			}
			sen.setValue(sen.getValue());
			builder.getObjective().getScore(ChatColor.values()[line - 1] + "").setScore(line - 1);
		}
	}
}
