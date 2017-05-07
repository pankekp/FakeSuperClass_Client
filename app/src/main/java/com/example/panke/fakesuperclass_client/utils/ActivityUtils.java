package com.example.panke.fakesuperclass_client.utils;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Pan Ke on 2017/3/27.
 */

public class ActivityUtils {

    public static String myOkHttp(String sId, String tId, String code, String cookie) {

        OkHttpClient okHttpClient = new OkHttpClient();
        String json = null;
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8888/FakeSuperClass_Server/rec/sId/" + sId + "/tId/" + tId + "/code/" + code + "/cookie/"+cookie)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
                Log.i("panke",json+"......");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String myPath() {
        File file = Environment.getExternalStorageDirectory();
        String path = file.getAbsolutePath() + "/jpg/checkcode.jpg";
        return path;
    }

    public static void myHandler(Handler handler, String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        Message msg = new Message();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    public static String myList(String timeStamp) {

        OkHttpClient okHttpClient = new OkHttpClient();
        String json = null;
        Request request = new Request.Builder()
                .url("http:/10.0.2.2:8888/FakeSuperClass_Server/list/timeStamp/" + timeStamp)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                json = response.body().string();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

}
