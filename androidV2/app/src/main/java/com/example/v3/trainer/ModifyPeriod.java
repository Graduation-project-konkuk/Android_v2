package com.example.v3.trainer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.v3.R;
import com.example.v3.trainer.dto.Period;

import okhttp3.MediaType;

public class ModifyPeriod extends AppCompatActivity {

    TextView add_period;

    Button add_period_button;
    Button close_period_button;

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_moditfy_period);

        Period addPeriod = new Period("");

        Intent intent = getIntent();

        add_period = findViewById(R.id.my_period);

        add_period_button = findViewById(R.id.add_period_button);
        add_period_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 여기서 저장 버튼 누르면 경력 기간 전송
                 */
                String period = add_period.getText().toString();
                addPeriod.setEx_period(period);
                intent.putExtra("PeriodDto",addPeriod);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



        close_period_button = findViewById(R.id.close_period_button);
        close_period_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }
}
