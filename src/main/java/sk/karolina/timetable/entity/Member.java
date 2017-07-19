package sk.karolina.timetable.entity;

import java.util.HashMap;
import java.util.Map;

import sk.karolina.timetable.enums.Level;

public class Member {
	private String name;
	private Map<Level, Integer> preferences;
	private double importance;
	private double attendance;

	public Member() {
		preferences = new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPreference(Level level) {
		return preferences.get(level);
	}

	public void setPreference(Level level, int preference) {
		preferences.put(level, preference);
	}

	public double getImportance() {
		return importance;
	}

	public void setImportance(double importance) {
		this.importance = importance;
	}

	public double getAttendance() {
		return attendance;
	}

	public void setAttendance(double attendance) {
		this.attendance = attendance;
	}
}
