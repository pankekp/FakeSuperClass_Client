package com.example.panke.fakesuperclass_client.bean;

/**
 * @author Zhao Zhixu
 */
public class Schedule {
	int ScheduleId;	
	String ClassNo;	
	String Classes;	
	String PeopleNum;
	String Time;	
	String Location;	
	String TeacherId;	
	String CourseId;
	String Semester;
	
	public int getScheduleId() {
		return ScheduleId;
	}
	public void setScheduleId(int scheduleId) {
		ScheduleId = scheduleId;
	}
	public String getClassNo() {
		return ClassNo;
	}
	public void setClassNo(String classNo) {
		ClassNo = classNo;
	}
	public String getClasses() {
		return Classes;
	}
	public void setClasses(String classes) {
		Classes = classes;
	}
	public String getPeopleNum() {
		return PeopleNum;
	}
	public void setPeopleNum(String peopleNum) {
		PeopleNum = peopleNum;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getLocation() {
		return Location;
	}
	public void setLocation(String location) {
		Location = location;
	}
	public String getTeacherId() {
		return TeacherId;
	}
	public void setTeacherId(String teacherId) {
		TeacherId = teacherId;
	}
	public String getCourseId() {
		return CourseId;
	}
	public void setCourseId(String courseId) {
		CourseId = courseId;
	}
	public String getSemester() {
		return Semester;
	}
	public void setSemester(String semester) {
		Semester = semester;
	}
}
