package com.example.v3.member.exerciseList;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.v3.R;
import com.example.v3.member.exerciseList.adapter.ExerciseAdapter;
import com.example.v3.member.exerciseList.adapter.ExerciseItem;
import com.example.v3.member.exerciseList.adapter.OnExerciseItemClickListener;
import com.example.v3.member.poseCamera.PoseEstimationMain;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class ExerciseLowerList extends AppCompatActivity {
    static public String x;

    Button camera_btn;
    Button cancel_pose_fragment_btn;
    View exercise_list_layout;
    View pose_fragment;

    RecyclerView recyclerView;
    ExerciseAdapter adapter;

    static final String TAG = "LowerRecyclerView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_list);

        exercise_list_layout = (View) findViewById(R.id.exercise_list_layout);
        pose_fragment = (View) findViewById(R.id.pose_fragment);

        camera_btn = (Button) findViewById(R.id.camera_btn);
        cancel_pose_fragment_btn = (Button) findViewById(R.id.cancel_pose_fragment_btn);

        recyclerView = (RecyclerView) findViewById(R.id.exerciseRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ExerciseAdapter();

        Log.d(TAG, "activate 1");
        // 운동 종류 입력
        adapter.addItem(new ExerciseItem("스텝 포워드 다이나믹 런지",R.raw.step_forward_dynamic_lunge));
        adapter.addItem(new ExerciseItem("스텝 백워드 다이나믹 런지",R.raw.step_backward_dynamic_lunge));
        adapter.addItem(new ExerciseItem("사이드 런지",R.raw.side_lunge));
        adapter.addItem(new ExerciseItem("크로스 런지",R.raw.cross_lunge));
        adapter.addItem(new ExerciseItem("시저크로스",R.raw.no_caesar_cross));
        adapter.addItem(new ExerciseItem("힙쓰러스트",R.raw.no_hit_thrust));
        Log.d(TAG, "activate 2");
        Log.d(TAG, "activate 3" + adapter.getItemCount());
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnExerciseItemClickListener() {
            @Override
            public void onItemClick(ExerciseAdapter.ViewHolder holder, View view, int position) {
                exercise_list_layout.setBackgroundColor(Color.parseColor("#80000000"));
                recyclerView.setBackgroundColor(Color.parseColor("#80000000"));

                x = adapter.getItem(position).getName();

                exercise_list_layout.setClickable(false);

                pose_fragment.setVisibility(View.VISIBLE);
                pose_fragment.setClickable(true);
            }
        });


        camera_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                exercise_list_layout.setBackgroundColor(Color.parseColor("#00000000"));
                exercise_list_layout.setClickable(true);

                pose_fragment.setVisibility(View.GONE);
                pose_fragment.setClickable(false);

                Intent intent = new Intent(getApplicationContext(), PoseEstimationMain.class);
                intent.putExtra("name",x);
                startActivity(intent);
            }
        });

        cancel_pose_fragment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_list_layout.setBackgroundColor(Color.parseColor("#00000000"));
                exercise_list_layout.setClickable(true);

                pose_fragment.setVisibility(View.GONE);
                pose_fragment.setClickable(false);
            }
        });

    }


}
