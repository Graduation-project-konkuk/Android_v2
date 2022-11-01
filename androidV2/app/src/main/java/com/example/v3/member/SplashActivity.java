package com.example.v3.member;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.v3.login.Login;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main); theme로 지정

        moveTitle(1);
    }

    private void moveTitle(int sec) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), Login.class);

                startActivity(intent);

                finish();
            }
        }, 1000 * sec);
    }
}