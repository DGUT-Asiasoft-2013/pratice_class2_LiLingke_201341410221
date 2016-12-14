package com.example.administrator.helloword;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.helloword.api.MD5;
import com.example.administrator.helloword.api.Server;
import com.example.administrator.helloword.fragment.zhuce.PasswordRecoverStep1Fragment;
import com.example.administrator.helloword.fragment.zhuce.PasswordRecoverStep2Fragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/6.
 */
public class PasswordRecoverActivity extends Activity {
    PasswordRecoverStep1Fragment step1 = new PasswordRecoverStep1Fragment();
    PasswordRecoverStep2Fragment step2 = new PasswordRecoverStep2Fragment();
    String email,verify,password,passwordrepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_password_recover);
        step1.setOnGoNextListener(new PasswordRecoverStep1Fragment.OnGoNextListener() {
            public void onGoNext() {
                email = step1.getEditText();
                Toast.makeText(PasswordRecoverActivity.this,email,Toast.LENGTH_SHORT).show();
                goStep2();
            }
        });
        step2.setOnGoComitListener(new PasswordRecoverStep2Fragment.OnGoComitListener() {
            @Override
            public void onGoComit() {
                goComit();
            }
        });

        getFragmentManager().beginTransaction().replace(R.id.container, step1).commit();
    }

    void goStep2(){

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.slide_in_right,
                        R.animator.slide_out_left,
                        R.animator.slide_in_left,
                        R.animator.slide_out_right
                )
                .replace(R.id.container, step2)
                .addToBackStack(null)
                .commit();
    }

    void goComit(){
       // verify = step2.getVerify();
        password = MD5.getMD5(step2.getpassword());
        passwordrepeat = step2.getPasswordRepeat();

        if (!step2.getpassword().equals(passwordrepeat)){
            Toast.makeText(PasswordRecoverActivity.this,"两次输入的密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = Server.getSharedClient();
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("email",email)
                .addFormDataPart("passwordHash",password);
        Request request =Server.requestBuilderWithApi("passwordrecover")
                .method("post",null)
                .post(body.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PasswordRecoverActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PasswordRecoverActivity.this,responseString, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }
}
