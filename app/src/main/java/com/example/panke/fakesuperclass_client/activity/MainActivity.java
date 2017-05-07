package com.example.panke.fakesuperclass_client.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.panke.fakesuperclass_client.R;
import com.example.panke.fakesuperclass_client.bean.InitInfo;
import com.example.panke.fakesuperclass_client.bean.Semester;
import com.example.panke.fakesuperclass_client.bean.Teacher;
import com.example.panke.fakesuperclass_client.dbutil.CourseDbHelper;
import com.example.panke.fakesuperclass_client.dbutil.DBmanager;
import com.example.panke.fakesuperclass_client.utils.ActivityUtils;
import com.example.panke.fakesuperclass_client.utils.JsonParser;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private AutoCompleteTextView autoCompleteTextView;
    private TextView tv;
    private EditText idEt;
    private ImageView iv;
    private EditText codeEt;

    private String code;
    private String sId;
    private String tId;
    private String json;
    private Map<String, String> semesters;
    private List<String> tNames;
    private String tName;
    private String cookie = null;
    private String localId;


    private Handler handler;

    private JsonParser jsonParser;
    private DBmanager dBmanager;
    private CourseDbHelper courseDbHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jsonParser = new JsonParser();

        //数据库前期加载
        courseDbHelper = new CourseDbHelper(this, "timetable", null, 1);
        sqLiteDatabase = courseDbHelper.getReadableDatabase();
        dBmanager = new DBmanager(courseDbHelper,sqLiteDatabase);

        File dirFirstFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/jpg");//方法二：通过变量文件来获取需要创建的文件夹名字
        if(!dirFirstFolder.exists())
        { //如果该文件夹不存在，则进行创建
            dirFirstFolder.mkdirs();//创建文件夹
        }

        Thread t = new Thread(){
            @Override
            public void run() {
                //拿出数据库中的时间戳
                String timeStamp = dBmanager.findNewestTimeStamp()+"";
                Log.i("panke",timeStamp+".......");
                //发送时间戳
                String temp = ActivityUtils.myList(timeStamp);
                Log.i("panke",temp+"......");
                tNames = new ArrayList<String>();
                semesters = new HashMap<String, String>();
                //若服务器端返回的是“ok”，则直接使用本地数据库中的列表信息
                //否则先将服务器端发送过来的json格式列表解析，存入数据库，再拿出
                if ("ok".equals(temp)){
                    tNames = dBmanager.findAllTeacherName();
                    semesters = dBmanager.findAllSemesters();
                }else {
                    try {
                        InitInfo initInfo = jsonParser.parseInitInfo(temp);
                        long ts = initInfo.getTimeStamp();
                        dBmanager.saveTimeStamp(ts);
                        Map<String, String>tm = initInfo.getTeacherMap();
                        Map<String, String>sm = initInfo.getSemesterMap();
                        for(String key:tm.keySet()){
                            Teacher t=new Teacher();
                            t.setTeacherName(tm.get(key));
                            t.setTeacherId(key);
                            dBmanager.saveTeacher(t);
                        }
                        for(String key:sm.keySet()){
                            Semester s=new Semester();
                            s.setSemesterId(sm.get(key));
                            s.setSemesterName(key);
                            dBmanager.saveSemester(s);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //存数据库中拿出列表
                    tNames = dBmanager.findAllTeacherName();
                    semesters = dBmanager.findAllSemesters();
                }
            }
        };

        t.start();
        //网络连接线程与主线程合并，否则在没有得到教师列表或学期列表是主线程会在初始化下拉菜单或自动完成框时报空指针异常
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        tv = (TextView)findViewById(R.id.idTip);
        idEt = (EditText)findViewById(R.id.id);
        iv = (ImageView) findViewById(R.id.codeImg);
        codeEt = (EditText) findViewById(R.id.codeText);

        //activity初始化时设置重名id信息提示、重名id填写、验证码显示及验证码填写四个组件占位隐藏
        tv.setVisibility(View.INVISIBLE);
        idEt.setVisibility(View.INVISIBLE);
        iv.setVisibility(View.INVISIBLE);
        codeEt.setVisibility(View.INVISIBLE);

        //下拉菜单初始化设置
        spinner = (Spinner) findViewById(R.id.semester);

        Set<String> ssNames = new TreeSet<String>();
        ssNames = semesters.keySet();
        List<String> sNames = new ArrayList<String>();
        sNames.addAll(ssNames);

        ArrayAdapter<String> adapterSemester = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sNames);
        spinner.setAdapter(adapterSemester);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String sName = adapterView.getItemAtPosition(i).toString();
                sId = semesters.get(sName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //自动完成框初始化设置
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.name);

        ArrayAdapter<String> adapterName = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tNames);
        autoCompleteTextView.setAdapter(adapterName);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tName = adapterView.getItemAtPosition(i).toString();

            }

        });


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String flag = msg.getData().getString("flag");
                switch (flag){
                    case "codeEnvi":
                        Bitmap bitmap = BitmapFactory.decodeFile(ActivityUtils.myPath());
                        iv.setImageBitmap(bitmap);
                        iv.setVisibility(View.VISIBLE);
                        codeEt.setVisibility(View.VISIBLE);
                        break;
                    case "codeInvi":
                        iv.setVisibility(View.INVISIBLE);
                        codeEt.setVisibility(View.INVISIBLE);
                        tv.setVisibility(View.INVISIBLE);
                        idEt.setVisibility(View.INVISIBLE);
                        idEt.setText("");
                        codeEt.setText("");
                        break;
                    case "idEnvi":
                        tv.setVisibility(View.VISIBLE);
                        idEt.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };
    }

    public void search(View view) {
        new Thread() {
            @Override
            public void run() {

                //若id栏位空，则表示没有重名，使用用户所选名字进行id查找
                //若id栏不为空，则说明有重名情况，将用户根据提示所填的id发送至服务端
                String id = idEt.getText().toString();
                if ("".equals(id)){
                    //用名字查数据库，有重复需要查出不同的id以逗号隔开，无重复则返回单个id
                    localId = dBmanager.findTId(tName);
                    if (localId.length()>7){
                        String[] localIds = localId.split(",");
                        String tip = "存在重复名称，请输入id进行查询："+localIds[0]+"/"+localIds[1];
                        tv.setText(tip);
                        ActivityUtils.myHandler(handler, "flag", "idEnvi");
                    }else {
                        tId = localId;
                    }
                }else {
                    tId = id;
                }

                //若code填写栏为空则，发送错误的验证码
                //只用不为空，说明已经填写验证码，此时发送用户填写的验证码
                String codeText = codeEt.getText().toString();
                if ("".equals(codeText)) {
                    code = "0000";
                } else {
                    code = codeText;
                }

                //将服务器端返回的json解析
                json = ActivityUtils.myOkHttp(sId,tId,code,cookie);

                //根据json字符串中type值的不同，进行不同的操作
                //若为record，则将json字符串加入intent跳转至数据显示的activity，子线程结束
                //若为vcImage，则向主线程发送消息，显示验证码，子线程结束
                try {
                    String type = jsonParser.typeOf(json);
                    switch (type){
                        case "record":
                            sendActivity(json);
                            ActivityUtils.myHandler(handler, "flag", "codeInvi");
                            break;
                        case "vcImage":
                            String temp = jsonParser.parseVcImage(json,ActivityUtils.myPath());
                            //. = 替换
                            Pattern p2 = Pattern.compile("=");
                            Matcher m2 = p2.matcher(temp);
                            temp = m2.replaceAll("%3D");
                            Pattern p3 = Pattern.compile("ASP\\.");
                            Matcher m3 = p3.matcher(temp);
                            temp = m3.replaceAll("");
                            ActivityUtils.myHandler(handler, "flag", "codeEnvi");
                            cookie = temp;
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void sendActivity(String json) {
        Intent intent = new Intent();
        intent.putExtra("json", json);
        intent.setAction("schedule");
        startActivity(intent);
    }
}