package com.example.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shop.model.Reg;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.example.shop.common.UrlAddress.regUrl;

public class RegisterActivity extends AppCompatActivity {
    private RequestQueue queue;
    private EditText et_userName,et_passWord,et_age,et_email;
    private RadioButton rb_male,rb_famale;
    private RadioGroup sexGroup;
    private String sex;
    private Button bt_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        queue = Volley.newRequestQueue(this);
        et_userName = (EditText)findViewById(R.id.username);
        et_passWord = (EditText)findViewById(R.id.password);
        et_age = (EditText)findViewById(R.id.age);
        et_email = (EditText)findViewById(R.id.email);
        rb_male = (RadioButton)findViewById(R.id.male);
        rb_famale = (RadioButton)findViewById(R.id.female);
        sexGroup = (RadioGroup)findViewById(R.id.sex);
        bt_submit = (Button)findViewById(R.id.submit);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit(view);
            }
        });
    }
    public void submit(View view) {
         final String userName = et_userName.getText().toString();
         final String userPass = et_passWord.getText().toString();
         final String age = et_age.getText().toString();
         final String email = et_email.getText().toString();
         sex = "0";
        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rb_male.getId() == i){
                    sex = "0";
                }else {
                    sex = "1";
                }
            }
        });
        System.out.println(sex);
        StringRequest request = new StringRequest(Request.Method.POST, regUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                Gson gson = new Gson();
                Reg reg = gson.fromJson(response,Reg.class);
                if (reg.getReg().equals("yes")){
                    Toast.makeText(RegisterActivity.this,"注册成功!",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(RegisterActivity.this,"注册失败!",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this,"请检查网络!",Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userName", userName);
                map.put("userPass", userPass);
                map.put("userAge",age);
                map.put("userSex",sex);
                map.put("userEmail",email);
                return map;
            }
        };
        queue.add(request);
    }
}
