package com.example.administrator.helloword.fragment.zhuce;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.helloword.R;

/**
 * Created by Administrator on 2016/12/7.
 */
public class NotesListFragment extends Fragment {
    View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_page_notes_list,null);
        }
        return view;
    }
}
