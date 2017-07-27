package sk.karolina.timetable.strategy;

import sk.karolina.timetable.io.Parameters;

public class Assume4HoursStrategy implements ITimetableHolePenaltyStrategy {

	@Override
	public double calculatePenalty(Parameters parameters, int holeCount) {
		switch (holeCount) {
		case 0:
			return 0;
		case 1:
			return parameters.getPenalty1UnwantedBetweenWanted();
		case 2:
			return parameters.getPenalty1UnwantedBetweenWanted() + parameters.getPenalty2UnwantedBetweenWanted();
		default:
			throw new IllegalStateException(
					"This strategy assumes a 4-hour timetable with a maximum holeCount of 2; the actual holeCount was "
							+ holeCount);
		}
	}

}
