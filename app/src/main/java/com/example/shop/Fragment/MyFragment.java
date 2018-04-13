package com.example.shop.Fragment;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shop.OrdersActivity;
import com.example.shop.R;
import com.example.shop.util.SharedPreferenceHelper;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.codego.view.DotLayout;

import static com.example.shop.common.UrlAddress.getMyListOrdersUrl;

public class MyFragment extends Fragment {
    RequestQueue queue = null;
    Button myOrders;
    DotLayout unPay;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_my,container,false);
        queue = Volley.newRequestQueue(getActivity());
        getUnPayOrders();
        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myOrders = (Button)getActivity().findViewById(R.id.myOrders);
        unPay = (DotLayout)getActivity().findViewById(R.id.unPay);


        myOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrdersActivity.class);
                startActivity(intent);
            }
        });

    }
    public void getUnPayOrders(){
        Log.e("这个方法执行了","确定");
        StringRequest request = new StringRequest(Request.Method.POST, getMyListOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("第四个response",response);
                    JSONObject jo = new JSONObject(response);
                    JSONArray jsonArray = jo.getJSONArray("orders");
                    Log.e("待付款",String.valueOf(jsonArray.length()));
                    unPay.show(true,jsonArray.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            //提交参数
                protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("status",String.valueOf(0));
                Log.e("这是参数",map.toString());
                return map;
            }
            //写入Cookie
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHashMap = new HashMap<>();
                String cookie = SharedPreferenceHelper.getCookie(getActivity());
                localHashMap.put("Cookie", cookie);
                Log.e("这是cookie",localHashMap.toString());
                return localHashMap;
            }
        };
        queue.add(request);
    }

}
