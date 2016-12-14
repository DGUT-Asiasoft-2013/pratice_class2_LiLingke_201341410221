package com.example.administrator.helloword;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.helloword.fragment.zhuce.FeedListFragment;
import com.example.administrator.helloword.fragment.zhuce.MeFragment;
import com.example.administrator.helloword.fragment.zhuce.NotesListFragment;
import com.example.administrator.helloword.fragment.zhuce.SearchPageFragment;
import com.example.administrator.helloword.fragments.inputcell.MainTabbarFragment;

/**
 * Created by Administrator on 2016/12/6.
 */
public class HelloWorldActivity extends Activity{
    FeedListFragment contentFeedList = new FeedListFragment();
    NotesListFragment contentNotesList = new NotesListFragment();
    SearchPageFragment contentSearch = new SearchPageFragment();
    MeFragment contentMe = new MeFragment();
    MainTabbarFragment tabbar;
    String str;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helloworld);
        tabbar = (MainTabbarFragment) getFragmentManager().findFragmentById(R.id.frag_tabbar);
        tabbar.setOnTabSelectedListener(new MainTabbarFragment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                changeContentFragment(index);

            }
        });

    }

    protected  void onResume(){
        super.onResume();

        tabbar.setSelectedItem(0);
    }

    void changeContentFragment(int index){
        Fragment newFrag = null;
        switch (index){
            case 0:
                newFrag = contentFeedList;
                break;
            case 1:
                newFrag = contentNotesList;
                break;
            case 2:
                newFrag = contentSearch;
                break;
            case 3:
                newFrag = contentMe;
                break;
        }

        if (newFrag == null)
            return;;

        getFragmentManager()
                .beginTransaction()
                //R.id.content是Fragment容器，newFrag是要被添加进content的Fragment
                .replace(R.id.content,newFrag)
                .commit();
    }
}
