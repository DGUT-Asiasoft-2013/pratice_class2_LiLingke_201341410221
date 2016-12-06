package com.example.administrator.helloword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.helloword.fragments.inputcell.SimpleTextInputCellFragment;

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
                goLogin();
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

    void goLogin(){
        Intent itnt = new Intent(this, HelloWorldActivity.class);
        startActivity(itnt);
    }

    void goRecoverPassword(){
        Intent itnt = new Intent(this, PasswordRecoverActivity.class);
        startActivity(itnt);
    }
}
