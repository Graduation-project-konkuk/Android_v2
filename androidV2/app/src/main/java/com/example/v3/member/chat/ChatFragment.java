package com.example.v3.member.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v3.Environment;
import com.example.v3.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChatFragment extends Fragment {

    static final String TAG = "ChatFragment";

    RecyclerView recyclerView;
    ChatAdapter adapter;

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mfragment_chat, container, false);

        recyclerView = v.findViewById(R.id.chat_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter();

        prefs = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit = prefs.edit();

        /**
         * 모든 트레이너 정보 조회 : GET
         */
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://211.46.24.58:8080/trainers")
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

//                    {
//                        "count": 1,
//                            "data": [
//                        {
//                            "id": 26,
//                                "name": "씨발",
//                                "gender": "남자",
//                                "period": "11-14~11-15",
//                                "introduction": "이런 씨발련이",
//                                "image": null
//                        }
//                        ]
//                    }
                    try {
                        int count = jsonObject.getInt("count");
                        JSONArray arrayData = jsonObject.getJSONArray("data");
                        for(int i = 0 ;  i < count ; i++){
                            JSONObject dataObject = arrayData.getJSONObject(i);
                            long id = dataObject.getLong("id");
                            String name = dataObject.getString("name");
                            String gender = dataObject.getString("gender");
                            String period = dataObject.getString("period");
//                            String image = dataObject.getString("image");

                            // String to Bitmap
//                            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
//                            Bitmap imageBitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            // Dummy Image 입력
                            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.trainer);

                            adapter.addItem(new ChatItem(id, icon, name, gender, period));
                        }
                        if(getActivity() != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setAdapter(adapter);
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        /**
         * 트레이너 dummy data 입력
         */
        Log.d(TAG, "activate dummy1");
//        adapter.addItem(new ChatItem());
//        adapter.addItem(new ChatItem());
//        adapter.addItem(new ChatItem());
//        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.trainer);
        // 1번 파라미터에 아이디 넣어야함
//        adapter.addItem(new ChatItem(String.valueOf((int)(Math.random()*100)), icon, "흑자헬스", "남자", "2012.12.23~2022.10.10"));
        Log.d(TAG, "finish dummy1");

        adapter.setOnItemClickListener(new OnChatItemClickListener() {
            @Override
            public void onItemClick(ChatAdapter.ViewHolder holder, View view, int position, long id) {
                Log.d("ChatFragment", "Intent Start!");
                Intent intent = new Intent(getContext(), ChatProfile.class);
                Log.d("ChatFragment", String.valueOf(id));
                intent.putExtra("id", String.valueOf(id));
                startActivity(intent);
                Log.d("ChatFragment", "Intent Stop!");
            }
        });



        return v;
    }
}
