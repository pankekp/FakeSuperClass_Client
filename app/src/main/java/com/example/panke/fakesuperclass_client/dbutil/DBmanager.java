package com.example.panke.fakesuperclass_client.dbutil;


import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.example.panke.fakesuperclass_client.bean.Course;
import com.example.panke.fakesuperclass_client.bean.Schedule;
import com.example.panke.fakesuperclass_client.bean.Semester;
import com.example.panke.fakesuperclass_client.bean.Teacher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sbml1 on 2017/3/25.
 */

public class DBmanager implements Manager{
    private CourseDbHelper helper;
    private SQLiteDatabase db;
    public DBmanager(CourseDbHelper helper,SQLiteDatabase db) {
        this.helper=helper;
        this.db=db;

    }

    @Override
    public void saveTeacher(Teacher teacher){
        Object[] obj=new Object[]{teacher.getTeacherId(),teacher.getTeacherName()};
        String sql="insert into teachers (tId,tName) values(?,?)";
        try {
            db.execSQL(sql,obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void saveCourse(Course course){
        Object[] obj=new Object[]{course.getCourseId(),course.getCourseName(),course.getCredit(),course.getMethod(),course.getType()};
        String sql="insert into courses (cId,cName,credit,method,type) values(?,?,?,?,?)";
        try {
            db.execSQL(sql,obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void saveSchedule(Schedule schedule){
        Object[] obj=new Object[]{schedule.getClassNo(),schedule.getClasses(),schedule.getPeopleNum(),schedule.getTime(),schedule.getLocation(),schedule.getTeacherId(),schedule.getCourseId()};
        String sql="insert into schedules(sId,classNo,classes,peopleNum,time,location,tId,cId) values(null,?,?,?,?,?,?,?)";
        try {
            db.execSQL(sql,obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String findTId(String tName){
        String TId=null;
        String sql="select tId from teachers where tName=?";
        try {
            Cursor c = db.rawQuery(sql,new String[]{tName});
            while (c.moveToNext()){
                TId=TId+","+c.getString(c.getColumnIndex("tId"));
            }
            TId=TId.substring(5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return TId;
    }
    @Override
    public boolean hasTeacherData(String tId){
        boolean hasData=false;
        String sql="select sId from schedules where tId=?";
        try {
            Cursor c = db.rawQuery(sql,new String[]{tId});
            while (c.moveToNext()){
                hasData=true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasData;
    }
    @Override
    public Teacher findTeacher(String tId){
        Teacher teacher=new Teacher();
        String sql="select tId,tName from teachers where tId=?";
        try {
            Cursor c = db.rawQuery(sql,new String[]{tId});
            while (c.moveToNext()){
                teacher.setTeacherId(tId);
                teacher.setTeacherId(c.getString(c.getColumnIndex("tName")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return teacher;
    }

    @Override
    public List<String> findAllTeacherName() {
        List<String> TeacherNameList=new ArrayList<>();
        String sql="select tName from teachers";
        try {
            Cursor c = db.rawQuery(sql,new String[]{});
            while (c.moveToNext()){
                TeacherNameList.add(c.getString(c.getColumnIndex("tName")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return TeacherNameList;
    }

    @Override
    public List<Course> findCourse(String cId){
        int i=0;
        List<Course> courseList=new ArrayList<>();
        Course course=new Course();
        String sql="select cId,cName,credit,method,type from courses where cId=?";
        try {
            Cursor c = db.rawQuery(sql,new String[]{cId});
            while (c.moveToNext()){
                course.setCourseId(cId);
                course.setCourseName(c.getString(c.getColumnIndex("cName")));
                course.setCredit(c.getString(c.getColumnIndex("credit")));
                course.setMethod(c.getString(c.getColumnIndex("method")));
                course.setType(c.getString(c.getColumnIndex("type")));
                courseList.add(i++,course);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return courseList;
    }
    @Override
    public List<Schedule> findSchedules(String tId){
        int i=0;
        List<Schedule> schedules=new ArrayList<Schedule>();
        Schedule schedule=new Schedule();
        String sql="select sId,classNo,classes,peopleNum,time,location,tId,cId from schedules where tId=?";
        try {
            Cursor c = db.rawQuery(sql,new String[]{tId});
            while (c.moveToNext()){
                schedule.setScheduleId(c.getInt(c.getColumnIndex("sId")));
                schedule.setClassNo(c.getString(c.getColumnIndex("classNo")));
                schedule.setClasses(c.getString(c.getColumnIndex("classes")));
                schedule.setPeopleNum(c.getString(c.getColumnIndex("peopleNum")));
                schedule.setTime(c.getString(c.getColumnIndex("time")));
                schedule.setLocation(c.getString(c.getColumnIndex("location")));
                schedule.setTeacherId(tId);
                schedule.setCourseId(c.getString(c.getColumnIndex("cId")));
                schedules.add(i++,schedule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return schedules;
    }

    @Override
    public void saveSemester(Semester semester) {
        Object[] obj=new Object[]{semester.getSemesterId(),semester.getSemesterName()};
        String sql="insert into semesters (SemesterId,SemesterName) values(?,?)";
        try {
            db.execSQL(sql,obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> findAllSemesters() {
        Map<String, String> semesters=new HashMap<>();
        String sql="select SemesterId,SemesterName from semesters";
        try {
            Cursor c=db.rawQuery(sql,new String[]{});
            while(c.moveToNext()){
                String SemesterId=c.getString(c.getColumnIndex("SemesterId"));
                String SemesterName=c.getString(c.getColumnIndex("SemesterName"));
                semesters.put(SemesterId,SemesterName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return semesters;
    }

    @Override
    public void saveTimeStamp(long timeStamp) {
        Object[] obj=new Object[]{timeStamp};
        String sql="insert into timestamps (TimeStampId,timeStamp) values(null,?)";
        try {
            db.execSQL(sql,obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long findNewestTimeStamp() {
        long timeStamp=0;
        int timeStampId=0;
        String sql="select TimeStampId,timeStamp from timestamps";
        try {
            Cursor c=db.rawQuery(sql,new String[]{});
            while(c.moveToNext()){
                if(timeStampId<c.getInt(c.getColumnIndex("TimeStampId")))  {
                    timeStamp=c.getColumnIndex("timeStamp");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeStamp;
    }
}
