package com.example.panke.fakesuperclass_client.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.panke.fakesuperclass_client.R;

public class ScheduleActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        textView = (TextView)findViewById(R.id.test);

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        textView.setText(json);
    }
}
