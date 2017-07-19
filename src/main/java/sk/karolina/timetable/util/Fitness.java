package sk.karolina.timetable.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.karolina.timetable.entity.Member;
import sk.karolina.timetable.entity.Timetable;
import sk.karolina.timetable.enums.Level;
import sk.karolina.timetable.io.MembersWriter;
import sk.karolina.timetable.io.Parameters;

public class Fitness {

	private List<Double> programHappinesses;
	private List<Double> timetableHappinesses;

	public Fitness() {
		this.programHappinesses = new ArrayList<>();
		this.timetableHappinesses = new ArrayList<>();
	}

	// najprv vyradim zjavne zle rozvrhy
	// potom pre kazdeho cloveka spocitam ich "vacsie" preferencie
	// sucet vynasobim spokojnostou cloveka s navaznostou a jeho dolezitostou
	// scitam toto pre vsetkych ludi
	// vysledne skore vydelim suctom dolezitosti
	// a este ho vydelim aj maximalnym dosiahnutelnym skore
	// na konci penalizujem rozvrh za nekompletne stvorylky
	public double calculate(Parameters parameters, Timetable timetable, List<Member> members) {
		double fitness = 0;
		int hours = parameters.getHours();

		// penalizuj pripady, kedy RD_PRIPRAVKA a SD_MS_VYUKA nie su pocas
		// prvych dvoch hodin
		Map<Level, Integer> levelCounts = new HashMap<>();

		for (Level level : Level.values()) {
			levelCounts.put(level, 0);
		}

		for (int i = 0; i < 4; i++) {
			Level level = timetable.getLevel(i);
			levelCounts.put(level, levelCounts.get(level) + 1);
		}

		if (parameters.isPripravkaAtBeginning()
				&& (levelCounts.get(Level.RD_PRIPRAVKA) < 1 || levelCounts.get(Level.SD_MS_VYUKA) < 1)) {
			return fitness;
		}

		// penalizuj pripady, kedy sa niektora uroven nachadza viac raz
		for (int i = 4; i < timetable.getSize(); i++) {
			Level level = timetable.getLevel(i);
			levelCounts.put(level, levelCounts.get(level) + 1);
		}

		for (int count : levelCounts.values()) {
			if (count > 1) {
				return fitness;
			}
		}

		// penalizuj pripady, kedy je v obidvoch salach SD alebo RD naraz
		for (int i = 0; i < hours; i++) {
			boolean isRoundMala = timetable.getLevel(i * 2).isRound();
			boolean isRoundVelka = timetable.getLevel(i * 2 + 1).isRound();

			if ((isRoundMala && isRoundVelka) || (!isRoundMala && !isRoundVelka)) {
				return fitness;
			}
		}

		// ak je rozvrh stale viable, spocitaj jeho fitness
		int preference1, preference2, maxPreference, countUnwantedBetweenWanted;
		double memberFitness, totalPenaltyUnwantedBetweenWanted, importancesSum = 0;
		List<Integer> maxPrefs;
		List<Double> squarePrefCounts = new ArrayList<>(Collections.nCopies(4, 0.0));
		boolean wantedBlockStarted, wantedBlockEnded;
		Level level1, level2;

		for (Member member : members) {
			memberFitness = countUnwantedBetweenWanted = 0;
			maxPrefs = new ArrayList<>();
			wantedBlockStarted = wantedBlockEnded = false;

			for (int i = 0; i < hours; i++) {
				level1 = timetable.getLevel(i * 2);
				level2 = timetable.getLevel(i * 2 + 1);
				preference1 = member.getPreference(level1);
				preference2 = member.getPreference(level2);
				maxPreference = preference1 > preference2 ? preference1 : preference2;
				maxPrefs.add(maxPreference);
				memberFitness += maxPreference;

				// chce (a vie) clovek tancovat square viac ako round? (je dost ludi na stvorylku?)
				if ((!level1.isRound() && preference1 >= preference2 && preference1 != parameters.getNEVIEM()
						&& preference1 != parameters.getNECHCEM())
						|| (!level2.isRound() && preference2 >= preference1 && preference2 != parameters.getNEVIEM()
								&& preference2 != parameters.getNECHCEM())) {
					// toto sa sice vola counts, ale realne je to sucet pravdepodobnosti (pravdepodobnost, ze je clovek na klubaci)
					squarePrefCounts.set(i, squarePrefCounts.get(i) + member.getAttendance());
				}
			}

			this.programHappinesses.add(memberFitness);

			// penalizuj pripady, kedy ma clovek dieru v rozvrhu
			// block sa zacina prvym vyskytom wanted programu a konci prvym
			// dalsim vyskytom wanted programu po tom, co bol nejaky unwanted
			// program
			for (int i = 0; i < hours; i++) {
				if (!wantedBlockStarted && !wantedBlockEnded
						&& maxPrefs.get(i) >= parameters.getThresholdWantedPreference()) {
					wantedBlockStarted = true;
					continue;
				}

				if (wantedBlockStarted && !wantedBlockEnded
						&& maxPrefs.get(i) <= parameters.getThresholdUnwantedPreference()) {
					countUnwantedBetweenWanted++;
					continue;
				}

				if (wantedBlockStarted && !wantedBlockEnded && countUnwantedBetweenWanted > 0
						&& maxPrefs.get(i) >= parameters.getThresholdWantedPreference()) {
					wantedBlockEnded = true;
				}
			}

			if (wantedBlockStarted && !wantedBlockEnded) {
				countUnwantedBetweenWanted = 0;
			}

			switch (countUnwantedBetweenWanted) {
			case 0:
				totalPenaltyUnwantedBetweenWanted = 0;
				break;
			case 1:
				totalPenaltyUnwantedBetweenWanted = parameters.getPenalty1UnwantedBetweenWanted();
				break;
			case 2:
				totalPenaltyUnwantedBetweenWanted = parameters.getPenalty1UnwantedBetweenWanted()
						+ parameters.getPenalty2UnwantedBetweenWanted();
				break;
			default:
				throw new IllegalStateException("countUnwantedBetweenWanted was " + countUnwantedBetweenWanted);
			}

			double timetableHappiness = 1 - totalPenaltyUnwantedBetweenWanted;
			memberFitness *= timetableHappiness;
			this.timetableHappinesses.add(timetableHappiness);
			// napr. jirkove tanecne preferencie nas nezaujimaju, kedze 99% casu calleruje
			// podobne preferencie vierky puchlovej nas nezaujimaju, pretoze nebude chodit nezavisle od rozvrhu
			// z tohto hladiska by sme mohli skore cloveka nasobit aj jeho dochadzkou,
			// ale takto by sme mohli diskriminovat ludi, ktori chodia malo, ale kebyze je lepsi rozvrh, chodili by viac
			memberFitness *= member.getImportance();
			fitness += memberFitness;
			importancesSum += member.getImportance();
		}

		fitness /= importancesSum
				* (parameters.getCHCEM1() + parameters.getCHCEM2() + parameters.getCHCEM3() + parameters.getCHCEM4());

		// penalizuj rozvrhy, kde treba stvorylky doplnat z roundovych
		// tanecnikov
		for (double count : squarePrefCounts) {
			if (count <= parameters.getIncompleteSquare()) {
				fitness *= parameters.getPenaltyIncomplete();
				continue;
			}
			if (count <= parameters.getNotEnoughSparesForSquare()) {
				fitness *= parameters.getPenaltyNotEnoughSpares();
			}
		}

		return fitness;
	}

	public void write() throws IOException {
		MembersWriter.write(programHappinesses, timetableHappinesses);
	}
}
