package com.example.v3.member.plan_adapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v3.Environment;
import com.example.v3.R;
import com.example.v3.member.plan_adapter.dto.AddPlanDto;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPlan extends AppCompatActivity {

    Spinner spinner;

    Button close_add_button;
    Button add_plan_button;
    TextView add_weight;
    TextView add_reps;
    TextView add_date;

    private AddPlanDto addPlanDto;

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_plan);

        final String[] add_exercise = {""};

        spinner = (Spinner) findViewById(R.id.add_exercise);
        ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(this, R.array.exercise, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                add_exercise[0] = sAdapter.getItem(position).toString();

                Toast.makeText(AddPlan.this,
                        sAdapter.getItem(position),Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Intent intent = getIntent();

        add_weight = findViewById(R.id.add_weight);
        add_reps = findViewById(R.id.add_reps);

        addPlanDto = new AddPlanDto("", "", "");


        prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit = prefs.edit();

        add_plan_button = findViewById(R.id.add_plan_button);
        add_plan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 여기서 저장버튼 누르면 위에 네가지 textview들 서버로 전송
                 * PlanAdapter에도 즉시 반영하여 업데이트 한다.
                 */
                String saveWeight = add_weight.getText().toString().trim();
                String saveExercise = add_exercise[0];
                String saveReps = add_reps.getText().toString().trim();

                if (saveWeight.length() > 0 || saveExercise.length() > 0 || saveReps.length() > 0) {

                    String json = addPlanJson(saveWeight, saveExercise, saveReps);
                    System.out.println(json);
                    RequestBody body = RequestBody.create(json, JSON);

//                    RequestBody body = new FormBody.Builder()
//                            .add("localDate", saveDate)
//                            .add("weight", saveWeight)
//                            .add("exerciseName", saveExercise)
//                            .add("reps", saveReps)
//                            .build();

                    Log.d("data : ", saveWeight);
                    Log.d("data : ", saveExercise);
                    Log.d("data : ", saveReps);



                    // 요청 만들기
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(Environment.ip+"/plan/user")
                            .addHeader("Authorization", prefs.getString("token",""))
                            .post(body)
                            .build();

//                    Response response = null;
//                    try {
//                        response = client.newCall(request).execute();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(response.body().toString());

                    // 응답 콜백
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

                                addPlanDto.saveAll(saveWeight, saveExercise, saveReps);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(getApplicationContext(), responseData.substring(1, responseData.length()-1), Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                // 이전으로 돌아가기
                                intent.putExtra("addPlanDto", addPlanDto);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    });

                }


            }
        });



        close_add_button = findViewById(R.id.close_add_button);
        close_add_button.setOnClickListener(new View.OnClickListener() {
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

    public String addPlanJson(String saveWeight, String saveExercise, String saveReps){
        return "{\"weight\":\"" + saveWeight + "\","
                + "\"exerciseName\":\"" + saveExercise + "\","
                + "\"reps\":\"" + saveReps +"\"}";
    }


}
