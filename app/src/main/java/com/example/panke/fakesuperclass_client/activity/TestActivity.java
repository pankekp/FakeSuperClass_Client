package com.example.panke.fakesuperclass_client.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.panke.fakesuperclass_client.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TestActivity extends AppCompatActivity {

    private Spinner spinner;
    private AutoCompleteTextView autoCompleteTextView;
    private TextView tv;
    private EditText et;

    private String sId;
    private String tId;
    private String tName;
    private String tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        spinner = (Spinner) findViewById(R.id.semester);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.name);
        tv = (TextView)findViewById(R.id.idTip);
        et = (EditText)findViewById(R.id.id);

        tv.setVisibility(View.INVISIBLE);
        et.setVisibility(View.INVISIBLE);

        Map<String, String> semesters = new HashMap<String, String>();
        semesters.put("20171", "2017-1");
        semesters.put("20162", "2016-2");
        semesters.put("20161", "2016-1");
        semesters.put("20152", "2015-2");
        Set<String> ssIds = new TreeSet<String>();
        ssIds = semesters.keySet();
        final List<String> sIds = new ArrayList<String>();
        sIds.addAll(ssIds);
        final List<String> sNames = new ArrayList<String>();
        for (String sId : sIds) {
            sNames.add(semesters.get(sId));
        }

        ArrayAdapter<String> adapterSemester = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sNames);
        spinner.setAdapter(adapterSemester);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int idAtPosition = (int)adapterView.getSelectedItemId();
                sId = sIds.get(idAtPosition);
                Log.i("panke", sId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> tNames = new ArrayList<String>();
        List<String> tIds = new ArrayList<String>();

        tNames.add(0,"aaa");
        tNames.add(1,"bbb");
        tNames.add(2,"bbb");
        tNames.add(3,"ccc");
        tIds.add(0,"111");
        tIds.add(1,"222");
        tIds.add(2,"333");
        tIds.add(3,"444");

        ArrayAdapter<String> adapterName = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tNames);
        autoCompleteTextView.setAdapter(adapterName);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tName = adapterView.getItemAtPosition(i).toString();
                Log.i("panke", tName);
                if ("bbb".equals(tName)){
                    tip = "存在重复名称，请输入id进行查询："+"333/444";
                    tv.setText(tip);
                    tv.setVisibility(View.VISIBLE);
                    et.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void test(View view) {
        tId = et.getText().toString();
        Log.i("panke",tId+"...");
    }
}
