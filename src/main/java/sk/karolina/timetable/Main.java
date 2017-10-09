package sk.karolina.timetable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sk.karolina.timetable.entity.Fitness;
import sk.karolina.timetable.entity.Member;
import sk.karolina.timetable.entity.Timetable;
import sk.karolina.timetable.io.MembersReader;
import sk.karolina.timetable.io.MembersWriter;
import sk.karolina.timetable.io.Parameters;
import sk.karolina.timetable.penalty.Assume4HoursStrategy;
import sk.karolina.timetable.penalty.ITimetableHolePenaltyStrategy;
import sk.karolina.timetable.penalty.LogisticFunctionStrategy;
import sk.karolina.timetable.util.FitnessCalculator;

public class Main {
	public static void main(String[] args) throws IOException {

		List<Member> members = MembersReader.read();
		Timetable timetableOld = new Timetable();
		timetableOld.generate();
		Timetable timetableFinal = null;
		Fitness fitnessFinal = new Fitness(0, new ArrayList<>(), new ArrayList<>());
		ITimetableHolePenaltyStrategy strategy = null;

		switch (Parameters.getInstance().getTimetableHolePenaltyStrategy()) {
		case 1:
			strategy = new Assume4HoursStrategy();
			break;
		case 2:
			strategy = new LogisticFunctionStrategy();
			break;
		default:
			new IllegalArgumentException("The allowed values for the timetableHolePenaltyStrategy property are 1 and 2; the actual value was " + Parameters.getInstance().getTimetableHolePenaltyStrategy());
		}

		if (Parameters.getInstance().isCalculateFitnessOnly()) {
			fitnessFinal = FitnessCalculator.calculate(timetableOld, members, strategy);
			timetableFinal = timetableOld;
		} else {
			for (int i = 1; i <= Parameters.getInstance().getIterations(); i++) {
				Timetable timetableNew = new Timetable(timetableOld);
				timetableNew.mutate();
				Fitness fitnessOld = FitnessCalculator.calculate(timetableOld, members, strategy);
				Fitness fitnessNew = FitnessCalculator.calculate(timetableNew, members, strategy);

				if (fitnessNew.getFitness() >= fitnessOld.getFitness()) {
					timetableOld = timetableNew;
					timetableFinal = timetableNew;
					fitnessFinal = fitnessNew;
				} else {
					timetableFinal = timetableOld;
					fitnessFinal = fitnessOld;
				}

				System.out.println(i + ": " + fitnessFinal.getFitness());
			}
		}

		System.out.println(fitnessFinal.getFitness() + " - " + timetableFinal);
		MembersWriter.write(fitnessFinal);
	}
}
