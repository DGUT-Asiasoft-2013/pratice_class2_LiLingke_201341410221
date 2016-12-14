package com.example.administrator.helloword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.helloword.api.Article;
import com.example.administrator.helloword.api.Server;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/14.
 */
public class CommentActivity extends Activity {
   // Button comment_comit;
    EditText title_edit,neirong_edit;
    Article article;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        article = (Article) getIntent().getSerializableExtra("article");
        title_edit = (EditText) findViewById(R.id.comment_title);
        neirong_edit = (EditText) findViewById(R.id.comment_text);
        findViewById(R.id.comment_comit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendComment();
            }
        });
    }

    public void sendComment(){
        String neirong = neirong_edit.getText().toString();
        OkHttpClient client = Server.getSharedClient();
        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("text", neirong);
        Request request = Server.requestBuilderWithApi("article/"+article.getId()+"/comments")
                .method("post",null)
                .post(body.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CommentActivity.this.onResponse(call, str);
                    }
                });


            }
        });
    }

    protected void onResponse(Call call, String response){
        new AlertDialog.Builder(CommentActivity.this)
                .setTitle("上传成功")
                .setMessage(response)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CommentActivity.this.finish();
                        overridePendingTransition(R.anim.none, R.anim.slide_out_bottom);
                    }
                }).show();
    }
}
