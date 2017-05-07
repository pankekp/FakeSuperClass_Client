package com.example.panke.fakesuperclass_client.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.panke.fakesuperclass_client.R;
import com.example.panke.fakesuperclass_client.bean.Course;
import com.example.panke.fakesuperclass_client.bean.Record;
import com.example.panke.fakesuperclass_client.bean.Schedule;
import com.example.panke.fakesuperclass_client.utils.JsonParser;

import org.json.JSONException;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by sunzehua
 */

public class SuperCourseActivity extends AppCompatActivity {

    /** 第一个无内容的格子 */
    protected TextView empty;
    /** 星期一的格子 */
    protected TextView monColum;
    /** 星期二的格子 */
    protected TextView tueColum;
    /** 星期三的格子 */
    protected TextView wedColum;
    /** 星期四的格子 */
    protected TextView thrusColum;
    /** 星期五的格子 */
    protected TextView friColum;
    /** 星期六的格子 */
    protected TextView satColum;
    /** 星期日的格子 */
    protected TextView sunColum;
    /** 课程表body部分布局 */
    protected RelativeLayout course_table_layout;
    /** 屏幕宽度 **/
    protected int screenWidth;
    /** 课程格子平均宽度 **/
    protected int aveWidth;
    protected int gridHight;
    protected final int[] background = {R.drawable.course_info_blue, R.drawable.course_info_green,
            R.drawable.course_info_red, R.drawable.course_info_red,
            R.drawable.course_info_yellow};
    protected Record record;
    protected List<Schedule> scheduleList;
    protected List<Course>  courseList;
    protected  String csId="";
    protected  String csName="";
    protected  String csCredit="";
    protected  String csMethod="";
    protected  String csType="";
    protected  final  String  chiToInt=new String("一二三四五六日");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_course);
        init();
        String json=getIntent().getStringExtra("json");
        JsonParser jsonParser=new JsonParser();

        try {
            record=jsonParser.parseRecord(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        scheduleList=record.getScheduleList();
        courseList=record.getCourseList();

        for(Schedule sd:scheduleList){
            show(sd);
        }

    }
    public void init(){
        //获得列头的控件
        empty = (TextView) this.findViewById(R.id.test_empty);
        monColum = (TextView) this.findViewById(R.id.test_monday_course);
        tueColum = (TextView) this.findViewById(R.id.test_tuesday_course);
        wedColum = (TextView) this.findViewById(R.id.test_wednesday_course);
        thrusColum = (TextView) this.findViewById(R.id.test_thursday_course);
        friColum = (TextView) this.findViewById(R.id.test_friday_course);
        satColum  = (TextView) this.findViewById(R.id.test_saturday_course);
        sunColum = (TextView) this.findViewById(R.id.test_sunday_course);
        course_table_layout = (RelativeLayout) this.findViewById(R.id.test_course_rl);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        //屏幕宽度
        int width = dm.widthPixels;
        //平均宽度
        final int aveWidth = width / 8;
        //第一个空白格子设置为25宽
        empty.setWidth(aveWidth * 3/4);
        monColum.setWidth(aveWidth * 33/32 + 1);
        tueColum.setWidth(aveWidth * 33/32 + 1);
        wedColum.setWidth(aveWidth * 33/32 + 1);
        thrusColum.setWidth(aveWidth * 33/32 + 1);
        friColum.setWidth(aveWidth * 33/32 + 1);
        satColum.setWidth(aveWidth * 33/32 + 1);
        sunColum.setWidth(aveWidth * 33/32 + 1);
        this.screenWidth = width;
        this.aveWidth = aveWidth;
        int height = dm.heightPixels;
        this.gridHight = height / 12;
        //设置课表界面
        //动态生成12 * maxCourseNum个textview
        for(int i = 1; i <= 12; i ++){

            for(int j = 1; j <= 8; j ++){

                TextView tx = new TextView(SuperCourseActivity.this);
                tx.setBackgroundColor(Color.parseColor("#0000FF"));
                tx.setId((i - 1) * 8  + j);     //设置组件ID,后面要用
                //除了最后一列，都使用course_text_view_bg背景（最后一列没有右边框）
                if(j < 8)
                    tx.setBackgroundDrawable(SuperCourseActivity.this.
                            getResources().getDrawable(R.drawable.course_text_view_bg));
                else
                    tx.setBackgroundDrawable(SuperCourseActivity.this.
                            getResources().getDrawable(R.drawable.course_table_last_colum));
                //相对布局参数
                RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
                        aveWidth * 33 / 32 + 1,
                        gridHight);
                //文字对齐方式
                tx.setGravity(Gravity.CENTER);
                //字体样式
                tx.setTextAppearance(this, R.style.courseTableText);
                //如果是第一列，需要设置课的序号（1 到 12）
                if(j == 1)
                {
                    tx.setText(String.valueOf(i));
                    rp.width = aveWidth * 3/4;
                    //设置他们的相对位置
                    if(i == 1)
                        rp.addRule(RelativeLayout.BELOW, empty.getId());
                    else
                        rp.addRule(RelativeLayout.BELOW, (i - 1) * 8);
                }
                else
                {
                    rp.addRule(RelativeLayout.RIGHT_OF, (i - 1) * 8  + j - 1);
                    rp.addRule(RelativeLayout.ALIGN_TOP, (i - 1) * 8  + j - 1);
                    tx.setText("");                        //也可以在这儿注入文本
                }

                tx.setLayoutParams(rp);
                course_table_layout.addView(tx);
            }
        }

    }

    public void show(Schedule sd){

        final Schedule schedule=sd;
        String[] weekAndTime=getWeekAndJieshu(sd.getTime());
        for(Course cs:courseList){
            if(cs.getCourseId().equals(sd.getCourseId()))
            {
                csId=cs.getCourseId();
                csName=cs.getCourseName();
                csCredit=cs.getCredit();
                csMethod=cs.getMethod();
                csType=cs.getType();
                break;
            }
        }

        final String csname=csName;
        final String csid=csId;
        final String cscredit=csCredit;
        final String csmethod=csMethod;
        final String cstype=csType;

        TextView courseInfo = new TextView(this);
        courseInfo.setText(csName+"@\n"+sd.getLocation()+"@\n"+weekAndTime[0]);
        //该textview的高度根据其节数的跨度来设置
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                aveWidth * 31 / 32,
                (gridHight - 5) * (Integer.parseInt(weekAndTime[3])-Integer.parseInt(weekAndTime[2])+1)) ;              //5 是padding   2表示跨的节数
        //textview的位置由课程开始节数和上课的时间（day of week）确定
        rlp.topMargin = 5 + (Integer.parseInt(weekAndTime[2])- 1) * gridHight;    //2表示在第2节开始
        rlp.leftMargin = 2;
        // 偏移由这节课是星期几决定
        rlp.addRule(RelativeLayout.RIGHT_OF, chiToInt.indexOf(weekAndTime[1])+1);       //2是表示周二   在R.id=2组件（周一组件）的右边    前面生成格子时的ID就是根据Day来设置的
        //字体剧中
        courseInfo.setGravity(Gravity.CENTER);
        // 设置一种背景
        final int number = new Random().nextInt(5);
        courseInfo.setBackgroundResource(background[number]);
        courseInfo.setTextSize(12);
        courseInfo.setLayoutParams(rlp);
        courseInfo.setTextColor(Color.WHITE);
        //设置不透明度
        courseInfo.getBackground().setAlpha(222);
        course_table_layout.addView(courseInfo);
        courseInfo.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Log.i("robin","......"+"点击事件");
                                              final TextView courseInfo1 = new TextView(SuperCourseActivity.this);

                                              courseInfo1.setText("课号@"+csid+"\n"+"课名@"+csname+"\n"+"学分@"+cscredit+"\n"+"方式@"+csmethod+"\n"+"类型@"+cstype+"\n"+"班级@"+schedule.getClasses()+"\n"+"班号@"+schedule.getClassNo()+"\n"+"人数@"+schedule.getPeopleNum()+"\n"+"学期@"+schedule.getSemester());

                                              RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                                                      (aveWidth * 31 / 32+1)*5,
                                                      (gridHight - 5) * 6 );
                                              rlp.topMargin = 5 + (3 - 1) * gridHight;
                                              rlp.leftMargin = 5;
                                              rlp.addRule(RelativeLayout.RIGHT_OF, 2);
                                              courseInfo1.setGravity(Gravity.CENTER);
//                                              int number = new Random().nextInt(5);
                                              courseInfo1.setBackgroundResource(background[number]);
                                              courseInfo1.setTextSize(20);
                                              courseInfo1.setLayoutParams(rlp);
                                              courseInfo1.setTextColor(Color.WHITE);
                                              courseInfo1.getBackground().setAlpha(222);
                                              course_table_layout.addView(courseInfo1);
                                              courseInfo1.setMovementMethod(ScrollingMovementMethod.getInstance());

                                              courseInfo1.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      courseInfo1.setVisibility(View.INVISIBLE);
                                                  }

                                              });
                                          }
                                      }
        );

    }
    public String[] getWeekAndJieshu(String time){


        String[] array=new String[4];

        Pattern p = Pattern.compile("(\\d+-\\d+周)\\s(.)\\[(\\d+)-(\\d+)节\\]");
        Matcher m = p.matcher(time);
        if(m.find()) {
            array[0] = m.group(1);    //课程周数
            array[1] = m.group(2);    //星期几
            array[2] = m.group(3);    //开始节数
            array[3] = m.group(4);    //结束节数
        }

         return array;
    }

}
