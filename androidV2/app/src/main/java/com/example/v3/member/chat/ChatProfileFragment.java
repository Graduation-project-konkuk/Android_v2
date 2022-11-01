package com.example.v3.member.chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v3.R;
import com.example.v3.member.chat.video.VideoList;
import com.example.v3.member.chat.video.YoutubeAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ChatProfileFragment extends AppCompatActivity {

    static final String TAG = "ChatProfileFragment";

    ImageView mlayout_chat_profile_img;
    TextView mlayout_chat_profile_name;
    TextView mlayout_chat_profile_sex;
    TextView mlayout_chat_profile_historyPeriod;
    TextView mlayout_chat_profile_introduction;

    RecyclerView recyclerView;

    ArrayList<VideoList> videoLists;

    /**
     * REST랑 id 이용해서 트레이너 정보 가져와서 출력해야함
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mlayout_chat_profile);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        mlayout_chat_profile_img = (ImageView) findViewById(R.id.mlayout_chat_profile_img);
        mlayout_chat_profile_name = (TextView) findViewById(R.id.mlayout_chat_profile_name);
        mlayout_chat_profile_sex = (TextView) findViewById(R.id.mlayout_chat_profile_sex);
        mlayout_chat_profile_historyPeriod = (TextView) findViewById(R.id.mlayout_chat_profile_historyPeriod);
        mlayout_chat_profile_introduction = (TextView) findViewById(R.id.mlayout_chat_profile_introduction);

        recyclerView = (RecyclerView) findViewById(R.id.mlayout_chat_profile_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        videoLists = new ArrayList<>();

        VideoList VideoList = new VideoList("https://www.youtube.com/embed/UIXcKIz_UDA");
        videoLists.add(VideoList);
        VideoList = new VideoList("https://www.youtube.com/embed/UIXcKIz_UDA");
        videoLists.add(VideoList);
        VideoList = new VideoList("https://www.youtube.com/embed/9ouC5a_la4g");
        videoLists.add(VideoList);
        VideoList = new VideoList("https://www.youtube.com/embed/7YoE0xCMdy0");
        videoLists.add(VideoList);
        VideoList = new VideoList("https://www.youtube.com/embed/8OnXkproxuE");
        videoLists.add(VideoList);
        VideoList = new VideoList("https://www.youtube.com/embed/2OG0Z7hZQao");
        videoLists.add(VideoList);

        YoutubeAdapter youtubeAdapter = new YoutubeAdapter(videoLists,getApplicationContext());
        recyclerView.setAdapter(youtubeAdapter);


    }
}