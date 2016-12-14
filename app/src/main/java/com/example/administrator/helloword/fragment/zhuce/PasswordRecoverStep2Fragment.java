package com.example.administrator.helloword.fragment.zhuce;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.helloword.R;
import com.example.administrator.helloword.fragments.inputcell.SimpleTextInputCellFragment;

/**
 * Created by Administrator on 2016/12/6.
 */
public class PasswordRecoverStep2Fragment extends Fragment {
    View view;
  //  SimpleTextInputCellFragment input_verify;
    SimpleTextInputCellFragment input_password;
    SimpleTextInputCellFragment input_password_repeat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(view==null){
            view = inflater.inflate(R.layout.fragment_password_recover_step2, null);
         //   input_verify = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_verify);
            input_password = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
            input_password_repeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);

            view.findViewById(R.id.repeat_password_btnsubmit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                       goComit();
                }
            });
        }
        return view;
    }

    public void onResume() {
        super.onResume();

        input_password.setLabelText("输入新密码");
        input_password.setHintText("请输入新密码");
        input_password_repeat.setLabelText("确认新密码");
        input_password_repeat.setHintText("请在次输入新密码");

    }

   // public String getVerify(){return input_verify.getEditText();}
    public String getpassword(){return input_password.getEditText();}
    public String getPasswordRepeat(){return input_password_repeat.getEditText();}

    public static interface OnGoComitListener{
        void onGoComit();
    }

    OnGoComitListener onGoComitListener;
    public void setOnGoComitListener (OnGoComitListener onGoComitListener){
        this.onGoComitListener = onGoComitListener;
    }

    void goComit(){
        if (onGoComitListener != null)
            onGoComitListener.onGoComit();
    }
}
