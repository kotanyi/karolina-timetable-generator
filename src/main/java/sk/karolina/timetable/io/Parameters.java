package sk.karolina.timetable.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import sk.karolina.timetable.enums.Level;

public class Parameters {
	private boolean calculateFitnessOnly;
	private int iterations, hours, maxPointMutations;
	private boolean startFromCurrentTimetable;
	private List<Level> currentTimetable;
	private int timetableHolePenaltyStrategy;
	private int thresholdUnwantedPreference, thresholdWantedPreference;
	private double penalty1UnwantedBetweenWanted, penalty2UnwantedBetweenWanted;
	private double logisticFunctionK, logisticFunctionXNaught;
	private boolean isPripravkaAtBeginning;
	private int notEnoughSparesForSquare, incompleteSquare;
	private double penaltyNotEnoughSpares, penaltyIncomplete;
	private int CHCEM1, CHCEM2, CHCEM3, CHCEM4, RAD, MOZEM, NECHCEM, NEVIEM;

	public Parameters() throws IOException {
		FileInputStream stream = new FileInputStream("parameters.properties");
		Properties properties = new Properties();
		properties.load(stream);
		stream.close();

		calculateFitnessOnly = Boolean.parseBoolean(properties.getProperty("calculateFitnessOnly"));
		iterations = Integer.parseInt(properties.getProperty("iterations"));
		hours = Integer.parseInt(properties.getProperty("hours"));
		maxPointMutations = Integer.parseInt(properties.getProperty("maxPointMutations"));
		startFromCurrentTimetable = Boolean.parseBoolean(properties.getProperty("startFromCurrentTimetable"));
		currentTimetable = Arrays.asList(properties.getProperty("currentTimetable").split(", ")).stream()
				.map(string -> Level.valueOf(string)).collect(Collectors.toList());

		timetableHolePenaltyStrategy = Integer.parseInt(properties.getProperty("timetableHolePenaltyStrategy"));
		thresholdUnwantedPreference = Integer.parseInt(properties.getProperty("thresholdUnwantedPreference"));
		thresholdWantedPreference = Integer.parseInt(properties.getProperty("thresholdWantedPreference"));
		penalty1UnwantedBetweenWanted = Double.parseDouble(properties.getProperty("penalty1UnwantedBetweenWanted"));
		penalty2UnwantedBetweenWanted = Double.parseDouble(properties.getProperty("penalty2UnwantedBetweenWanted"));
		logisticFunctionK = Double.parseDouble(properties.getProperty("logisticFunctionK"));
		logisticFunctionXNaught = Double.parseDouble(properties.getProperty("logisticFunctionXNaught"));
		isPripravkaAtBeginning = Boolean.parseBoolean(properties.getProperty("isPripravkaAtBeginning"));
		notEnoughSparesForSquare = Integer.parseInt(properties.getProperty("notEnoughSparesForSquare"));
		incompleteSquare = Integer.parseInt(properties.getProperty("incompleteSquare"));
		penaltyNotEnoughSpares = Double.parseDouble(properties.getProperty("penaltyNotEnoughSpares"));
		penaltyIncomplete = Double.parseDouble(properties.getProperty("penaltyIncomplete"));

		CHCEM1 = Integer.parseInt(properties.getProperty("CHCEM1"));
		CHCEM2 = Integer.parseInt(properties.getProperty("CHCEM2"));
		CHCEM3 = Integer.parseInt(properties.getProperty("CHCEM3"));
		CHCEM4 = Integer.parseInt(properties.getProperty("CHCEM4"));
		RAD = Integer.parseInt(properties.getProperty("RAD"));
		MOZEM = Integer.parseInt(properties.getProperty("MOZEM"));
		NECHCEM = Integer.parseInt(properties.getProperty("NECHCEM"));
		NEVIEM = Integer.parseInt(properties.getProperty("NEVIEM"));
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
}
