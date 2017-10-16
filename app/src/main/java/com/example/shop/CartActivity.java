package com.example.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shop.util.BitmapCache;
import com.example.shop.util.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.hoang8f.widget.FButton;

import static com.example.shop.common.UrlAddress.addressUrl;

public class CartActivity extends AppCompatActivity {
    List addrIdList,userIdList,addrProvinceList,addrCityList,addrAreaList,addrContentList,addrReceiverList,addrTleList,addrIsDefaultList;
    RequestQueue queue = null;
    RadioGroup addressRg;
    RadioButton radioButton;
    TextView goodsInfo;
    NetworkImageView imageView;
    ImageLoader imageLoader;
    FButton pay;
    String wannaColorId,wannaSizeId,goodsId,wannaColor,wannaSize,wannaNum,wannaGoodsName,pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        queue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(queue,new BitmapCache());
        Intent intent = getIntent();
        wannaColor = intent.getStringExtra("wannaColor");
        goodsId = intent.getStringExtra("goodsId");
        wannaSize = intent.getStringExtra("wannaSize");
        wannaNum = intent.getStringExtra("wannaNum");
        wannaGoodsName = intent.getStringExtra("wannaGoodsName");
        pic = intent.getStringExtra("pic");
        initView();
        getAddress();
        goodsInfo.setText("您选择的是:"+wannaGoodsName+" "+wannaColor+"  "+wannaSize+"   *"+wannaNum);
        imageView.setImageUrl(pic,imageLoader);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pay();
            }
        });
    }
    public void initView(){
        pay = (FButton)findViewById(R.id.pay);
        imageView = (NetworkImageView)findViewById(R.id.orderImg);
        addressRg = (RadioGroup)findViewById(R.id.addressGroup);
        goodsInfo = (TextView)findViewById(R.id.orderInfo);
        addrIdList = new ArrayList();
        userIdList = new ArrayList();
        addrProvinceList = new ArrayList();
        addrCityList = new ArrayList();
        addrAreaList = new ArrayList();
        addrContentList = new ArrayList();
        addrReceiverList = new ArrayList();
        addrTleList = new ArrayList();
        addrIsDefaultList = new ArrayList();


    }
    public void Pay(){

        finish();
    }
    public void getAddressRg(){
        int a = 0;
        Log.e("长度",addrIdList.toString());
        for (int i =0;i<addrIdList.size();i++){
            radioButton = new RadioButton(this);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,RadioGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15,0,0,0);
            radioButton.setPadding(80,0,0,0);
            radioButton.setText(addrProvinceList.get(i).toString()+addrCityList.get(i).toString()+addrAreaList.get(i).toString()+addrContentList.get(i).toString()+
                    "("+addrReceiverList.get(i).toString()+"收)"+"   电话号码:"+addrTleList.get(i).toString());
            Log.e("测试",addrProvinceList.get(i).toString());

            if (addrIsDefaultList.get(i).equals("1")){
                a = i+1;
            }
            addressRg.addView(radioButton);

        }
        addressRg.check(a);

    }

    public void getAddress(){
        StringRequest request = new StringRequest(Request.Method.POST, addressUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray addressArray = jsonObject.getJSONArray("addres");
                    for (int i = 0;i<addressArray.length();i++){
                        JSONObject object = addressArray.getJSONObject(i);
                        String addrId = object.getString("addrId");
                        addrIdList.add(addrId);
                        String userId = object.getString("userId");
                        userIdList.add(userId);
                        String addrProvince = object.getString("addrProvince");
                        addrProvinceList.add(addrProvince);
                        String addrCity = object.getString("addrCity");
                        addrCityList.add(addrCity);
                        String addrArea = object.getString("addrArea");
                        addrAreaList.add(addrArea);
                        String addrContent = object.getString("addrContent");
                        addrContentList.add(addrContent);
                        String addrReceiver = object.getString("addrReceiver");
                        addrReceiverList.add(addrReceiver);
                        String addrTel = object.getString("addrTel");
                        addrTleList.add(addrTel);
                        String addrIsDefault = object.getString("addrIsdefault");
                        addrIsDefaultList.add(addrIsDefault);
                        Log.e("tag",addrCity);
                    }
                    getAddressRg();
                    Log.e("id",addrIdList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CartActivity.this,"请检查网络",Toast.LENGTH_LONG).show();
            }
        }){
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHashMap = new HashMap<>();
                String cookie = SharedPreferenceHelper.getCookie(CartActivity.this);
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }

}
