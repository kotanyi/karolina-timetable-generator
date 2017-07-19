package sk.karolina.timetable;

import java.io.IOException;
import java.util.List;

import sk.karolina.timetable.entity.Member;
import sk.karolina.timetable.entity.Timetable;
import sk.karolina.timetable.io.MembersReader;
import sk.karolina.timetable.io.Parameters;
import sk.karolina.timetable.util.Fitness;

public class Main {
	public static void main(String[] args) throws IOException {

		Parameters parameters = new Parameters();
		MembersReader membersReader = new MembersReader(parameters);
		List<Member> members = membersReader.read();
		Timetable timetableOld = new Timetable(parameters);
		timetableOld.generate();
		Timetable timetableFinal = null;
		double scoreFinal;
		Fitness fitnessFinal = null;

		if (parameters.isCalculateFitnessOnly()) {
			fitnessFinal = new Fitness();
			scoreFinal = fitnessFinal.calculate(parameters, timetableOld, members);
			System.out.println(scoreFinal);
		} else {
			for (int i = 1; i <= parameters.getIterations(); i++) {
				Timetable timetableNew = new Timetable(timetableOld);
				timetableNew.mutate();
				Fitness fitnessOld = new Fitness();
				double scoreOld = fitnessOld.calculate(parameters, timetableOld, members);
				Fitness fitnessNew = new Fitness();
				double scoreNew = fitnessNew.calculate(parameters, timetableNew, members);

				if (scoreNew >= scoreOld) {
					timetableOld = timetableNew;
					timetableFinal = timetableNew;
					fitnessFinal = fitnessNew;
					scoreFinal = scoreNew;
				} else {
					timetableFinal = timetableOld;
					fitnessFinal = fitnessOld;
					scoreFinal = scoreOld;
				}

				System.out.println(i + ": " + scoreFinal);
			}

			System.out.println(timetableFinal);
		}

		fitnessFinal.write();
	}
}
