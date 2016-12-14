package com.example.administrator.helloword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.administrator.helloword.api.Server;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/6.
 */
public class BootActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);


    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            private int abcd = 0;

            public void run() {
                startLoginActivity();
            }
        }, 1000);*/

        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuilderWithApi("hello")
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                BootActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BootActivity.this,e.getLocalizedMessage()+"连接失败", Toast.LENGTH_SHORT).show();
                        startLoginActivity();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                BootActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Toast.makeText(BootActivity.this, response.body().string(), Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            startLoginActivity();
                        }

                    }
                });


              }


        });
    }

    void startLoginActivity(){
        Intent itnt = new Intent(this, LoginActivity.class);
        startActivity(itnt);
        finish();
    }
}
