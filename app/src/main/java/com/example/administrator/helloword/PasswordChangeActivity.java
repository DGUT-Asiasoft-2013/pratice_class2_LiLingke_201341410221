package com.example.administrator.helloword;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.helloword.api.MD5;
import com.example.administrator.helloword.api.Server;
import com.example.administrator.helloword.fragments.inputcell.SimpleTextInputCellFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/13.
 */
public class PasswordChangeActivity extends Activity {
    SimpleTextInputCellFragment change_password;
    SimpleTextInputCellFragment change_new_password;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        change_password = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.change_password);
        change_new_password = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.change_new_password);

        findViewById(R.id.change_password_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoChangepassword();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        change_password.setLabelText("输入密码");
        change_password.setHintText("请输入当前密码");
        change_new_password.setLabelText("输入新密码");
        change_new_password.setHintText("请输入新密码");
    }

    void gotoChangepassword(){
        String password = MD5.getMD5(change_password.getEditText());
        String newpassword = MD5.getMD5(change_new_password.getEditText());

        OkHttpClient client = Server.getSharedClient();

        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("changePassword",password)
                .addFormDataPart("changeNewPassword", newpassword);
        Request request  = Server.requestBuilderWithApi("changepassword")
                .method("post",null)
                .post(body.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseString = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PasswordChangeActivity.this, responseString, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}
