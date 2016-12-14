package com.example.administrator.helloword;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.helloword.api.MD5;
import com.example.administrator.helloword.api.Server;
import com.example.administrator.helloword.api.User;
import com.example.administrator.helloword.fragments.inputcell.SimpleTextInputCellFragment;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public class LoginActivity extends Activity{
   public  SimpleTextInputCellFragment fragInputCellAccount;
    public SimpleTextInputCellFragment fragInputCellPassword;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fragInputCellAccount = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.account);
        fragInputCellPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.password);

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goRegister();
            }
        });

        findViewById(R.id.up_login).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Login_into();
            }
        });

        findViewById(R.id.btn_forgot_password).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goRecoverPassword();
            }
        });
    }

  protected  void onResume(){
        super.onResume();


      fragInputCellAccount.setLabelText("用户名");
      fragInputCellAccount.setHintText("请输入用户名");
      fragInputCellPassword.setLabelText("密码");
      fragInputCellPassword.setHintText("请输入密码");
    }

    void goRegister(){
        Intent itnt = new Intent(this,RegisterActivity.class);
        startActivity(itnt);
    }

    void goRecoverPassword(){
        Intent itnt = new Intent(this, PasswordRecoverActivity.class);
        startActivity(itnt);
    }

    void Login_into(){
        String account = fragInputCellAccount.getEditText();
        String password = fragInputCellPassword.getEditText();
        password = MD5.getMD5(password);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("登录中");
        progressDialog.setMessage("请稍候");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        OkHttpClient client = Server.getSharedClient();
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account",account)
                .addFormDataPart("passwordHash",password);

        Request request =Server.requestBuilderWithApi("login")
                .method("post",null)
                .post(body.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    final User user = objectMapper.readValue(response.body().string(),User.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();

                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("登录成功")
                                    .setMessage(user.getName()+","+user.getAccount())
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(LoginActivity.this,HelloWorldActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }).show();
                        }
                    });
                }catch (final Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("登录失败")
                                    .setMessage(e.getMessage())
                                    .show();
                        }
                    });
                }
            }
        });
    }
}
