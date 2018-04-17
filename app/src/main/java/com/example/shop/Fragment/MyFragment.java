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
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shop.AddressManageActivity;
import com.example.shop.OrdersActivity;
import com.example.shop.R;
import com.example.shop.util.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.codego.view.DotLayout;


import static com.example.shop.common.UrlAddress.getMyListOrdersUrl;

public class MyFragment extends Fragment {
    TextView addressManage;
    RequestQueue queue = null;
    DotLayout unPay,unShipped,unReceived,all;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_my,container,false);


        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
        unPay = (DotLayout)getActivity().findViewById(R.id.unPay);
        unShipped = (DotLayout)getActivity().findViewById(R.id.unShipped);
        unReceived = (DotLayout)getActivity().findViewById(R.id.unReceived);
        all = (DotLayout)getActivity().findViewById(R.id.all);
        addressManage = (TextView)getActivity().findViewById(R.id.address_manage);
        getUnPayOrders();
        getUnShippedOrders();
        getunReceivedOrders();
        getAllOrders();
        itemOnClick();
    }
    public void itemOnClick(){
        unPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),OrdersActivity.class);
                intent.putExtra("status","0");
                startActivity(intent);
            }
        });
        unShipped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),OrdersActivity.class);
                intent.putExtra("status","1");
                startActivity(intent);
            }
        });
        unReceived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),OrdersActivity.class);
                intent.putExtra("status","4");
                startActivity(intent);
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),OrdersActivity.class);
                intent.putExtra("status","");
                startActivity(intent);
            }
        });
        addressManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddressManageActivity.class);
                startActivity(intent);
            }
        });
    }
    public void getUnPayOrders(){
        Log.e("这个方法执行了","确定");
        StringRequest request = new StringRequest(Request.Method.POST, getMyListOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("第四个response",response);
                try {


                    JSONObject jo = new JSONObject(response);
                    JSONArray jsonArray = jo.getJSONArray("orders");
                    Log.e("待付款",String.valueOf(jsonArray.length()));
                    if (jsonArray.length()>0){
                        unPay.show(true,jsonArray.length());
                    }else {
                        unPay.show(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("网络错误","网络错误");
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
    public void getUnShippedOrders(){
        StringRequest request = new StringRequest(Request.Method.POST, getMyListOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("第四个response",response);
                try {


                    JSONObject jo = new JSONObject(response);
                    JSONArray jsonArray = jo.getJSONArray("orders");
                    Log.e("待付款",String.valueOf(jsonArray.length()));
                    if (jsonArray.length()>0){
                        unShipped.show(true,jsonArray.length());
                    }else {
                        unShipped.show(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("网络错误","网络错误");
            }
        }){
            //提交参数
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("status",String.valueOf(1));
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
    public void getunReceivedOrders(){
        StringRequest request = new StringRequest(Request.Method.POST, getMyListOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("第四个response",response);
                try {


                    JSONObject jo = new JSONObject(response);
                    JSONArray jsonArray = jo.getJSONArray("orders");
                    Log.e("待付款",String.valueOf(jsonArray.length()));
                    if (jsonArray.length()>0){
                        unReceived.show(true,jsonArray.length());
                    }else {
                        unReceived.show(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("网络错误","网络错误");
            }
        }){
            //提交参数
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("status",String.valueOf(4));
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
    public void getAllOrders(){
        StringRequest request = new StringRequest(Request.Method.POST, getMyListOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("第四个response",response);
                try {


                    JSONObject jo = new JSONObject(response);
                    JSONArray jsonArray = jo.getJSONArray("orders");
                    Log.e("待付款",String.valueOf(jsonArray.length()));
                    if (jsonArray.length()>0){
                        all.show(true,jsonArray.length());
                    }else {
                        all.show(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("网络错误","网络错误");
            }
        }){

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
