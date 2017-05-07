package com.example.panke.fakesuperclass_client.bean;

import java.util.Map;

public class InitInfo {
	long TimeStamp;
	Map<String, String> TeacherMap;
	Map<String, String> SemesterMap;
	public long getTimeStamp() {
		return TimeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		TimeStamp = timeStamp;
	}
	public Map<String, String> getTeacherMap() {
		return TeacherMap;
	}
	public void setTeacherMap(Map<String, String> teacherMap) {
		TeacherMap = teacherMap;
	}
	public Map<String, String> getSemesterMap() {
		return SemesterMap;
	}
	public void setSemesterMap(Map<String, String> semesterMap) {
		SemesterMap = semesterMap;
	}
}
