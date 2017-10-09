package sk.karolina.timetable.io;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import sk.karolina.timetable.entity.Fitness;

public class MembersWriter {

	public static void write(Fitness fitness) throws IOException {
		FileWriter writer = new FileWriter("happinesses.csv");
		CSVPrinter printer = CSVFormat.DEFAULT.print(writer);

		for (int i = 0; i < fitness.getProgramHappinesses().size(); i++) {
			printer.printRecord(fitness.getProgramHappinesses().get(i), fitness.getTimetableHappinesses().get(i));
		}

		writer.close();
	}
}
