package com.example.v3.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.v3.Environment;
import com.example.v3.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignupTrainer extends AppCompatActivity {

    private EditText tSignupId;
    private EditText tSignupPwd;
    private EditText tSignupName;
    private EditText tSignupHeight;
    private EditText tSignupWeight;
    private EditText tSignupSex;
    private EditText tSignupPhone;
    private EditText tSignupHistoryPeriod;
    private EditText tSignupIntroduction;
    private ImageView tSignupFeature;
    private ImageView tSignupProfileImg;
    private ImageView tSignupResumeImg;

    private static final int REQUEST_CODE = 0;

    private String featureString;

    private Button tSignupBtn;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_trainer);

        tSignupId = (EditText) findViewById(R.id.tSignupId);
        tSignupPwd = (EditText) findViewById(R.id.tSignupPwd);
        tSignupName = (EditText) findViewById(R.id.tSignupName);
        tSignupHeight = (EditText) findViewById(R.id.tSignupHeight);
        tSignupWeight = (EditText) findViewById(R.id.tSignupWeight);
        tSignupSex = (EditText) findViewById(R.id.tSignupSex);
        tSignupPhone = (EditText) findViewById(R.id.tSignupPhone);
        tSignupHistoryPeriod = (EditText) findViewById(R.id.tSignupHistoryPeriod);
        tSignupIntroduction = (EditText) findViewById(R.id.tSignupIntroduction);
        tSignupFeature = (ImageView) findViewById(R.id.tSignupFeature);
//        tSignupProfileImg = (ImageView) findViewById(R.id.tSignupProfileImg);
//        tSignupResumeImg = (ImageView) findViewById(R.id.tSignupResumeImg);


        tSignupBtn = (Button) findViewById(R.id.tSignupBtn);

        // 회원가입 버튼 클릭
        tSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 60 ~ 134
                String email = tSignupId.getText().toString().trim();
                String password = tSignupPwd.getText().toString().trim();
                String name = tSignupName.getText().toString().trim();
                String height = tSignupHeight.getText().toString().trim();
                String weight = tSignupWeight.getText().toString().trim();
                String sex = tSignupSex.getText().toString().trim();
                String phone = tSignupPhone.getText().toString().trim();
                String period = tSignupHistoryPeriod.getText().toString().trim();
                String introduction = tSignupIntroduction.getText().toString().trim();

                if (email.length() > 0 || password.length() > 0 || name.length() > 0 || height.length() > 0  || weight.length() > 0 || sex.length() > 0 || phone.length() > 0 || period.length() > 0 || introduction.length() > 0 || featureString.length() > 0) {


                    // get방식 파라미터 추가
//                    HttpUrl.Builder urlBuilder = HttpUrl.parse("117.16.137.155:8080/user/signUp").newBuilder();
//                    urlBuilder.addQueryParameter("v", "1.0"); // 예시
//                    String url = urlBuilder.build().toString();
                    String json = signupJson(email, password, name, height, weight, sex, phone, period, introduction, featureString);
                    System.out.println(json);
                    RequestBody body = RequestBody.create(json, JSON);
                    // POST 파라미터 추가
//                    RequestBody formBody = new FormBody.Builder()
//                            .add("email", id)
//                            .add("password", password)
//                            .add("name", name)
//                            .build();

//                    Log.d("signup json: ", formBody.toString());
                    // 요청 만들기
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(Environment.ip+"/trainer/signUp")
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
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(getApplicationContext(), "회원가입 실패!", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                // 응답 성공
                                Log.i("tag", "응답 성공");
                                final String responseData = response.body().string();
                                // 서브 스레드 Ui 변경 할 경우 에러
                                // 메인스레드 Ui 설정
                                finish();

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(getApplicationContext(), "회원가입 완료!", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        }
                    });

                    // 회원가입 종료 시점
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(getApplicationContext(), "입력 사항을 마저 기재해주세요!", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                finish();
            }
        });

        tSignupFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    featureString = new BufferedReader(
                            new InputStreamReader(in, StandardCharsets.UTF_8))
                            .lines()
                            .collect(Collectors.joining("\n"));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT);
            }
        }
    }

    public String signupJson(String email, String password, String name, String height, String weight, String sex, String phone, String period, String introduction, String featureString){

        return "{\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\","
                + "\"name\":\"" + name + "\","
                + "\"height\":\"" + height + "\","
                + "\"weight\":\"" + weight + "\","
                + "\"sex\":\"" + sex + "\","
                + "\"phone\":\"" + phone + "\","
                + "\"period\":\"" + period + "\","
                + "\"introduction\":\"" + introduction + "\","
                + "\"feature\":\"" + featureString + "\"}";
    }
}
