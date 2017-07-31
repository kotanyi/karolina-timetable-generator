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

	private Parameters parameters;
	private List<Level> timetable;

	public Timetable(Parameters parameters) {
		this.parameters = parameters;
		timetable = new ArrayList<>();
	}

	public Timetable(Timetable timetable) {
		this.parameters = timetable.parameters;
		this.timetable = new ArrayList<Level>(timetable.timetable);
	}

	public void generate() {
		if (parameters.isStartFromCurrentTimetable()) {
			this.timetable = parameters.getCurrentTimetable();
		} else {
			for (int i = 1; i <= parameters.getHours() * 2; i++) {
				timetable.add(Level.getRandomLevel());
			}

			if(parameters.isForceLevel() && !timetable.contains(parameters.getLevelToForce())) {
				timetable.set(random.nextInt(timetable.size()), parameters.getLevelToForce());
			}
		}
	}

	public void mutate() {
		int timetableSize = timetable.size();

		switch (random.nextInt(2)) {
		case 0: // point mutation
			int pointMutations = random.nextInt(parameters.getMaxPointMutations()) + 1;

			for (int i = 1; i <= pointMutations; i++) {
				timetable.set(random.nextInt(timetableSize), Level.getRandomLevel());
			}

			break;
		case 1: // swap two slots
			Collections.swap(timetable, random.nextInt(timetableSize), random.nextInt(timetableSize));

			break;
		}
	}

	public int getSize() {
		return timetable.size();
	}

	public Level getLevel(int index) {
		return timetable.get(index);
	}

	@Override
	public String toString() {
		for (int i = 0; i < parameters.getHours(); i++) {
			if (timetable.get(i * 2).isRound()) {
				Collections.swap(timetable, i * 2, i * 2 + 1);
			}
		}

		return String.join(", ", timetable.stream().map(level -> level.name()).collect(Collectors.toList()));
	}

}
