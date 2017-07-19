package sk.karolina.timetable.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class MembersWriter {

	public static void write(List<Double> programHappinesses, List<Double> timetableHappinesses) throws IOException {
		FileWriter writer = new FileWriter("happinesses.csv");
		CSVPrinter printer = CSVFormat.DEFAULT.print(writer);
		
		for(int i = 0; i < programHappinesses.size(); i++) {
			printer.printRecord(programHappinesses.get(i), timetableHappinesses.get(i));
		}
		
		writer.close();
	}
}
