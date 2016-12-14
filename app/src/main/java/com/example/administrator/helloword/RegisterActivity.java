package com.example.administrator.helloword;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.helloword.api.MD5;
import com.example.administrator.helloword.api.Server;
import com.example.administrator.helloword.fragments.inputcell.PictureInputCellFragment;
import com.example.administrator.helloword.fragments.inputcell.SimpleTextInputCellFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/6.
 */
public class RegisterActivity extends Activity{
    SimpleTextInputCellFragment fragInputCellAccount;
    SimpleTextInputCellFragment fragInputCellPassword;
    SimpleTextInputCellFragment fragInputCellPasswordRepeat;
    SimpleTextInputCellFragment fragImputCellEmail;
    SimpleTextInputCellFragment fragInputCellName;
    PictureInputCellFragment fragInputCellPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        fragInputCellAccount = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_account);
        fragInputCellPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
        fragInputCellPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);
        fragInputCellPicture = (PictureInputCellFragment)getFragmentManager().findFragmentById(R.id.input_picture);
        fragImputCellEmail = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_email);
        fragInputCellName= (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_name);
        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        fragInputCellAccount.setLabelText("用户名");
        fragInputCellAccount.setHintText("请输入用户名");

        fragInputCellPassword.setLabelText("密码");
        fragInputCellPassword.setHintText("请输入密码");
        fragInputCellPassword.setIsPassword(true);

        fragInputCellPasswordRepeat.setLabelText("请再次输入密码");
        fragInputCellPasswordRepeat.setHintText("确认密码");
        fragInputCellPasswordRepeat.setIsPassword(true);

        fragImputCellEmail.setLabelText("电子邮件");
        fragImputCellEmail.setHintText("请输入电子邮件");

        fragInputCellName.setLabelText("昵称");
        fragInputCellName.setHintText("请输入昵称");

    }

    void submit(){
        String account = fragInputCellAccount.getEditText();
        String name = fragInputCellName.getEditText();
        String passsword = fragInputCellPassword.getEditText();
        String comitpassword = fragInputCellPasswordRepeat.getEditText();
        String email = fragImputCellEmail.getEditText();

        if (!passsword.equals(comitpassword)){
            Toast.makeText(RegisterActivity.this,"两次输入的密码不一致！请重新输入",Toast.LENGTH_SHORT).show();
            return;
        }

        passsword = MD5.getMD5(passsword);

        OkHttpClient client = Server.getSharedClient();

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account",account)
                .addFormDataPart("name",name)
                .addFormDataPart("passwordHash", passsword)
                .addFormDataPart("email",email);

        if (fragInputCellPicture.getPngData() != null){
            requestBodyBuilder.addFormDataPart("avatar", "avatar",
                    RequestBody.create(MediaType.parse("image/png"),fragInputCellPicture.getPngData()));
        }

        Request request  = Server.requestBuilderWithApi("register")
                .method("post", null)
                .post(requestBodyBuilder.build())
                .build();

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("请稍后");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        RegisterActivity.this.onFailure(call, e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                final String responseString = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        try {
                            RegisterActivity.this.onResponse(call, responseString);
                        } catch (Exception e) {
                            e.printStackTrace();
                            RegisterActivity.this.onFailure(call,e);
                        }
                    }
                });

            }
        });
  }
    void onResponse(Call arg0, String responseBody){
        new AlertDialog.Builder(this)
                .setTitle("注册成功")
                .setMessage(responseBody)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
       RegisterActivity.this.finish();
    }

    void onFailure(Call arg0, Exception arg1){
        new AlertDialog.Builder(this)
                .setTitle("注册失败")
                .setMessage(arg1.getLocalizedMessage())
                .setNegativeButton("好",null).show();
    }

}
