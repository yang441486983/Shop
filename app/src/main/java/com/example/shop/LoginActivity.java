package com.example.shop;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shop.common.UrlAddress;
import com.example.shop.model.Login;
import com.example.shop.util.PrefStore;
import com.example.shop.util.SharedPreferenceHelper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.shop.common.UrlAddress.loginUrl;

public class LoginActivity extends AppCompatActivity {
    private RequestQueue queue;
    private EditText et_username;
    private EditText et_password;
    private Button login,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        queue = Volley.newRequestQueue(this);
        et_username = (EditText) findViewById(R.id.et_userName);
        et_password = (EditText)findViewById(R.id.et_userPass);
        login = (Button)findViewById(R.id.login);
        register = (Button)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        PrefStore prefStore = new PrefStore(getApplicationContext());
        String name = prefStore.getPref("userName","");
        String pass = prefStore.getPref("userPass","");
        String cookie = SharedPreferenceHelper.getCookie(this);
        if (cookie!=null&&cookie!=""){
            et_username.setText(name);
            et_password.setText(pass);
            Login();
        }
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Login();
                }
            });



    }
    public void Login(){
        final String userName = et_username.getText().toString();
        final String userPass = et_password.getText().toString();
        final PrefStore prefStore = new PrefStore(getApplicationContext());
        prefStore.savePref("userName",userName);
        prefStore.savePref("userPass",userPass);
        StringRequest request = new StringRequest(Request.Method.POST, loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Gson gson = new Gson();
                Login login = gson.fromJson(response,Login.class);
                if(login.getLogin().equals("yes")){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this,"登陆成功!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginActivity.this,"登陆失败!",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "请检测网络！", Toast.LENGTH_SHORT).show();
            }
        })
        {
            //提交参数
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userName", userName);
                map.put("userPass", userPass);
                return map;
            }
            // 重写方法获取Cookie
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                if (rawCookies != null) {
                    String cookie = rawCookies.split(";")[0];
                    SharedPreferenceHelper.saveCookie(LoginActivity.this, cookie);
                    System.out.println("cookie"+cookie);
                }
                return super.parseNetworkResponse(response);
            }
            //写入Cookie
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHashMap = new HashMap<>();
                String cookie = SharedPreferenceHelper.getCookie(LoginActivity.this);
                localHashMap.put("Cookie", cookie);
                System.out.println("这是localHashMap"+cookie);
                return localHashMap;
            }



        };
        queue.add(request);
    }
//    //写入Cookie
//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        Map<String, String> localHashMap = new HashMap<>();
//        String cookie = SharedPreferenceHelper.getCookie(Login.this);
//        localHashMap.put("Cookie", cookie);
//        return localHashMap;
//    }{login=yes}


}
