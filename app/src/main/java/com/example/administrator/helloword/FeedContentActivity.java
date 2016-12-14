package com.example.administrator.helloword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.helloword.R;
import com.example.administrator.helloword.api.Article;
import com.example.administrator.helloword.api.Comment;
import com.example.administrator.helloword.api.PageData;
import com.example.administrator.helloword.api.Server;
import com.example.administrator.helloword.fragments.inputcell.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/7.
 */
public class FeedContentActivity extends Activity {
    TextView content_item_name,content_item_title,content_item_time;
    AvatarView avatarView;
    Button comment_btn;
    Article article;
    ListView comment_list;
    List<Comment>data;
    int page = 0;

    View btnLoadMore;
    TextView textLoadMore;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_content_activity);
        article = (Article)getIntent().getSerializableExtra("text");
        content_item_name = (TextView) findViewById(R.id.feed_content_item_name);
        content_item_title = (TextView) findViewById(R.id.feed_content_item_title);
        content_item_time = (TextView) findViewById(R.id.content_item_time);
        avatarView = (AvatarView) findViewById(R.id.feed_content_item_iv);
        comment_btn = (Button) findViewById(R.id.feedcontent_comment_btn);
        comment_list = (ListView) findViewById(R.id.comment_list);
//        btnLoadMore = LayoutInflater.inflate(R.layout.widget_load_more_button,null);
        btnLoadMore = LayoutInflater.from(this).inflate(R.layout.widget_load_more_button,null);
        textLoadMore = (TextView) btnLoadMore.findViewById(R.id.load_more);
        comment_list.addFooterView(btnLoadMore);
        comment_list.setAdapter(listAdapter);
        textLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment_lognmore();
            }
        });

        content_item_name.setText(article.getAuthorName());
        content_item_title.setText(article.getTitle());
        content_item_time.setText(DateFormat.format("yyy-mm-dd hh:mm",article.getCreateDate()).toString());
        avatarView.load(article.getAuthorImage());

        comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        contentreLogin();
    }

    public void comment(){
        Intent intent = new Intent(FeedContentActivity.this, CommentActivity.class);
        intent.putExtra("article",article);
        startActivity(intent);
        overridePendingTransition(R.anim.none, R.anim.slide_in_bottom);
    }

    BaseAdapter listAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return data == null? 0:data.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup parent) {
            View view;
            if (convertview == null){
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
                view = layoutInflater.inflate(R.layout.activity_comment_list_item,null);
            }else {view = convertview;}

            AvatarView avatarView = (AvatarView) view.findViewById(R.id.comment_list_item_iv);
            TextView textView_name = (TextView) view.findViewById(R.id.comment_list_item_name);
            TextView textView_text = (TextView) view.findViewById(R.id.comment_list_item_text);
            TextView textView_time = (TextView) view.findViewById(R.id.comment_list_item_tiem);

          //  textView_name.setText("name");
           // textView_text.setText("dfdsgsdfgfsd");
           textView_name.setText(data.get(position).getAuthor().getName());
            textView_time.setText(DateFormat.format("yyy-mm-dd hh:mm", data.get(position).getCreateDate()).toString());
            textView_text.setText(data.get(position).getText());
            avatarView.load(data.get(position).getAuthor().getAvatar());

          //  avatarView.load(data.get(position).get);

            return view;
        }
    };

    public void  contentreLogin(){
        final Request request = Server.requestBuilderWithApi("article/"+article.getId()+"/comments")
                .get()
                .build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
             ;try{
                  //  ObjectMapper objectMapper = new ObjectMapper();
                    PageData<Comment> data = new ObjectMapper().readValue(response.body().string(), new TypeReference<PageData<Comment>>() {});
                    FeedContentActivity.this.page = data.getNumber();
                    FeedContentActivity.this.data = data.getContent();
                   FeedContentActivity.this.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           listAdapter.notifyDataSetInvalidated();
                       }
                   });
                }catch (final Exception e){
                    FeedContentActivity.this. runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           new AlertDialog.Builder(FeedContentActivity.this)
                                   .setMessage(e.getMessage())
                                   .show();
                       }
                   });
                }


            }
        });


    }
   public void comment_lognmore(){
       textLoadMore.setEnabled(false);
       textLoadMore.setText("加载中");

       Request request = Server.requestBuilderWithApi("article/"+article.getId()+"/comments/"+page+1).get().build();
       Server.getSharedClient().newCall(request).enqueue(new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               FeedContentActivity.this.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       textLoadMore.setEnabled(true);
                       textLoadMore.setText("点击加载更多");
                   }
               });

           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               FeedContentActivity.this.runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       textLoadMore.setEnabled(true);
                       textLoadMore.setText("点击加载更多");
                   }
               });

               try{
                   PageData<Comment> feeds = new ObjectMapper().readValue(response.body().string(),new TypeReference<PageData<Comment>>(){});
                   if (feeds.getNumber() > page){
                       if (data==null){
                           data = feeds.getContent();
                       }else {
                           data.addAll(feeds.getContent());
                       }
                       page = feeds.getNumber();

                       FeedContentActivity.this.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               listAdapter.notifyDataSetChanged();
                           }
                       });
                   }
               }catch (Exception e){
                   e.printStackTrace();
               }

           }
       });
   }
}
