package com.example.administrator.helloword.fragment.zhuce;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.helloword.PasswordChangeActivity;
import com.example.administrator.helloword.R;
import com.example.administrator.helloword.api.User;
import com.example.administrator.helloword.api.Server;
import com.example.administrator.helloword.fragments.inputcell.widgets.AvatarView;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/7.
 */
public class MeFragment extends Fragment {
    View view;
    TextView my_text;
    ProgressBar progressBar;
    AvatarView avatarView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null){
            view = inflater.inflate(R.layout.fragment_page_my,null);
            my_text = (TextView) view.findViewById(R.id.my_text);
            progressBar = (ProgressBar) view.findViewById(R.id.progress);
            avatarView = (AvatarView) view.findViewById(R.id.avatar);
            view.findViewById(R.id.xg_password).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), PasswordChangeActivity.class);
                    startActivity(intent);
                }
            });
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        my_text.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient clien = Server.getSharedClient();
        Request request = Server.requestBuilderWithApi("me")
                .method("get",null)
                .build();

        clien.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MeFragment.this.onFailuer(call,e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                try {
                    final User user = new ObjectMapper().readValue(response.body().bytes(), User.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MeFragment.this.onResponse(call, user);
                        }
                    });
                }catch (final Exception e){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MeFragment.this.onFailuer(call,e);
                        }
                    });
                }

            }
        });
    }

    protected void onResponse(Call call, User user){
        progressBar.setVisibility(View.GONE);
        avatarView.load(user);
        my_text.setVisibility(View.VISIBLE);
        my_text.setTextColor(Color.BLACK);
        my_text.setText("Hello" + user.getName());
    }

    protected void onFailuer(Call call, Exception ex){
        progressBar.setVisibility(View.GONE);
        my_text.setVisibility(View.VISIBLE);
        my_text.setTextColor(Color.RED);
        my_text.setText(ex.getMessage());
    }
}
