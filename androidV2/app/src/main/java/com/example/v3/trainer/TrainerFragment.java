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
import android.widget.TextView;
import android.widget.Toast;

import com.example.v3.R;
import com.example.v3.member.plan_adapter.AddPlan;
import com.example.v3.member.plan_adapter.PlanItem;
import com.example.v3.member.plan_adapter.dto.AddPlanDto;
import com.example.v3.trainer.dto.Period;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class TrainerFragment extends Fragment {
    private TextView testUserInfo;
    private TextView name_txt;
    private TextView sex_txt;
    private TextView period_txt;
    private TextView url_txt;
    private Button period_change;
    private Button url_add;

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    private ActivityResultLauncher<Intent> move_period;


    // 텍스트 뷰 수정값 (이름,성,경력기간)
    String name = null;
    String sex = null;
    String period = null;
//    String url = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.tfragment_user, container, false);

        name_txt = v.findViewById(R.id.trainer_name);
        sex_txt = v.findViewById(R.id.trainer_sex);
        period_txt = v.findViewById(R.id.ex_period);
        url_txt = v.findViewById(R.id.youtube_url);

        period_change = v.findViewById(R.id.period_change);

        prefs = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit = prefs.edit();
/**
 * 액티비티 콜백 함수
 */
        move_period = registerForActivityResult(
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

        url_add = v.findViewById(R.id.url_add);

        url_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //url_txt를 트레이너 엔티티의 유튜브 업로드 url 속성에 insert
                Toast.makeText(getActivity().getApplicationContext(), "영상 등록 성공", Toast.LENGTH_SHORT).show();
            }
        });


        /**
         * API호출
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
//                    // 응답 실패
//                    Log.i("tag", "응답실패");
//                } else {
//                    // 응답 성공
//                    Log.i("tag", "응답 성공");
//                    final String responseData = response.body().string();
//                    // 서브 스레드 Ui 변경 할 경우 에러
//                    // 메인스레드 Ui 설정
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


        return v;
    }
}
