package com.example.panke.fakesuperclass_client.dbutil;


import com.example.panke.fakesuperclass_client.bean.Course;
import com.example.panke.fakesuperclass_client.bean.Schedule;
import com.example.panke.fakesuperclass_client.bean.Semester;
import com.example.panke.fakesuperclass_client.bean.Teacher;

import java.util.List;
import java.util.Map;

/**
 * Created by sbml1 on 2017/3/25.
 */

public interface Manager {

    public void saveTeacher(Teacher teacher);
    public void saveCourse(Course course);
    public void saveSchedule(Schedule schedule);
    public String findTId(String tName);
    public boolean hasTeacherData(String tId);
    public Teacher findTeacher(String tId);
    public List<String> findAllTeacherName();
    public List<Course> findCourse(String cId);
    public List<Schedule> findSchedules(String tId);
    public void saveSemester(Semester semester);
    public Map<String,String> findAllSemesters();
    public void saveTimeStamp(long timeStamp);
    public long findNewestTimeStamp();
}
