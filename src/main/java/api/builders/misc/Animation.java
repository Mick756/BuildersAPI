package api.builders.misc;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Animation {
	
	private int index = 0;
	private final @Getter List<AnimationBuilder.Phase> phases;
	private final @Getter long wait;
	
	public Animation(AnimationBuilder builder, long wait) {
		this.phases = builder.phases;
		this.wait = wait;
	}
	
	public AnimationBuilder.Phase current() {
		return this.phases.get(index);
	}
	
	public String next() {
		if (this.atEnd()) index = 0;
		return phases.get(index++).display;
	}
	
	public boolean atEnd() {
		return index >= phases.size();
	}
	
	public static class AnimationBuilder {
		
		private final List<Phase> phases;
		
		public AnimationBuilder() {
			this.phases = new ArrayList<>();
		}
		
		public AnimationBuilder add(Phase phase) {
			phases.add(phase);
			return this;
		}
		
		public AnimationBuilder clear() {
			this.phases.clear();
			return this;
		}
		
		public AnimationBuilder set(int index, Phase phase) {
			this.phases.set(index, phase);
			return this;
		}
		
		public record Phase(String display, int duration) {}
		
	}
}
