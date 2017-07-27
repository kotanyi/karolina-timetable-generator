package sk.karolina.timetable.strategy;

import sk.karolina.timetable.io.Parameters;

public interface ITimetableHolePenaltyStrategy {

	public double calculatePenalty(Parameters parameters, int holeCount);

}
