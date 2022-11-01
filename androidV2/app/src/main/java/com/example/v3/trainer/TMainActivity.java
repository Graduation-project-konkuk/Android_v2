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
        setContentView(R.layout.mactivity_main);

        init();
        SettingListener(); // 리스너 등록

    }

    private void init(){
        home_layout = findViewById(R.id.home_layout);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_layout, new ExerciseFragment())
                .commit();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    private void SettingListener() {
        bottomNavigationView.setOnItemSelectedListener(new
                TabSelectedListener());
    }

    class TabSelectedListener implements NavigationBarView.OnItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.tab_exercises: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_layout, new ExerciseFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_plans: {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_layout, new PlanFragment())
                            .commit();
                    return true;
                }
                case R.id.tab_users:{
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.home_layout, new UserFragment())
                            .commit();
                    return true;
                }
            }
            return false;
        }
    }
}