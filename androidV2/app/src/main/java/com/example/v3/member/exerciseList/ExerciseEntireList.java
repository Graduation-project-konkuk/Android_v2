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
public class ExerciseEntireList extends AppCompatActivity {
    static public String x;

    Button camera_btn;
    Button cancel_pose_fragment_btn;
    View exercise_list_layout;
    View pose_fragment;

    RecyclerView recyclerView;
    ExerciseAdapter adapter;

    static final String TAG = "EntireRecyclerView";
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
        adapter.addItem(new ExerciseItem("버피 테스트",R.raw.burpee_test));
        adapter.addItem(new ExerciseItem("플랭크",R.raw.plank));
        adapter.addItem(new ExerciseItem("굿모닝",R.raw.goodmorning));
        adapter.addItem(new ExerciseItem("푸쉬업",R.raw.push_up));
        adapter.addItem(new ExerciseItem("니푸쉬업",R.raw.knee_push_up));
        adapter.addItem(new ExerciseItem("Y-Exercise",R.raw.y_exercise));
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
