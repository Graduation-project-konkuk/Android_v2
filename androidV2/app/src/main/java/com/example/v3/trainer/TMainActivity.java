package com.example.v3.trainer;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.v3.R;
import com.example.v3.member.ExerciseFragment;
import com.example.v3.member.PlanFragment;
import com.example.v3.member.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class TMainActivity extends AppCompatActivity {

    LinearLayout home_layout;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tactivity_main);

        init();
        SettingListener(); // 리스너 등록

    }

    private void init(){
        home_layout = findViewById(R.id.t_home_layout);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.t_home_layout, new TrainerFragment())
                .commit();
        bottomNavigationView = findViewById(R.id.t_bottomNavigationView);
    }

    private void SettingListener() {
        bottomNavigationView.setOnItemSelectedListener(new
                TabSelectedListener());
    }

    class TabSelectedListener implements NavigationBarView.OnItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.t_tab_trainers: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.t_home_layout, new TrainerFragment())
                            .commit();
                    return true;
                }
            }
            return false;
        }
    }
}