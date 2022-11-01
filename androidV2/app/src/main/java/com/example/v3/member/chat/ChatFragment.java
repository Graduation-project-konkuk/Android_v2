package com.example.v3.member.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v3.R;

public class ChatFragment extends Fragment {

    static final String TAG = "ChatFragment";

    RecyclerView recyclerView;
    ChatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mfragment_chat, container, false);

        recyclerView = v.findViewById(R.id.chat_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter();

        /**
         * 트레이너 dummy data 입력
         */
        Log.d(TAG, "activate dummy1");
//        adapter.addItem(new ChatItem());
//        adapter.addItem(new ChatItem());
//        adapter.addItem(new ChatItem());
        Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.trainer);
        // 1번 파라미터에 아이디 넣어야함
        adapter.addItem(new ChatItem(String.valueOf((int)(Math.random()*100)), icon, "dummyTrainer1", "남자", "2020.12.23~2022.10.10"));
        Log.d(TAG, "finish dummy1");
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnChatItemClickListener() {
            @Override
            public void onItemClick(ChatAdapter.ViewHolder holder, View view, int position, String id) {
                Log.d("ChatFragment", "Intent Start!");
                Intent intent = new Intent(getContext(), ChatProfileFragment.class);
                intent.putExtra("id", "12345");
                startActivity(intent);
                Log.d("ChatFragment", "Intent Stop!");
            }
        });



        return v;
    }
}
