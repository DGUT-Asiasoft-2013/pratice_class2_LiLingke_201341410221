package com.example.administrator.helloword.fragments.inputcell;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.helloword.NewActivity;
import com.example.administrator.helloword.R;

/**
 * Created by Administrator on 2016/12/6.
 */
public class MainTabbarFragment extends Fragment{
    View tabFeeds,tabSearch,tabMe,tabNotes;
    Button btnNew;
    View [] tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_tabbar,null);
        tabFeeds = view.findViewById(R.id.tab_feeds);
        tabMe = view.findViewById(R.id.tab_me);
        tabSearch = view.findViewById(R.id.tab_search);
        tabNotes = view.findViewById(R.id.tab_notes);
        btnNew = (Button) view.findViewById(R.id.tab_new);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NewActivity.class);
                startActivity(intent);
               getActivity().overridePendingTransition(R.anim.none,R.anim.slide_in_bottom);

            }
        });
        tabs = new View[]{
                tabFeeds,tabNotes,tabSearch,tabMe
        };
        for (final View tab : tabs){
            tab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onTabClicked(tab);
                }
            });
        }
        return  view;


    }
    /*
    *设置第一次进入HelloWorldActivity被选中的按钮
     */
    public void setSelectedItem(int index){
        if (index >= 0 && index <tabs.length)
            onTabClicked(tabs[index]);
    }

    private void onTabClicked(View tab) {
        int selectedIndex = -1;
        for (int i=0; i<tabs.length; i++){
            View otherTab = tabs[i];
            if (otherTab == tab){
                otherTab.setSelected(true);
                selectedIndex = i;
            }else
            otherTab.setSelected(false);
        }
        if (onTabSelectedListener != null &&selectedIndex >=0){
            onTabSelectedListener.onTabSelected(selectedIndex);
        }
    }
    /*
    *定义一个回调接口，供外部调用
     */
    public static interface OnTabSelectedListener{
        void onTabSelected(int index);
    }

    OnTabSelectedListener onTabSelectedListener;
    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener){
        this.onTabSelectedListener = onTabSelectedListener;
    }
}
