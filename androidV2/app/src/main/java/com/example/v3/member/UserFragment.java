//package com.example.v3;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link UserFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class UserFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public UserFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment UserFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static UserFragment newInstance(String param1, String param2) {
//        UserFragment fragment = new UserFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_user, container, false);
//    }
//}
package com.example.v3.member;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.v3.Environment;
import com.example.v3.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class UserFragment extends Fragment {

    private TextView testUserInfo;
    private TextView user_email_txt;
    private TextView user_name_txt;
    private TextView user_weight_txt;
    private TextView user_height_txt;


    private SharedPreferences prefs;
    private SharedPreferences.Editor edit;


    // ????????? ??? ????????? (?????????, ??????, ?????????, ??????)
    String email = null;
    String name = null;
    String weight = null;
    String height = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.mfragment_user, container, false);

        user_email_txt = (TextView) v.findViewById(R.id.user_email_txt);
        user_name_txt = (TextView) v.findViewById(R.id.user_name_txt);
        user_email_txt = (TextView) v.findViewById(R.id.user_email_txt);
        user_weight_txt = (TextView) v.findViewById(R.id.user_weight_txt);
        user_height_txt = (TextView) v.findViewById(R.id.user_height_txt);

        prefs = this.getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        edit = prefs.edit();
        System.out.println("0");
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Environment.ip+"/user/info")
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

                    Log.d("userInfo : ", responseData);

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
                        email = dataObject.getString("email");
                        name = dataObject.getString("name");
                        String bodyInfoForm = dataObject.getString("bodyInfo");
                        JSONObject bodyInfoObject = new JSONObject(bodyInfoForm);
                        weight = bodyInfoObject.getString("weight");
                        height = bodyInfoObject.getString("height");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                user_email_txt.setText(email);
                                user_name_txt.setText(name);
                                user_weight_txt.setText(weight+"kg");
                                user_height_txt.setText(height+"cm");
                            }
                        });
                    }
                    Log.d("data : ", data);
                }
            }
        });


        return v;
    }
}