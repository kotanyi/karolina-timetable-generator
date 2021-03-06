package sk.karolina.timetable.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.karolina.timetable.entity.Fitness;
import sk.karolina.timetable.entity.Member;
import sk.karolina.timetable.entity.Timetable;
import sk.karolina.timetable.enums.Level;
import sk.karolina.timetable.io.Parameters;
import sk.karolina.timetable.penalty.ITimetableHolePenaltyStrategy;

public class FitnessCalculator {

	// najprv vyradim zjavne zle rozvrhy
	// potom pre kazdeho cloveka spocitam ich "vacsie" preferencie
	// sucet vynasobim spokojnostou cloveka s navaznostou a jeho dolezitostou
	// scitam toto pre vsetkych ludi
	// vysledne skore vydelim suctom dolezitosti
	// a este ho vydelim aj maximalnym dosiahnutelnym skore
	// na konci penalizujem rozvrh za nekompletne stvorylky
	public static Fitness calculate(Timetable timetable, List<Member> members, ITimetableHolePenaltyStrategy strategy) {
		double fitness = 0;
		int hours = Parameters.getInstance().getHours();
		List<Double> programHappinesses, timetableHappinesses;
		programHappinesses = new ArrayList<>();
		timetableHappinesses = new ArrayList<>();

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

		if (Parameters.getInstance().isPripravkaAtBeginning()
				&& (levelCounts.get(Level.RD_PRIPRAVKA) < 1 || levelCounts.get(Level.SD_MS_VYUKA) < 1)) {
			return new Fitness(0, programHappinesses, timetableHappinesses);
		}

		// penalizuj pripady, kedy sa niektora uroven nachadza viac raz
		for (int i = 4; i < hours * 2; i++) {
			Level level = timetable.getLevel(i);
			levelCounts.put(level, levelCounts.get(level) + 1);
		}

		for (int count : levelCounts.values()) {
			if (count > 1) {
				return new Fitness(0, programHappinesses, timetableHappinesses);
			}
		}

		// penalizuj pripady, kedy sa urcita uroven v rozvrhu vobec nenachadza
		if(Parameters.getInstance().isForceLevels()) {
			for(Level level : Parameters.getInstance().getLevelsToForce()) {
				if(!timetable.contains(level)) {
					return new Fitness(0, programHappinesses, timetableHappinesses);
				}
			}
		}

		boolean isRoundSmall, isRoundLarge, lastYearsBeginnersCannotHelp = false;
		
		for (int i = 0; i < hours; i++) {
			// penalizuj pripady, kedy je v obidvoch salach SD alebo RD naraz
			isRoundSmall = timetable.getLevel(i * 2).isRound();
			isRoundLarge = timetable.getLevel(i * 2 + 1).isRound();

			if ((isRoundSmall && isRoundLarge) || (!isRoundSmall && !isRoundLarge)) {
				return new Fitness(0, programHappinesses, timetableHappinesses);
			}
			
			// penalizuj pripady, kedy minulorocna pripravka nepomaha tohtorocnej
			if(Parameters.getInstance().isLastYearShouldHelpThisYear()) {
				if(timetable.getLevel(i * 2) == Level.SD_MS_VYUKA && (timetable.getLevel(i * 2 + 1) == Level.SD_MS_TANCOVANIE || timetable.getLevel(i * 2 + 1) == Level.RD_LAHKY) ||
						timetable.getLevel(i * 2 + 1) == Level.SD_MS_VYUKA && (timetable.getLevel(i * 2) == Level.SD_MS_TANCOVANIE || timetable.getLevel(i * 2) == Level.RD_LAHKY) ||
						timetable.getLevel(i * 2) == Level.RD_PRIPRAVKA && (timetable.getLevel(i * 2 + 1) == Level.SD_MS_TANCOVANIE || timetable.getLevel(i * 2 + 1) == Level.RD_LAHKY) ||
						timetable.getLevel(i * 2 + 1) == Level.RD_PRIPRAVKA && (timetable.getLevel(i * 2) == Level.SD_MS_TANCOVANIE || timetable.getLevel(i * 2) == Level.RD_LAHKY)) {
							lastYearsBeginnersCannotHelp = true;
				}
			}
		}

		if(lastYearsBeginnersCannotHelp) {
			return new Fitness(0, programHappinesses, timetableHappinesses);
		}

		// ak je rozvrh stale viable, spocitaj jeho fitness
		int preference1, preference2, maxPreference, countUnwantedBetweenWanted, countUnwantedLocal;
		double memberFitness, totalPenaltyUnwantedBetweenWanted, importancesSum = 0, timetableHappiness, importance;
		List<Integer> maxPrefs;
		List<Double> squarePrefCounts = new ArrayList<>(Collections.nCopies(4, 0.0));
		boolean wantedInProgress, unwantedInProgress;
		Level level1, level2;

		for (Member member : members) {
			memberFitness = countUnwantedBetweenWanted = countUnwantedLocal = 0;
			maxPrefs = new ArrayList<>();
			wantedInProgress = unwantedInProgress = false;

			for (int i = 0; i < hours; i++) {
				level1 = timetable.getLevel(i * 2);
				level2 = timetable.getLevel(i * 2 + 1);
				preference1 = member.getPreference(level1);
				preference2 = member.getPreference(level2);
				maxPreference = preference1 > preference2 ? preference1 : preference2;
				maxPrefs.add(maxPreference);
				memberFitness += maxPreference;

				// chce (a vie) clovek tancovat square viac ako round? (je dost ludi na stvorylku?)
				if ((!level1.isRound() && preference1 >= preference2 && preference1 != Parameters.getInstance().getNEVIEM()
						&& preference1 != Parameters.getInstance().getNECHCEM())
						|| (!level2.isRound() && preference2 >= preference1 && preference2 != Parameters.getInstance().getNEVIEM()
								&& preference2 != Parameters.getInstance().getNECHCEM())) {
					// toto sa sice vola counts, ale realne je to sucet pravdepodobnosti (pravdepodobnost, ze je clovek na klubaci)
					squarePrefCounts.set(i, squarePrefCounts.get(i) + member.getAttendance());
				}
			}

			programHappinesses.add(memberFitness);

			// penalizuj pripady, kedy ma clovek dieru v rozvrhu
			for (int i = 0; i < hours; i++) {
				if (maxPrefs.get(i) >= Parameters.getInstance().getThresholdWantedPreference()) {
					if (!wantedInProgress && !unwantedInProgress) {
						wantedInProgress = true;
					} else if (!wantedInProgress && unwantedInProgress) {
						wantedInProgress = true;
						unwantedInProgress = false;
						countUnwantedBetweenWanted += countUnwantedLocal;
						countUnwantedLocal = 0;
					} else if (wantedInProgress && !unwantedInProgress) {
						// do nothing
					} else {
						// should not happen
					}
				} else if (maxPrefs.get(i) <= Parameters.getInstance().getThresholdUnwantedPreference()) {
					if (!wantedInProgress && !unwantedInProgress) {
						// ak ma clovek na zaciatku vecera program, ktory ho
						// nezaujima, tak jednoducho pride neskor
					} else if (!wantedInProgress && unwantedInProgress) {
						countUnwantedLocal++;
					} else if (wantedInProgress && !unwantedInProgress) {
						wantedInProgress = false;
						unwantedInProgress = true;
						countUnwantedLocal++;
					} else {
						// should not happen
					}
				}
			}

			totalPenaltyUnwantedBetweenWanted = strategy.calculatePenalty(countUnwantedBetweenWanted);
			timetableHappiness = 1 - totalPenaltyUnwantedBetweenWanted;
			memberFitness *= timetableHappiness;
			timetableHappinesses.add(timetableHappiness);
			// napr. jirkove tanecne preferencie nas nezaujimaju, kedze 99% casu calleruje
			// podobne preferencie vierky puchlovej nas nezaujimaju, pretoze nebude chodit nezavisle od rozvrhu
			// z tohto hladiska by sme mohli skore cloveka nasobit aj jeho dochadzkou,
			// ale takto by sme mohli diskriminovat ludi, ktori chodia malo, ale kebyze je lepsi rozvrh, chodili by viac
			importance = member.getImportance();
			memberFitness *= importance;
			fitness += memberFitness;
			importancesSum += importance;
		}

		fitness /= importancesSum
				* (Parameters.getInstance().getCHCEM1() + Parameters.getInstance().getCHCEM2() + Parameters.getInstance().getCHCEM3() + Parameters.getInstance().getCHCEM4());

		// penalizuj rozvrhy, kde treba stvorylky doplnat z roundovych
		// tanecnikov
		for (double count : squarePrefCounts) {
			if (count <= Parameters.getInstance().getIncompleteSquare()) {
				fitness *= Parameters.getInstance().getPenaltyIncomplete();
				continue;
			}
			if (count <= Parameters.getInstance().getNotEnoughSparesForSquare()) {
				fitness *= Parameters.getInstance().getPenaltyNotEnoughSpares();
			}
		}

		return new Fitness(fitness, programHappinesses, timetableHappinesses);
	}
}
