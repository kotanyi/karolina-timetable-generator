package sk.karolina.timetable.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import sk.karolina.timetable.enums.Level;
import sk.karolina.timetable.io.Parameters;

public class Timetable {

	private static final Random random = new Random();

	private List<Level> timetable;

	public Timetable() {
		timetable = new ArrayList<>();
	}

	public Timetable(Timetable timetable) {
		this.timetable = new ArrayList<Level>(timetable.timetable);
	}

	public void generate() {
		if (Parameters.getInstance().isStartFromCurrentTimetable()) {
			this.timetable = Parameters.getInstance().getCurrentTimetable();
		} else {
			for (int i = 1; i <= Parameters.getInstance().getHours() * 2; i++) {
				timetable.add(Level.getRandomLevel());
			}
		}
	}

	public void mutate() {
		int timetableSize = timetable.size();

		switch (random.nextInt(2)) {
		case 0: // point mutation
			int pointMutations = random.nextInt(Parameters.getInstance().getMaxPointMutations()) + 1;

			for (int i = 1; i <= pointMutations; i++) {
				timetable.set(random.nextInt(timetableSize), Level.getRandomLevel());
			}

			break;
		case 1: // swap two slots
			Collections.swap(timetable, random.nextInt(timetableSize), random.nextInt(timetableSize));

			break;
		}
	}

	public Level getLevel(int index) {
		return timetable.get(index);
	}
	
	public boolean contains(Level level) {
		return timetable.contains(level);
	}

	@Override
	public String toString() {
		for (int i = 0; i < Parameters.getInstance().getHours(); i++) {
			if (timetable.get(i * 2).isRound()) {
				Collections.swap(timetable, i * 2, i * 2 + 1);
			}
		}

		return String.join(", ", timetable.stream().map(level -> level.toString()).collect(Collectors.toList()));
	}

}
