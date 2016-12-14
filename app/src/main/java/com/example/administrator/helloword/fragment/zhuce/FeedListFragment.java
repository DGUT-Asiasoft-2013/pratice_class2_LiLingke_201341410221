package com.example.administrator.helloword.fragment.zhuce;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helloword.FeedContentActivity;
import com.example.administrator.helloword.R;
import com.example.administrator.helloword.api.Article;
import com.example.administrator.helloword.api.PageData;
import com.example.administrator.helloword.api.Server;
import com.example.administrator.helloword.api.User;

import com.example.administrator.helloword.fragments.inputcell.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FeedListFragment extends Fragment{
    View view;
    ListView listView;
    List<Article> data;
    View btnLoadMore;
    TextView textLoadMore;
    int page = 0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_page_feed_list,null);
            btnLoadMore = inflater.inflate(R.layout.widget_load_more_button,null);
            textLoadMore = (TextView) btnLoadMore.findViewById(R.id.load_more);

            listView = (ListView) view.findViewById(R.id.list);
            listView.addFooterView(btnLoadMore);
            listView.setAdapter(listadapt);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    ItemClick(position);
                }
            });

            textLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loanmore();

                }
            });
        }
        return view;

    }


    BaseAdapter listadapt = new BaseAdapter() {
        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup parent) {
            View view;
            if (convertview == null){
                LayoutInflater layoutInflatern = LayoutInflater.from(parent.getContext());
                view  = layoutInflatern.inflate(R.layout.fragment_page_feed_list_item,null);
            }else
            view = convertview;

            TextView text1 = (TextView)view.findViewById(R.id.list_item_name);
            TextView text2 = (TextView) view.findViewById(R.id.list_item_title);
            AvatarView image = (AvatarView) view.findViewById(R.id.list_item_iv);

            Article article = data.get(position);
            text2.setText(article.getAuthorName()+article.getText());
            text1.setText("123");
            image.load(article.getAuthorImage());
            return view;
        }
    };
    void ItemClick(int position){
       Article text = data.get(position);
        Intent intent = new Intent(getActivity(),FeedContentActivity.class);
        intent.putExtra("text",text);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        reLogin();

    }
    public void loanmore(){

        textLoadMore.setEnabled(false);
        textLoadMore.setText("加载中");

        Request request = Server.requestBuilderWithApi("feeds/"+(page+1)).get().build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textLoadMore.setEnabled(true);
                        textLoadMore.setText("点击加载更多");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textLoadMore.setEnabled(true);
                        textLoadMore.setText("点击加载更多");
                    }
                });

                try{
                    PageData<Article> feeds = new ObjectMapper().readValue(response.body().string(),new TypeReference<PageData<Article>>(){});
                    if (feeds.getNumber() > page){
                        if (data==null){
                            Toast.makeText(getActivity(),"空",Toast.LENGTH_SHORT).show();
                            data = feeds.getContent();
                        }else {
                            data.addAll(feeds.getContent());
                        }
                        page = feeds.getNumber();

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),"notifyDataSetChanged",Toast.LENGTH_SHORT).show();
                                listadapt.notifyDataSetChanged();
                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    public void reLogin(){
        final Request request = Server.requestBuilderWithApi("feeds")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //   ObjectMapper objectMapper = new ObjectMapper();
                //  final User user = objectMapper.readValue(response.body().string(),User.class);

                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    PageData<Article> data = new ObjectMapper().readValue(response.body().string(), new TypeReference<PageData<Article>>() {});
                    FeedListFragment.this.page = data.getNumber();
                    FeedListFragment.this.data = data.getContent();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listadapt.notifyDataSetInvalidated();
                        }
                    });
                }catch (final Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity())
                                    .setMessage(e.getMessage())
                                    .show();
                        }
                    });
                }


            }
        });


    }
    /*   void parseObject(){
        String jsonStr;
        String jsonArticleList = new JSONObject(jsonStr).getJSONArray("content").toString();
        new ObjectMapper().readValue(jsonArticleList, ArrayList<Article>.class);

    }*/
}
