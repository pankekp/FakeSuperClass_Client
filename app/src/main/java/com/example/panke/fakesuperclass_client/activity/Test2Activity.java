package com.example.panke.fakesuperclass_client.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.panke.fakesuperclass_client.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2Activity extends AppCompatActivity {

    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        new Thread(){
            @Override
            public void run() {
                execute();
            }
        }.start();
    }

    private void execute(){
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8888/FakeSuperClass_Server/rec/sId/20161/tId/0000123/code/0000")
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            String json = response.body().string();
            Log.i("panke",json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
