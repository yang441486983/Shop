package com.example.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shop.util.SharedPreferenceHelper;

import java.util.HashMap;
import java.util.Map;

import static com.example.shop.common.UrlAddress.searchUrl;

public class SearchActivity extends AppCompatActivity {
    RequestQueue queue = null;
    String keyword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");
        Log.e("keyword",keyword);
    }
    public void getSearchGoods(){
        StringRequest request = new StringRequest(Request.Method.POST, searchUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            //提交参数
            protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> map = new HashMap<>();
                    map.put("keyword",keyword);
                    return map;


            }
            //写入Cookie
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHashMap = new HashMap<>();
                String cookie = SharedPreferenceHelper.getCookie(SearchActivity.this);
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }
}
