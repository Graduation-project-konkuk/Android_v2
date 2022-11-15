package com.example.v3.member.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v3.Environment;
import com.example.v3.R;
import com.example.v3.member.chat.video.VideoList;
import com.example.v3.member.chat.video.YoutubeAdapter;
import com.example.v3.member.chat.sms.SmsActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatProfile extends AppCompatActivity {

    static final String TAG = "ChatProfileFragment";

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    ImageView mlayout_chat_profile_img;
    TextView mlayout_chat_profile_name;
    TextView mlayout_chat_profile_sex;
    TextView mlayout_chat_profile_historyPeriod;
    TextView mlayout_chat_profile_introduction;

    RecyclerView recyclerView;

    Button mlayout_chat_profile_message;

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

        prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit = prefs.edit();

        mlayout_chat_profile_img = (ImageView) findViewById(R.id.mlayout_chat_profile_img);
        mlayout_chat_profile_name = (TextView) findViewById(R.id.mlayout_chat_profile_name);
        mlayout_chat_profile_sex = (TextView) findViewById(R.id.mlayout_chat_profile_sex);
        mlayout_chat_profile_historyPeriod = (TextView) findViewById(R.id.mlayout_chat_profile_historyPeriod);
        mlayout_chat_profile_introduction = (TextView) findViewById(R.id.mlayout_chat_profile_introduction);

        recyclerView = (RecyclerView) findViewById(R.id.mlayout_chat_profile_recycler_view);

        mlayout_chat_profile_message = (Button) findViewById(R.id.mlayout_chat_profile_message);

        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        /**
         * 단건 트레이너 프로필 조회 : GET
         */

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Environment.ip+"/trainer/profile?id="+id)
                .addHeader("Authorization", prefs.getString("token",""))
                .build();
        System.out.println(prefs.getString("token",""));
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // 응답 실패
                    Log.i("tag", "응답실패");
                } else {
                    // 응답 성공
                    Log.i("tag", "응답 성공");
                    final String responseData = response.body().string();
                    // 서브 스레드 Ui 변경 할 경우 에러
                    // 메인스레드 Ui 설정

                    Log.d(TAG, "responseData\n" + responseData);

                    JSONObject jsonObject = null;
                    String data = null;


                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    try {
                        jsonObject = new JSONObject(responseData);
                        Log.d(TAG, "jsonObject\n" + jsonObject.toString());
                        data = gson.toJson(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //"name":name, "gender":gender, "period":period, "introduction":introduction, "video":[1,2,3,4]

                    try {

                        String name = jsonObject.getString("name");
                        String gender = jsonObject.getString("gender");
                        String period = jsonObject.getString("period");
                        String introduction = jsonObject.getString("introduction");

                        ArrayList<VideoList> videoLists = new ArrayList<>();
                        JSONArray arrayData = jsonObject.getJSONArray("video");
                        for(int i=0; i<arrayData.length(); i++){
                            videoLists.add(new VideoList(arrayData.getString(i)));
                        }

                        String image = jsonObject.getString("image");
                        byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
                        Bitmap imageBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                        // recyclerView만들기
                        mlayout_chat_profile_name.setText(name);
                        mlayout_chat_profile_sex.setText(gender);
                        mlayout_chat_profile_historyPeriod.setText(period);
                        mlayout_chat_profile_introduction.setText(introduction);
                        mlayout_chat_profile_img.setImageBitmap(imageBitmap);
                        YoutubeAdapter youtubeAdapter = new YoutubeAdapter(videoLists,getApplicationContext());
                        recyclerView.setAdapter(youtubeAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

//        videoLists = new ArrayList<>();
//
//        VideoList VideoList = new VideoList("https://www.youtube.com/watch?v=0uixp1vmKKY");
//        videoLists.add(VideoList);
//        VideoList = new VideoList("https://www.youtube.com/watch?v=Fs-UgFmRX_I");
//        videoLists.add(VideoList);
////        VideoList = new VideoList("https://www.youtube.com/embed/9ouC5a_la4g");
////        videoLists.add(VideoList);
////        VideoList = new VideoList("https://www.youtube.com/embed/7YoE0xCMdy0");
////        videoLists.add(VideoList);
////        VideoList = new VideoList("https://www.youtube.com/embed/8OnXkproxuE");
////        videoLists.add(VideoList);
////        VideoList = new VideoList("https://www.youtube.com/embed/2OG0Z7hZQao");
////        videoLists.add(VideoList);
//
//        YoutubeAdapter youtubeAdapter = new YoutubeAdapter(videoLists,getApplicationContext());
//        recyclerView.setAdapter(youtubeAdapter);

        mlayout_chat_profile_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SmsActivity.class);
                startActivity(intent);
            }
        });

    }
}
