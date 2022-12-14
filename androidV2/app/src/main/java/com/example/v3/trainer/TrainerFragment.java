package com.example.v3.trainer;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.v3.Environment;
import com.example.v3.R;
import com.example.v3.member.plan_adapter.AddPlan;
import com.example.v3.member.plan_adapter.PlanItem;
import com.example.v3.member.plan_adapter.dto.AddPlanDto;
import com.example.v3.trainer.dto.Period;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class TrainerFragment extends Fragment {
//    private TextView testUserInfo;
    private String name;
    private TextView name_txt;
    private String gender;
    private TextView gender_txt;
    private String period;
    private TextView period_txt;
    private String introduction;
    private TextView introduction_txt;
    private String url;
    private EditText url_txt;
//    private Button period_change;
    private Button url_add;

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

//    private ActivityResultLauncher<Intent> move_period;


    // ????????? ??? ????????? (??????,???,????????????)
//    String name = null;
//    String gender = null;
//    String period = null;
//    String introduction = null;
//    String url = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tfragment_user, container, false);

        name_txt = v.findViewById(R.id.trainer_name_txt);
        gender_txt = v.findViewById(R.id.trainer_sex);
        period_txt = v.findViewById(R.id.ex_period);
        url_txt = v.findViewById(R.id.youtube_url);
        introduction_txt = v.findViewById(R.id.trainer_introduction_txt);

//        period_change = v.findViewById(R.id.period_change);

        prefs = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit = prefs.edit();
        System.out.println("0");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://211.46.24.58:8080/trainer/info")
                .addHeader("Authorization", prefs.getString("token",""))
                .build();
        System.out.println(prefs.getString("token",""));
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
                } else {
                    // ?????? ??????
                    Log.i("tag", "?????? ??????");
                    final String responseData = response.body().string();
                    // ?????? ????????? Ui ?????? ??? ?????? ??????
                    // ??????????????? Ui ??????

                    Log.d("trainerInfo : ", responseData);

                    JSONObject jsonObject = null;
                    String data = null;



                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    try {
                        jsonObject = new JSONObject(responseData);
                        System.out.println(jsonObject.toString());
                        data = gson.toJson(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        String dataForm = jsonObject.getString("data");
                        JSONObject dataObject = new JSONObject(dataForm);
                        name = dataObject.getString("name");
                        gender = dataObject.getString("gender");
                        period = dataObject.getString("period");
                        introduction = dataObject.getString("introduction");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                name_txt.setText(name);
                                gender_txt.setText(gender);
                                period_txt.setText(period);
                                introduction_txt.setText(introduction);
                            }
                        });
                    }
                    Log.d("data : ", data);
                }
            }
        });
/**
 * ???????????? ?????? ??????
 */
  /*      move_period = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent intent = result.getData();
                            Period period = (Period) intent.getSerializableExtra("PeriodDto");
                            period_txt.setText(period.getEx_period());
                            edit.putString("period", period.getEx_period());

                            edit.commit();
                        }
                    }
                });
        period_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyPeriod.class);
                move_period.launch(intent);
            }
        });
*/
        url_add = v.findViewById(R.id.url_add);

        url_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //url_txt??? ???????????? ???????????? ????????? ????????? url ????????? insert

                url = url_txt.getText().toString().trim();

                String json = addUrlJson(url);
                RequestBody body = RequestBody.create(json, JSON);

                // ?????? ?????????
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Environment.ip + "/trainer/upload")
                        .addHeader("Authorization", prefs.getString("token", ""))
                        .post(body)
                        .build();


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
                        } else {
                            // ?????? ??????
                            Log.i("tag", "?????? ??????");

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Toast.makeText(getActivity().getApplicationContext(), "?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                    }


                    /**
                     * API??????
                     */
//        prefs = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        edit = prefs.edit();
//        System.out.println("0");
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://59.6.66.212:8080/user/info")
//                .addHeader("Authorization", prefs.getString("token",""))
//                .build();
//        System.out.println(prefs.getString("token",""));
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//                if (!response.isSuccessful()) {
//                    // ?????? ??????
//                    Log.i("tag", "????????????");
//                } else {
//                    // ?????? ??????
//                    Log.i("tag", "?????? ??????");
//                    final String responseData = response.body().string();
//                    // ?????? ????????? Ui ?????? ??? ?????? ??????
//                    // ??????????????? Ui ??????
//
//                    Log.d("userInfo : ", responseData);
//
//                    JSONObject jsonObject = null;
//                    String data = null;
//
//
//                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                    try {
//                        jsonObject = new JSONObject(responseData);
//                        System.out.println(jsonObject.toString());
//                        data = gson.toJson(jsonObject);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        String dataForm = jsonObject.getString("data");
//                        JSONObject dataObject = new JSONObject(dataForm);
////                        email = dataObject.getString("email");
////                        name = dataObject.getString("name");
////                        String bodyInfoForm = dataObject.getString("bodyInfo");
////                        JSONObject bodyInfoObject = new JSONObject(bodyInfoForm);
////                        weight = bodyInfoObject.getString("weight");
////                        height = bodyInfoObject.getString("height");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    if(getActivity() != null){
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                user_email_txt.setText(email);
////                                user_name_txt.setText(name);
////                                user_weight_txt.setText(weight+"kg");
////                                user_height_txt.setText(height+"cm");
//                            }
//                        });
//                    }
//                    Log.d("data : ", data);
//                }
//            }
//        });
                });
            }
        });

        return v;
    }
    public String addUrlJson(String url) {
        return "{\"url\":\"" + url + "\"}";
    }
}