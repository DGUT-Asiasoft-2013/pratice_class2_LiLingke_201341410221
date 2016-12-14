package com.example.administrator.helloword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.helloword.api.Server;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/7.
 */
public class NewActivity extends Activity {
   Button btn_comit;
    EditText title_edit,neirong_edit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiyt_new);

        title_edit = (EditText) findViewById(R.id.title);
        neirong_edit = (EditText) findViewById(R.id.me_edit);
        findViewById(R.id.btn_comit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comit_edit();

                //显示动画效果，必须紧挨着startActivity()或finish()函数之后调用
                //第一个参数是第一个activity进入的动画，第二个参数是第二个activity退出的动画
                //如果不是在activity中调用，要先用getActivity()函数获取当前activity，在调用他

            }
        });
    }

    public void comit_edit(){
        String title = title_edit.getText().toString();
        String neirong = neirong_edit.getText().toString();
        OkHttpClient client = Server.getSharedClient();
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title",title)
                .addFormDataPart("text", neirong);
        Request request = Server.requestBuilderWithApi("article")
                .method("post",null)
                .post(body.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
               final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NewActivity.this.onResponse(call, str);
                    }
                });


            }
        });
    }

    protected void onResponse(Call call, String response){
        new AlertDialog.Builder(NewActivity.this)
                .setTitle("上传成功")
                .setMessage(response)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NewActivity.this.finish();
                        overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
                    }
                }).show();
    }
}
