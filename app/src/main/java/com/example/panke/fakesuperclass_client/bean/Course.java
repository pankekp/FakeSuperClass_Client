package com.example.panke.fakesuperclass_client.bean;

/**
 * @author Zhao Zhixu
 */
public class Course {
	String CourseId;	
	String CourseName;	
	String Credit;		
	String Method;		
	String Type;		
	
	public String getCourseId() {
		return CourseId;
	}
	public void setCourseId(String courseId) {
		CourseId = courseId;
	}
	public String getCourseName() {
		return CourseName;
	}
	public void setCourseName(String courseName) {
		CourseName = courseName;
	}
	public String getCredit() {
		return Credit;
	}
	public void setCredit(String credit) {
		Credit = credit;
	}
	public String getMethod() {
		return Method;
	}
	public void setMethod(String method) {
		Method = method;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}	
}
