package sk.karolina.timetable.io;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import sk.karolina.timetable.entity.Member;
import sk.karolina.timetable.enums.Level;

public class MembersReader {
	
	private Parameters parameters;
	
	public MembersReader(Parameters parameters) {
		this.parameters = parameters;
	}
	
	public List<Member> read() throws IOException {
		Reader reader = new FileReader("members.csv");
		Iterable<CSVRecord> records = CSVFormat.EXCEL.parse(reader);
		List<Member> members = new ArrayList<Member>();
		
		for(CSVRecord record : records) {
			Member member = new Member();
    		member.setName(record.get(0));
    		member.setPreference(Level.RD_PRIPRAVKA, mapPreferenceStringToInt(record.get(1)));
    		member.setPreference(Level.RD_LAHKY, mapPreferenceStringToInt(record.get(2)));
    		member.setPreference(Level.RD_STREDNY, mapPreferenceStringToInt(record.get(3)));
    		member.setPreference(Level.RD_TAZKY, mapPreferenceStringToInt(record.get(4)));
    		member.setPreference(Level.SD_MS_VYUKA, mapPreferenceStringToInt(record.get(5)));
    		member.setPreference(Level.SD_MS_TANCOVANIE, mapPreferenceStringToInt(record.get(6)));
    		member.setPreference(Level.SD_PLUS_VYUKA, mapPreferenceStringToInt(record.get(7)));
    		member.setPreference(Level.SD_PLUS_TANCOVANIE, mapPreferenceStringToInt(record.get(8)));
    		member.setPreference(Level.SD_A1_VYUKA, mapPreferenceStringToInt(record.get(9)));
    		member.setPreference(Level.SD_A1_TANCOVANIE, mapPreferenceStringToInt(record.get(10)));
    		member.setPreference(Level.SD_A2_VYUKA, mapPreferenceStringToInt(record.get(11)));
    		member.setPreference(Level.SD_A2_TANCOVANIE, mapPreferenceStringToInt(record.get(12)));
    		member.setImportance(Double.parseDouble(record.get(13)));
    		member.setAttendance(Double.parseDouble(record.get(14)));
    		members.add(member);
		}
    	
    	return members;
	}
	
	private int mapPreferenceStringToInt(String preference) {
		switch (preference) {
		case "chcem priorita 1":
			return this.parameters.getCHCEM1();
		case "chcem priorita 2":
			return this.parameters.getCHCEM2();
		case "chcem priorita 3":
			return this.parameters.getCHCEM3();
		case "chcem priorita 4":
			return this.parameters.getCHCEM4();
		case "rád(a)":
			return this.parameters.getRAD();
		case "môžem":
			return this.parameters.getMOZEM();
		case "nechcem":
			return this.parameters.getNECHCEM();
		case "neviem":
			return this.parameters.getNEVIEM();
		default:
			throw new IllegalArgumentException("Illegal preference '" + preference + "'");
		}
	}
}
