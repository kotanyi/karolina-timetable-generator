package sk.karolina.timetable.strategy;

import sk.karolina.timetable.io.Parameters;

public class LogisticFunctionStrategy implements ITimetableHolePenaltyStrategy {

	@Override
	public double calculatePenalty(Parameters parameters, int holeCount) {
		if (holeCount == 0) {
			return 0;
		} else {
			return 1 / (1 + Math
					.exp(-parameters.getLogisticFunctionK() * (((double) holeCount / (double) parameters.getHours())
							- parameters.getLogisticFunctionXNaught())));
		}
	}

}
