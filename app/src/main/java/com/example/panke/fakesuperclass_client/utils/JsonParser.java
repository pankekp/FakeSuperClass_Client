package com.example.panke.fakesuperclass_client.utils;

import com.example.panke.fakesuperclass_client.bean.Course;
import com.example.panke.fakesuperclass_client.bean.InitInfo;
import com.example.panke.fakesuperclass_client.bean.Record;
import com.example.panke.fakesuperclass_client.bean.Schedule;

import net.iharder.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Parse JSON string to Record, teacher list, semester list, 
 * or validation code image
 * 
 * @author zhixu
 *
 */
public class JsonParser {
	/**
	 * Get the type name of a JSON text
	 * @param jsontext
	 * @return the type name string of a JSON text
	 */
	public String typeOf(String jsontext) throws JSONException{
		JSONObject jsonobj = new JSONObject(jsontext);
		String type = null;
		type = jsonobj.getString("type");

		return type;
	}
	
	/**
	 * Get a Record object from a JSON text
	 * @param jsontext
	 * @return a Record object, or null if the text is not of Record type.
	 * @throws JSONException
	 */
	public Record parseRecord(String jsontext) throws JSONException{
		JSONObject jsonObj = new JSONObject(jsontext);
		String type = jsonObj.getString("type");
		if (!"record".equals(type))
			return null;
		
		Record resRecord = new Record();
		resRecord.setCourseList(new ArrayList<Course>());
		resRecord.setScheduleList(new ArrayList<Schedule>());
		JSONObject jsonRecord = jsonObj.getJSONObject("record");
		JSONArray jsonCourses = jsonRecord.getJSONArray("courseList");
		JSONArray jsonSchedules = jsonRecord.getJSONArray("scheduleList");
		
		for (int i = 0; i < jsonCourses.length(); i++) {
			JSONObject jsonCourse;
			Course course = new Course();
			jsonCourse = jsonCourses.getJSONObject(i);
			course.setCourseId(jsonCourse.getString("courseId"));
			course.setCourseName(jsonCourse.getString("courseName"));
			course.setCredit(jsonCourse.getString("credit"));
			course.setMethod(jsonCourse.getString("method"));
			course.setType(jsonCourse.getString("type"));
			resRecord.getCourseList().add(course);
		}
		
		for (int i = 0; i < jsonSchedules.length(); i++) {
			JSONObject jsonSchedule;
			Schedule schedule = new Schedule();
			jsonSchedule = jsonSchedules.getJSONObject(i);
			schedule.setScheduleId(jsonSchedule.getInt("scheduleId"));
			schedule.setClasses(jsonSchedule.getString("classes"));
			schedule.setClassNo(jsonSchedule.getString("classNo"));
			schedule.setPeopleNum(jsonSchedule.getString("peopleNum"));
			schedule.setTime(jsonSchedule.getString("time"));
			schedule.setLocation(jsonSchedule.getString("location"));
			schedule.setTeacherId(jsonSchedule.getString("teacherId"));
			schedule.setCourseId(jsonSchedule.getString("courseId"));
			resRecord.getScheduleList().add(schedule);
		}		
		
		return resRecord;
	}
	
	/**
	 * get an InitInfo object froma JSON string.
	 * @param jsontext
	 * @return an InitInfo object, or null if jsontext is not of type InitInfo
	 * @throws JSONException
	 */
	public InitInfo parseInitInfo(String jsontext) throws JSONException {
		JSONObject jsonObj = new JSONObject(jsontext);
		String type = jsonObj.getString("type");
		if (!"initInfo".equals(type))
			return null;
		
		InitInfo initInfo = new InitInfo();
		initInfo.setTimeStamp(jsonObj.getLong("timeStamp"));
		JSONArray teachers = jsonObj.getJSONArray("teacherMap");
		JSONArray semesters = jsonObj.getJSONArray("semesterMap");		
		
		Map<String, String> teacherMap = new TreeMap<String, String>();
		for (int i = 0; i < teachers.length(); i++) {
			JSONObject teacher = teachers.getJSONObject(i);
			teacherMap.put(teacher.getString("teacherId"), teacher.getString("teacherName"));
		}
		initInfo.setTeacherMap(teacherMap);		
		
		Map<String, String> semesterMap= new TreeMap<String, String>();
		for (int i = 0; i < semesters.length(); i++) {
			JSONObject semester = semesters.getJSONObject(i);//TODO: use a List, not a Map
			semesterMap.put(semester.getString("semesterId"), semester.getString("semesterName"));
		}
		initInfo.setSemesterMap(semesterMap);
		
		return initInfo;
	}
	
	/**
	 * Get a teacher map Map<TeacherId, TeacherName> form a JSON text
	 * @param jsontext
	 * @return a Treemap, or null if the text is not of TeacherMap type
	 * @throws JSONException
	 */
	public Map<String, String> parseTeacherMap(String jsontext) throws JSONException {
		JSONObject jsonObj = new JSONObject(jsontext);
		String type = jsonObj.getString("type");
		if (!"teacherMap".equals(type))
			return null;
		
		Map<String, String> teacherMap = new TreeMap<String, String>();
		JSONArray teachers = jsonObj.getJSONArray("teacherMap");
		
		for (int i = 0; i < teachers.length(); i++) {
			JSONObject teacher = teachers.getJSONObject(i);
			teacherMap.put(teacher.getString("teacherId"), teacher.getString("teacherName"));
		}
		
		return teacherMap;
	}
	
	/**
	 * Get a semester map Map<SemesterId, SemesterName> from a JSON text
	 * @param jsontext
	 * @return a TreeMap, or null if the text is not of SemesterMap type
	 * @throws JSONException
	 */
	public Map<String, String> parseSemesterMap(String jsontext) throws JSONException {
		JSONObject jsonObj = new JSONObject(jsontext);
		String type = jsonObj.getString("type");
		if (!"semesterMap".equals(type))
			return null;
		
		Map<String, String> semesterMap= new TreeMap<String, String>();
		JSONArray semesters = jsonObj.getJSONArray("semesterMap");
		
		for (int i = 0; i < semesters.length(); i++) {
			JSONObject semester = semesters.getJSONObject(i);//TODO: use a List, not a Map
			semesterMap.put(semester.getString("semesterId"), semester.getString("semesterName"));
		}
		
		return semesterMap;
	}	
	
	/**
	 * Parse a JSON string encoding an image to be decoded 
	 * @param jsontext
	 * @param vcpath path where output image is to be stored
	 * @return cookie returned by server
	 * @throws JSONException
	 * @throws IOException
	 */
	public String parseVcImage(String jsontext, String vcpath) throws JSONException, IOException {
		JSONObject jsonobj = new JSONObject(jsontext);
		String type = jsonobj.getString("type");
		if (!"vcImage".equals(type))
			return null;
		
		String vcStr = jsonobj.getString("vcImage");
		byte[] vcbyte = Base64.decode(vcStr);
		
		File file = new File(vcpath);
		ByteArrayInputStream bais = new ByteArrayInputStream(vcbyte);
		FileOutputStream fout = new FileOutputStream(file);
		byte[] buffer = new byte[4096];
		int len = -1;
		while ((len = bais.read(buffer)) != -1) {
			fout.write(buffer, 0, len);
		}
		
		bais.close();
		fout.close();
		return jsonobj.getString("cookie");
	}
}
