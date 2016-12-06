package com.example.administrator.helloword.fragments.inputcell;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.helloword.R;

/**
 * Created by Administrator on 2016/12/6.
 */
public class MainTabbarFragment extends Fragment{
    View btnNew,tabFeeds,tabSearch,tabMe,tabNotes;
    View [] tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_tabbar,null);
        tabFeeds = view.findViewById(R.id.tab_feeds);
        tabMe = view.findViewById(R.id.tab_me);
        tabSearch = view.findViewById(R.id.tab_search);
        tabNotes = view.findViewById(R.id.tab_notes);
        btnNew = view.findViewById(R.id.tab_new);

        tabs = new View[]{
                tabNotes,tabSearch,tabMe,tabFeeds
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

    private void onTabClicked(View tab) {
        for (View otherTab : tabs){
            otherTab.setSelected(otherTab == tab);
        }
    }
}
