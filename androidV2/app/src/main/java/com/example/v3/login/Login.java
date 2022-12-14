package com.example.v3.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.v3.Environment;
import com.example.v3.R;
import com.example.v3.member.MMainActivity;
import com.example.v3.member.chat.sms.SmsActivity;
import com.example.v3.trainer.TMainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    static final String TAG = "LOGIN";

    private Button loginBtn;
    private Button signupViewBtn;
    private EditText loginId;
    private EditText loginPwd;
    private RadioGroup radioGroupLogin;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    private boolean isMember = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);


        prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit = prefs.edit();

        loginBtn = (Button) findViewById(R.id.loginBtn);
        signupViewBtn = (Button) findViewById(R.id.signupViewBtn);

        loginId = (EditText) findViewById(R.id.loginId);
        loginPwd = (EditText) findViewById(R.id.loginPwd);

        radioGroupLogin = (RadioGroup) findViewById(R.id.radioGroupLogin);

        final Intent[] intent = new Intent[1];
        intent[0] = new Intent(getApplicationContext(), MMainActivity.class);

        final String[] intentFlag = new String[1];
        intentFlag[0] = "rgMemberRadio";

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(intentFlag[0].equals("rgMemberRadio")){
                    intent[0] = new Intent(getApplicationContext(), MMainActivity.class);
                    intent[0].addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                }else if(intentFlag[0].equals("rgTrainerRadio")){
                    intent[0] = new Intent(getApplicationContext(), TMainActivity.class);
                    intent[0].addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

//                  65~183
                String id = loginId.getText().toString().trim();
                String password = loginPwd.getText().toString().trim();

                System.out.println("id : " + id + "\n password : " + password);
                if (id.length() > 0 || password.length() > 0) {


                    // ?????? ???????????? ???????????? ????????? ???????????? ?????? sharedPreference ???????????????.
                    if(!prefs.getString("userName", "").equals(id)){
                        edit.clear();
                        edit.commit();
                    }

                    // get?????? ???????????? ??????
//                    HttpUrl.Builder urlBuilder = HttpUrl.parse("117.16.137.155:8080/user/signUp").newBuilder();
//                    urlBuilder.addQueryParameter("v", "1.0"); // ??????
//                    String url = urlBuilder.build().toString();
                    String json;
                    String flag;

                    if(prefs.getString("height", null) == null || prefs.getString("weight", null) == null){
                        flag = "no";
                        json = loginJson(id, password, flag);
                    } else{
                        flag = "yes";
                        json = loginJson(id, password, flag);
                    }
                    System.out.println(json);
                    RequestBody body = RequestBody.create(json, JSON);
                    // POST ???????????? ??????
//                    RequestBody formBody = new FormBody.Builder()
//                            .add("email", id)
//                            .add("password", password)
//                            .add("name", name)
//                            .build();

//                    Log.d("signup json: ", formBody.toString());
                    // ?????? ?????????
                    // MemberLogin, TrainerLogin url ????????????
                    OkHttpClient client = new OkHttpClient();
                    Request request;
                    System.out.println(isMember);
                    if(isMember){
                        request = new Request.Builder()
                                .url(Environment.ip+"/user/signIn")
                                .post(body)
                                .build();
                    }else{
                        request = new Request.Builder()
                                .url(Environment.ip+"/trainer/signIn")
                                .post(body)
                                .build();
                    }



//                    Response response = null;
//                    try {
//                        response = client.newCall(request).execute();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    System.out.println(response.body().toString());

                    // ?????? ??????
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            if (!response.isSuccessful()) {
                                // ?????? ??????
                                Log.i("tag", "????????????");
                                final String responseData = response.body().string();
                                JSONObject jsonObject = null;

                                /**
                                 * json ??????
                                 */
                                try {
                                    jsonObject = new JSONObject(responseData);
                                    edit.putString("token", response.header("Authorization"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Gson gson = new GsonBuilder()
                                        .setPrettyPrinting()
                                        .create();
                                String responseJson = gson.toJson(jsonObject);
                                Log.d("Login Response", responseJson);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Toast.makeText(getApplicationContext(), "????????? ?????? ??????????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                // ?????? ??????
                                Log.i("tag", "?????? ??????");
                                final String responseData = response.body().string();
                                // ?????? ????????? Ui ?????? ??? ?????? ??????
                                // ??????????????? Ui ??????

                                /**
                                 * ?????? ????????? ?????? ????????? ??????.
                                 */
                                System.out.println("header Token: " + response.header("Authorization"));
                                JSONObject jsonObject = null;

                                /**
                                 * json ??????
                                 */
                                try {
                                    jsonObject = new JSONObject(responseData);
                                    edit.putString("token", response.header("Authorization"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Gson gson = new GsonBuilder()
                                        .setPrettyPrinting()
                                                .create();
                                String responseJson = gson.toJson(jsonObject);
                                Log.d("Login Response", responseJson);

                                // ?????? ???????????? id ??????
                                edit.putString("userName", id);

                                if(flag.equals("no")){
                                    try {
                                        edit.putString("height", jsonObject.getString("height"));
                                        System.out.println("first height : " + jsonObject.getString("height"));
                                        edit.putString("weight", jsonObject.getString("weight"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                edit.commit();
                                System.out.println("header Token2: " + prefs.getString("token", ""));

//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        try {
//                                            Toast.makeText(getApplicationContext(), "??????" + responseData, Toast.LENGTH_SHORT).show();
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                });
//                                Intent intent = new Intent(getApplicationContext(), TMainActivity.class);
//                                startActivity(intent);
                                startActivity(intent[0]);
                            }
                        }
                    });

                } else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Toast.makeText(getApplicationContext(), "????????? ?????? ??????????????? ??????????????????!", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }


//


            }
        });

        signupViewBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(intentFlag[0].equals("rgMemberRadio")){
                    intent[0] = new Intent(getApplicationContext(), SignupMember.class);
                    intent[0].addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                }else if(intentFlag[0].equals("rgTrainerRadio")){
                    intent[0] = new Intent(getApplicationContext(), SignupTrainer.class);
                    intent[0].addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent[0]);
            }
        });

        radioGroupLogin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rgMemberRadio:
                        Log.d(TAG, "intent start1");
                        Log.d(TAG, "intent end1");
                        intentFlag[0] = "rgMemberRadio";
                        isMember = true;
                        break;
                    case R.id.rgTrainerRadio:
                        intentFlag[0] = "rgTrainerRadio";
                        isMember = false;
                        break;
                }
            }
        });
    }

    public String loginJson(String id, String password, String flag){
        return "{\"email\":\""+ id +"\","
                + "\"password\":\"" + password + "\","
                + "\"flag\":\"" + flag + "\"}";
    }

}
