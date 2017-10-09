package sk.karolina.timetable.penalty;

import sk.karolina.timetable.io.Parameters;

public class LogisticFunctionStrategy implements ITimetableHolePenaltyStrategy {

	@Override
	public double calculatePenalty(int holeCount) {
		if (holeCount == 0) {
			return 0;
		} else {
			return 1 / (1 + Math
					.exp(-Parameters.getInstance().getLogisticFunctionK() * (((double) holeCount / (double) Parameters.getInstance().getHours())
							- Parameters.getInstance().getLogisticFunctionXNaught())));
		}
	}

}
