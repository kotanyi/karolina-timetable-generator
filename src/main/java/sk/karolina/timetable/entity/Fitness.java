package sk.karolina.timetable.entity;

import java.util.List;

public class Fitness {

	private double fitness;
	private List<Double> programHappinesses;
	private List<Double> timetableHappinesses;

	public Fitness(double fitness, List<Double> programHappinesses, List<Double> timetableHappinesses) {
		this.fitness = fitness;
		this.programHappinesses = programHappinesses;
		this.timetableHappinesses = timetableHappinesses;
	}

	public double getFitness() {
		return fitness;
	}

	public List<Double> getProgramHappinesses() {
		return programHappinesses;
	}

	public List<Double> getTimetableHappinesses() {
		return timetableHappinesses;
	}

}
