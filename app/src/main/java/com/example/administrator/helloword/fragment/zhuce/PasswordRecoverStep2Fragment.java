package com.example.administrator.helloword.fragment.zhuce;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.helloword.R;

/**
 * Created by Administrator on 2016/12/6.
 */
public class PasswordRecoverStep2Fragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(view==null){
            view = inflater.inflate(R.layout.fragment_password_recover_step2, null);
        }

        return view;
    }
}
