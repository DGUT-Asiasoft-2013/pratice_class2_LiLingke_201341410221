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

        fragInputCellAccount.setLabelText("�û���");
        fragInputCellAccount.setHintText("�������û���");
        fragInputCellPassword.setLabelText("����");
        fragInputCellPassword.setHintText("����������");
        fragInputCellPassword.setIsPassword(true);
        fragInputCellPasswordRepeat.setLabelText("�ظ�����");
        fragInputCellPasswordRepeat.setHintText("���ٴ���������");
        fragInputCellPasswordRepeat.setIsPassword(true);

    }
}
