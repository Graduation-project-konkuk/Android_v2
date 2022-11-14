package com.example.v3.member.poseCamera;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.v3.R;

import org.json.JSONException;
import org.json.JSONObject;

public class PoseResult extends AppCompatActivity {

    private final String TAG = "PoseResult";
    private TextView pose_result_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pose_result);

//        pose_result_txt = (TextView) findViewById(R.id.pose_result_txt);

//        Intent poseResult = getIntent();
//        String modelResult = poseResult.getStringExtra("modelResult");


//        pose_result_txt.setText(modelResult);

        Intent poseResult = getIntent();
        int modelLength = Integer.parseInt(poseResult.getStringExtra("modelLength"));
        String modelResponse = poseResult.getStringExtra("modelResponse");
        Log.d(TAG, "response:\n" + modelResponse);

        try {
            JSONObject jsonObject = new JSONObject(modelResponse);
            String condition = jsonObject.getString("Condition1");
            JSONObject jsonObject1 = new JSONObject(condition);
            String state1 = jsonObject1.getString("state");
            String value1 = checkLevel(jsonObject1.getString("value"));
            TextView textView11 = findViewById(R.id.text11); textView11.setText(state1);
            TextView textView12 = findViewById(R.id.text12); textView12.setText(value1);

            String condition2 = jsonObject.getString("Condition2");
            JSONObject jsonObject2 = new JSONObject(condition2);
            String state2 = jsonObject2.getString("state");
            String value2 = checkLevel(jsonObject2.getString("value"));
            TextView textView21 = findViewById(R.id.text21); textView21.setText(state2);
            TextView textView22 = findViewById(R.id.text22); textView22.setText(value2);

            String condition3 = jsonObject.getString("Condition3");
            JSONObject jsonObject3 = new JSONObject(condition3);
            String state3 = jsonObject3.getString("state");
            String value3 = checkLevel(jsonObject3.getString("value"));
            TextView textView31 = findViewById(R.id.text31); textView31.setText(state3);
            TextView textView32 = findViewById(R.id.text32); textView32.setText(value3);
            if(modelLength >= 4){
                String condition4 = jsonObject.getString("Condition4");
                JSONObject jsonObject4 = new JSONObject(condition4);
                String state4 = jsonObject4.getString("state");
                String value4 = checkLevel(jsonObject4.getString("value"));
                TextView textView41 = findViewById(R.id.text41); textView41.setText(state4);
                TextView textView42 = findViewById(R.id.text42); textView42.setText(value4);
            }else{
                TextView textView41 = findViewById(R.id.text41); textView41.setText("");
                TextView textView42 = findViewById(R.id.text42); textView42.setText("");
            }

            if(modelLength >= 5){
                String condition5 = jsonObject.getString("Condition5");
                JSONObject jsonObject5 = new JSONObject(condition5);
                String state5 = jsonObject5.getString("state");
                String value5 = checkLevel(jsonObject5.getString("value"));
                TextView textView51 = findViewById(R.id.text51); textView51.setText(state5);
                TextView textView52 = findViewById(R.id.text52); textView52.setText(value5);
            }else{
                TextView textView51 = findViewById(R.id.text51); textView51.setText("");
                TextView textView52 = findViewById(R.id.text52); textView52.setText("");
            }


        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private String checkLevel(String input){
        Double value = Double.parseDouble(input);
        if(value>=0.5){
            return "좋음";
        }
        else if(0<value && value<0.5){
            return "보통";
        }
        else{
            return "나쁨";
        }

    }
}
