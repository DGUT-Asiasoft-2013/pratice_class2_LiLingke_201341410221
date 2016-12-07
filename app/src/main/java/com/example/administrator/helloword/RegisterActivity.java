package com.example.administrator.helloword;

import android.app.Activity;
import android.os.Bundle;

import com.example.administrator.helloword.fragments.inputcell.PictureInputCellFragment;
import com.example.administrator.helloword.fragments.inputcell.SimpleTextInputCellFragment;

/**
 * Created by Administrator on 2016/12/6.
 */
public class RegisterActivity extends Activity{
    SimpleTextInputCellFragment fragInputCellAccount;
    SimpleTextInputCellFragment fragInputCellPassword;
    SimpleTextInputCellFragment fragInputCellPasswordRepeat;
    PictureInputCellFragment fragInputCellPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        fragInputCellAccount = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_account);
        fragInputCellPassword = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password);
        fragInputCellPasswordRepeat = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_password_repeat);
        fragInputCellPicture = (PictureInputCellFragment)getFragmentManager().findFragmentById(R.id.input_picture);
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

    }
}
