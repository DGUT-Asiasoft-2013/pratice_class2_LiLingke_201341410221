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
public class PasswordRecoverStep1Fragment extends Fragment{
    SimpleTextInputCellFragment fragEmail;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(view==null){
            view = inflater.inflate(R.layout.fragment_password_recover_step1, null);

            fragEmail = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_step1_email);

            view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    goNext();
                }
            });
        }

        return view;
    }

    public String getEditText(){
        return fragEmail.getEditText();
    }
    @Override
    public void onResume() {
        super.onResume();

        fragEmail.setLabelText("输入邮箱");
        fragEmail.setHintText("请输入邮箱帐号");
    }

    public static interface OnGoNextListener{
        void onGoNext();
    }

    OnGoNextListener onGoNextListener;

    public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
        this.onGoNextListener = onGoNextListener;
    }

    void goNext(){
        if(onGoNextListener!=null){
            onGoNextListener.onGoNext();
        }
    }

}
