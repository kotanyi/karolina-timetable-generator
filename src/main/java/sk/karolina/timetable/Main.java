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
import sk.karolina.timetable.strategy.Assume4HoursStrategy;
import sk.karolina.timetable.strategy.ITimetableHolePenaltyStrategy;
import sk.karolina.timetable.strategy.LogisticFunctionStrategy;
import sk.karolina.timetable.util.FitnessCalculator;

public class Main {
	public static void main(String[] args) throws IOException {

		Parameters parameters = new Parameters();
		MembersReader membersReader = new MembersReader(parameters);
		List<Member> members = membersReader.read();
		Timetable timetableOld = new Timetable(parameters);
		timetableOld.generate();
		Timetable timetableFinal = null;
		Fitness fitnessFinal = new Fitness(0, new ArrayList<>(), new ArrayList<>());
		FitnessCalculator fitnessCalculator = new FitnessCalculator();
		ITimetableHolePenaltyStrategy strategy = null;

		switch (parameters.getTimetableHolePenaltyStrategy()) {
		case 1:
			strategy = new Assume4HoursStrategy();
			break;
		case 2:
			strategy = new LogisticFunctionStrategy();
			break;
		default:
			new IllegalArgumentException("The allowed values for the timetableHolePenaltyStrategy property are 1 and 2; the actual value was " + parameters.getTimetableHolePenaltyStrategy());
		}

		if (parameters.isCalculateFitnessOnly()) {
			fitnessFinal = fitnessCalculator.calculate(parameters, timetableOld, members, strategy);
			System.out.println(fitnessFinal.getFitness() + " - " + timetableOld);
		} else {
			for (int i = 1; i <= parameters.getIterations(); i++) {
				Timetable timetableNew = new Timetable(timetableOld);
				timetableNew.mutate();
				Fitness fitnessOld = fitnessCalculator.calculate(parameters, timetableOld, members, strategy);
				Fitness fitnessNew = fitnessCalculator.calculate(parameters, timetableNew, members, strategy);

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

			System.out.println(fitnessFinal.getFitness() + " - " + timetableFinal);
		}

		MembersWriter.write(fitnessFinal.getProgramHappinesses(), fitnessFinal.getTimetableHappinesses());
	}
}
