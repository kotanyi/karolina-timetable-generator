package sk.karolina.timetable.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import sk.karolina.timetable.enums.Level;

public final class Parameters {

	private static final Parameters INSTANCE;

	private boolean calculateFitnessOnly;
	private int iterations, hours, maxPointMutations;
	private boolean startFromCurrentTimetable;
	private List<Level> currentTimetable;
	private boolean forceLevels;
	private List<Level> levelsToForce;
	private boolean lastYearShouldHelpThisYear;
	private int timetableHolePenaltyStrategy;
	private int thresholdUnwantedPreference, thresholdWantedPreference;
	private double penalty1UnwantedBetweenWanted, penalty2UnwantedBetweenWanted;
	private double logisticFunctionK, logisticFunctionXNaught;
	private boolean isPripravkaAtBeginning;
	private int notEnoughSparesForSquare, incompleteSquare;
	private double penaltyNotEnoughSpares, penaltyIncomplete;
	private int CHCEM1, CHCEM2, CHCEM3, CHCEM4, RAD, MOZEM, NECHCEM, NEVIEM;

	static {
		try {
			INSTANCE = new Parameters();
		} catch (IOException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	private Parameters() throws IOException {
		FileInputStream stream = new FileInputStream("parameters.properties");
		Properties properties = new Properties();
		properties.load(stream);
		stream.close();

		calculateFitnessOnly = Boolean.parseBoolean(properties.getProperty(Parameter.calculateFitnessOnly.toString()));
		iterations = Integer.parseInt(properties.getProperty(Parameter.iterations.toString()));
		hours = Integer.parseInt(properties.getProperty(Parameter.hours.toString()));
		maxPointMutations = Integer.parseInt(properties.getProperty(Parameter.maxPointMutations.toString()));
		startFromCurrentTimetable = Boolean
				.parseBoolean(properties.getProperty(Parameter.startFromCurrentTimetable.toString()));
		currentTimetable = Arrays.asList(properties.getProperty(Parameter.currentTimetable.toString()).split(", "))
				.stream().map(string -> Level.valueOf(string)).collect(Collectors.toList());
		forceLevels = Boolean.parseBoolean(properties.getProperty(Parameter.forceLevels.toString()));

		if (properties.getProperty(Parameter.levelsToForce.toString()).equals("")) {
			levelsToForce = new ArrayList<>();
		} else {
			levelsToForce = Arrays.asList(properties.getProperty(Parameter.levelsToForce.toString()).split(", "))
					.stream().map(string -> Level.valueOf(string)).collect(Collectors.toList());
		}

		lastYearShouldHelpThisYear = Boolean
				.parseBoolean(properties.getProperty(Parameter.lastYearShouldHelpThisYear.toString()));

		timetableHolePenaltyStrategy = Integer
				.parseInt(properties.getProperty(Parameter.timetableHolePenaltyStrategy.toString()));
		thresholdUnwantedPreference = Integer
				.parseInt(properties.getProperty(Parameter.thresholdUnwantedPreference.toString()));
		thresholdWantedPreference = Integer
				.parseInt(properties.getProperty(Parameter.thresholdWantedPreference.toString()));
		penalty1UnwantedBetweenWanted = Double
				.parseDouble(properties.getProperty(Parameter.penalty1UnwantedBetweenWanted.toString()));
		penalty2UnwantedBetweenWanted = Double
				.parseDouble(properties.getProperty(Parameter.penalty2UnwantedBetweenWanted.toString()));
		logisticFunctionK = Double.parseDouble(properties.getProperty(Parameter.logisticFunctionK.toString()));
		logisticFunctionXNaught = Double
				.parseDouble(properties.getProperty(Parameter.logisticFunctionXNaught.toString()));
		isPripravkaAtBeginning = Boolean
				.parseBoolean(properties.getProperty(Parameter.isPripravkaAtBeginning.toString()));
		notEnoughSparesForSquare = Integer
				.parseInt(properties.getProperty(Parameter.notEnoughSparesForSquare.toString()));
		incompleteSquare = Integer.parseInt(properties.getProperty(Parameter.incompleteSquare.toString()));
		penaltyNotEnoughSpares = Double.parseDouble(properties.getProperty(Parameter.penaltyNotEnoughSpares.toString()));
		penaltyIncomplete = Double.parseDouble(properties.getProperty(Parameter.penaltyIncomplete.toString()));

		CHCEM1 = Integer.parseInt(properties.getProperty(Parameter.CHCEM1.toString()));
		CHCEM2 = Integer.parseInt(properties.getProperty(Parameter.CHCEM2.toString()));
		CHCEM3 = Integer.parseInt(properties.getProperty(Parameter.CHCEM3.toString()));
		CHCEM4 = Integer.parseInt(properties.getProperty(Parameter.CHCEM4.toString()));
		RAD = Integer.parseInt(properties.getProperty(Parameter.RAD.toString()));
		MOZEM = Integer.parseInt(properties.getProperty(Parameter.MOZEM.toString()));
		NECHCEM = Integer.parseInt(properties.getProperty(Parameter.NECHCEM.toString()));
		NEVIEM = Integer.parseInt(properties.getProperty(Parameter.NEVIEM.toString()));
	}

	public static Parameters getInstance() {
		return INSTANCE;
	}

	public boolean isCalculateFitnessOnly() {
		return calculateFitnessOnly;
	}

	public int getIterations() {
		return iterations;
	}

	public int getHours() {
		return hours;
	}

	public int getMaxPointMutations() {
		return maxPointMutations;
	}

	public boolean isStartFromCurrentTimetable() {
		return startFromCurrentTimetable;
	}

	public List<Level> getCurrentTimetable() {
		return currentTimetable;
	}

	public boolean isForceLevels() {
		return forceLevels;
	}

	public List<Level> getLevelsToForce() {
		return levelsToForce;
	}

	public boolean isLastYearShouldHelpThisYear() {
		return lastYearShouldHelpThisYear;
	}

	public int getTimetableHolePenaltyStrategy() {
		return timetableHolePenaltyStrategy;
	}

	public int getThresholdUnwantedPreference() {
		return thresholdUnwantedPreference;
	}

	public int getThresholdWantedPreference() {
		return thresholdWantedPreference;
	}

	public double getPenalty1UnwantedBetweenWanted() {
		return penalty1UnwantedBetweenWanted;
	}

	public double getPenalty2UnwantedBetweenWanted() {
		return penalty2UnwantedBetweenWanted;
	}

	public double getLogisticFunctionK() {
		return logisticFunctionK;
	}

	public double getLogisticFunctionXNaught() {
		return logisticFunctionXNaught;
	}

	public boolean isPripravkaAtBeginning() {
		return isPripravkaAtBeginning;
	}

	public int getNotEnoughSparesForSquare() {
		return notEnoughSparesForSquare;
	}

	public int getIncompleteSquare() {
		return incompleteSquare;
	}

	public double getPenaltyNotEnoughSpares() {
		return penaltyNotEnoughSpares;
	}

	public double getPenaltyIncomplete() {
		return penaltyIncomplete;
	}

	public int getCHCEM1() {
		return CHCEM1;
	}

	public int getCHCEM2() {
		return CHCEM2;
	}

	public int getCHCEM3() {
		return CHCEM3;
	}

	public int getCHCEM4() {
		return CHCEM4;
	}

	public int getRAD() {
		return RAD;
	}

	public int getMOZEM() {
		return MOZEM;
	}

	public int getNECHCEM() {
		return NECHCEM;
	}

	public int getNEVIEM() {
		return NEVIEM;
	}

	private static enum Parameter {
		calculateFitnessOnly, iterations, hours, maxPointMutations, startFromCurrentTimetable, currentTimetable, forceLevels, levelsToForce, lastYearShouldHelpThisYear,

		timetableHolePenaltyStrategy, thresholdUnwantedPreference, thresholdWantedPreference, penalty1UnwantedBetweenWanted, penalty2UnwantedBetweenWanted, logisticFunctionK, logisticFunctionXNaught, isPripravkaAtBeginning, notEnoughSparesForSquare, incompleteSquare, penaltyNotEnoughSpares, penaltyIncomplete,

		CHCEM1, CHCEM2, CHCEM3, CHCEM4, RAD, MOZEM, NECHCEM, NEVIEM;
	}
}
