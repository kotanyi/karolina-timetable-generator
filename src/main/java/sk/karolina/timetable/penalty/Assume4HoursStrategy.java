package sk.karolina.timetable.penalty;

import sk.karolina.timetable.io.Parameters;

public class Assume4HoursStrategy implements ITimetableHolePenaltyStrategy {

	@Override
	public double calculatePenalty(int holeCount) {
		switch (holeCount) {
		case 0:
			return 0;
		case 1:
			return Parameters.getInstance().getPenalty1UnwantedBetweenWanted();
		case 2:
			return Parameters.getInstance().getPenalty1UnwantedBetweenWanted() + Parameters.getInstance().getPenalty2UnwantedBetweenWanted();
		default:
			throw new IllegalStateException(
					"This penalty calculation assumes a 4-hour timetable with a maximum holeCount of 2; the actual holeCount was "
							+ holeCount);
		}
	}

}
