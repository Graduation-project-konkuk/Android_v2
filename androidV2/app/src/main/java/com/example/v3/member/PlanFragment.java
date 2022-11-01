package com.example.v3.member;


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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.v3.R;
import com.example.v3.member.plan_adapter.AddPlan;
import com.example.v3.member.plan_adapter.PlanAdapter;
import com.example.v3.member.plan_adapter.PlanItem;
import com.example.v3.member.plan_adapter.dto.AddPlanDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
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


public class PlanFragment extends Fragment {
    static final String TAG = "PlanRecyclerview";

    RecyclerView recyclerView;
    PlanAdapter adapter;
    Button add_plan;

    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;

    private TextView cur_weight;
    private TextView max_weight;
    private TextView min_weight;
    private TextView bmi;
    private TextView height;

    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.mfragment_plan, container, false);

        add_plan = v.findViewById(R.id.plan_addbutton);

        prefs = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit = prefs.edit();

        cur_weight = (TextView) v.findViewById(R.id.cur_weight);
        max_weight = (TextView) v.findViewById(R.id.max_weight);
        min_weight = (TextView) v.findViewById(R.id.min_weight);
        bmi = (TextView) v.findViewById(R.id.bmi);
        height = (TextView) v.findViewById(R.id.height);

        recyclerView = v.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlanAdapter();

        fillStatus();

        /**
         * 모든 운동 기록 가져온다.
         */

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://59.6.66.212:8080/plan/user")
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
                    // 응답 실패
                    Log.i("tag", "응답실패");
                } else {
                    // 응답 성공
                    Log.i("tag", "응답 성공");
                    final String responseData = response.body().string();
                    // 서브 스레드 Ui 변경 할 경우 에러
                    // 메인스레드 Ui 설정

                    Log.d("userInfo : ", responseData);

                    JSONObject jsonObject = null;
                    String data = null;
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    try {
                        jsonObject = new JSONObject(responseData);
                        data = jsonObject.getString("data");
                        data = gson.toJson(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(jsonObject.length() > 0){
                        Log.d("data : ", data);
                        System.out.println(data);
                        try {
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            int maxi_weight = Integer.parseInt(prefs.getString("max_weight", "0"));
                            int mini_weight = Integer.parseInt(prefs.getString("min_weight", "0"));


                            for(int i = 0 ; i < dataArray.length() ; i++){
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                String localDate = dataObject.getString("localDate");
                                String weight = dataObject.getString("weight") + "kg";
                                if(Integer.parseInt(dataObject.getString("weight")) > maxi_weight){
                                    maxi_weight = Integer.parseInt(dataObject.getString("weight"));
                                    edit.putString("max_weight", dataObject.getString("weight"));
                                    max_weight.setText(String.valueOf(maxi_weight));
                                }
                                if(Integer.parseInt(dataObject.getString("weight")) < mini_weight){
                                    mini_weight = Integer.parseInt(dataObject.getString("weight"));
                                    edit.putString("max_weight", dataObject.getString("weight"));
                                    max_weight.setText(String.valueOf(mini_weight));
                                }
                                StringBuilder exerciseName = new StringBuilder();
                                StringBuilder reps = new StringBuilder();
                                JSONArray planExercises = dataObject.getJSONArray("planExercises");
                                for(int j = 0 ; j < planExercises.length() ; j++){
                                    JSONObject planExerciseObject = planExercises.getJSONObject(j);
                                    exerciseName.append(planExerciseObject.getString("exerciseName")).append(" \n");
                                    reps.append(planExerciseObject.getString("reps")).append("reps").append(" \n");
                                }
                                System.out.println("localDate : " + localDate);
                                System.out.println("weight : " + weight);
                                System.out.println("exerciseName" + exerciseName.substring(0, exerciseName.length()-2));
                                System.out.println("reps : " + reps.substring(0, reps.length()-2));
                                adapter.addItem(new PlanItem(localDate, weight, exerciseName.substring(0, exerciseName.length()-2), reps.substring(0, reps.length()-2)));
                                System.out.println("adapter add : " + i + " 번" );
                            }
                            if(getActivity() != null){
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        System.out.println("입력 하세요!");
                    }

//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                Toast.makeText(getActivity(), "응답" + responseData, Toast.LENGTH_SHORT).show();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
                }
            }
        });

        recyclerView.setAdapter(adapter);

        /**
         * 현재, 최고, 최저, BMI, 신장 상황 가져와야 한다.
         * 추가버튼으로 추가하면 갱신 할 것인가?
         */

        /**
         * 액티비티 콜백 함수
         */
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            Intent intent = result.getData();
                            AddPlanDto addPlanDto = (AddPlanDto) intent.getSerializableExtra("addPlanDto");
                            cur_weight.setText(addPlanDto.getSaveWeight());
                            edit.putString("weight", addPlanDto.getSaveWeight());
                            if(Integer.parseInt((String) max_weight.getText()) < Integer.parseInt(addPlanDto.getSaveWeight())){
                                edit.putString("max_weight", addPlanDto.getSaveWeight());
                                max_weight.setText(addPlanDto.getSaveWeight());
                            }
                            if(Integer.parseInt((String) min_weight.getText()) > Integer.parseInt(addPlanDto.getSaveWeight())){
                                edit.putString("min_weight", addPlanDto.getSaveWeight());
                                min_weight.setText(addPlanDto.getSaveWeight());
                            }
                            double b = Math.pow(BigDecimal.valueOf(Integer.parseInt(prefs.getString("height", "1"))).divide(new BigDecimal(100),2, RoundingMode.HALF_EVEN).doubleValue(),2);
                            double bmiResult = BigDecimal.valueOf(Integer.parseInt(addPlanDto.getSaveWeight())).divide(new BigDecimal(b),2,RoundingMode.HALF_EVEN).doubleValue();
                            edit.putString("bmi", String.valueOf(bmiResult));
                            bmi.setText(String.valueOf(bmiResult));
                            edit.commit();
                            adapter.addItem(new PlanItem(LocalDate.now().toString(), addPlanDto.getSaveWeight()+"kg", addPlanDto.getSaveExercise(), addPlanDto.getSaveReps() + "reps"));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
        add_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddPlan.class);
                resultLauncher.launch(intent);
            }
        });

        return v;
    }

    /**
     * 초기 min_weight, max_weight 없다.
     */
    private void fillStatus() {
        System.out.println("min_weight1 : " + prefs.getString("min_weight", "none"));
        cur_weight.setText(prefs.getString("weight", "None"));
        if(prefs.getString("max_weight", "none").equals("none")){
            edit.putString("max_weight", prefs.getString("weight", "1"));
            max_weight.setText(prefs.getString("weight", "1"));
        } else{
            max_weight.setText(prefs.getString("max_weight", "1"));
        }
        if(prefs.getString("min_weight", "none").equals("none")){
            edit.putString("min_weight", prefs.getString("weight", "1"));
            min_weight.setText(prefs.getString("weight", "1"));
        } else{
            min_weight.setText(prefs.getString("min_weight", "1"));
        }
        double b = Math.pow(BigDecimal.valueOf(Integer.parseInt(prefs.getString("height", "1"))).divide(new BigDecimal(100),2, RoundingMode.HALF_EVEN).doubleValue(),2);
        double result = BigDecimal.valueOf(Integer.parseInt(prefs.getString("weight", "1"))).divide(new BigDecimal(b),2,RoundingMode.HALF_EVEN).doubleValue();
        bmi.setText(String.valueOf(result));
        height.setText(prefs.getString("height", "None"));
        edit.commit();
        System.out.println("min_weight2 : " + prefs.getString("min_weight", "none"));

    }

}