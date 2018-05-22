package com.example.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.example.shop.util.alipay.PayDemoActivity;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static com.example.shop.common.UrlAddress.addOrderUrl;
import static com.example.shop.common.UrlAddress.addressUrl;
import static com.example.shop.common.UrlAddress.detailsUrl;
import static com.example.shop.common.UrlAddress.picUrl;

public class CartActivity extends AppCompatActivity {
    List addrIdList,userIdList,addrProvinceList,addrCityList,addrAreaList,addrContentList,addrReceiverList,addrTleList,addrIsDefaultList;
    ArrayList<String> wannaColorList,wannaGoodsIdList,wannaSizeList,wannaNumList,wannaGoodsNameList,wannaPicList,wannaPriceList,wannaCartIdList;
    RequestQueue queue = null;
    RadioGroup addressRg;
    RadioButton radioButton;
    ListView confirmList;
    String address;
    float totalPrice;
    int totalNum;
    float totalPostPrice;
    List<String> postalfeeList;
    ImageLoader imageLoader;
    Button pay;
    TextView numberPrice;
    ConfirmOrderAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar)findViewById(R.id.confirm_pay);
        toolbar.setTitle("确认订单");
        (this).setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(queue,new BitmapCache());
        initView();
        adapter.notifyDataSetChanged();
        Intent intent = getIntent();
        wannaColorList = intent.getStringArrayListExtra("wannaColor");
        wannaGoodsIdList = intent.getStringArrayListExtra("goodsId");
        wannaSizeList = intent.getStringArrayListExtra("wannaSize");
        wannaNumList = intent.getStringArrayListExtra("wannaNum");
        wannaGoodsNameList = intent.getStringArrayListExtra("wannaGoodsName");
        wannaPicList = intent.getStringArrayListExtra("pic");
        wannaPriceList = intent.getStringArrayListExtra("price");
        wannaCartIdList = intent.getStringArrayListExtra("cartId");
        Log.e("wannaPicList谔谔谔谔",wannaPicList.toString());
        getAddress();

        Show();
    }
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                //退出处理的代码
                finish();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void addOrder(final String address, final String postPrice, final String orderGoodsId, final String goodsName, final String goodsDiscount, final String size, final String color, final String num, final String pic){
        StringRequest request = new StringRequest(Request.Method.POST, addOrderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("订单号",response);
                String orderId = response;
                Log.e("totalPrice",String.valueOf(totalPrice));
                Intent intent = new Intent(CartActivity.this, PayDemoActivity.class);
                intent.putExtra("totalPrice",String.valueOf(totalPrice))
                        .putExtra("totalNum",String.valueOf(totalNum))
                        .putExtra("orderId",orderId);
                intent.putStringArrayListExtra("cartId",wannaCartIdList);
                Log.e("需要支付",String.valueOf(totalPrice));
                startActivity(intent);
                Log.e("show",postalfeeList.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
            }
        }){

            //提交参数
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("address",address);
                map.put("orderPostalfee",postPrice);
                map.put("goodsId", orderGoodsId);
                map.put("goodsName",goodsName);
                map.put("goodsDiscount",goodsDiscount);
                map.put("size",size);
                map.put("color",color);
                map.put("num",num);
                map.put("pic",pic);
                Log.e("map",map.toString());
                return map;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHashMap = new HashMap<>();
                String cookie = SharedPreferenceHelper.getCookie(CartActivity.this);
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }
    public void Show(){
        totalPostPrice = 0;
        totalPrice = 0;
        totalNum = 0;
        confirmList.setAdapter(adapter);

        for (int i = 0;i<wannaGoodsIdList.size();i++){
            totalPrice = totalPrice + Float.parseFloat(wannaPriceList.get(i))*Integer.parseInt(wannaNumList.get(i));
            totalNum = totalNum + Integer.parseInt(wannaNumList.get(i));
        }
        numberPrice.setText("共"+totalNum+"个,"+totalPrice+"元");
        for (int i = 0;i<wannaGoodsIdList.size();i++){
            getPostalfee(wannaGoodsIdList.get(i));
        }
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int n = 0;n<postalfeeList.size();n++){
                    totalPostPrice = totalPostPrice + Float.valueOf(postalfeeList.get(n));
                }

                Gson gson = new Gson();
                String commitColor = gson.toJson(wannaColorList);
                String commitGoodsName = gson.toJson(wannaGoodsNameList);
                String commitGoodsId = gson.toJson(wannaGoodsIdList);
                Log.e("commitGoodsId",commitGoodsId);
                String commitSize = gson.toJson(wannaSizeList);
                String commitNum = gson.toJson(wannaNumList);
                String commitPic = gson.toJson(wannaPicList);
                String commitPrice = gson.toJson(wannaPriceList);
                addOrder(address,String.valueOf(totalPostPrice),commitGoodsId,commitGoodsName,commitPrice,commitSize,commitColor,commitNum,commitPic);


            }
        });
    }
    public void getPostalfee(final String goodsId){
        postalfeeList.clear();
        StringRequest request = new StringRequest(Request.Method.POST, detailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject jsonObject = jo.getJSONObject("goodsDetail");
                    Log.e("goodesDetail",jsonObject.toString());
                    String goodsPostalfee = jsonObject.getString("goodsPostalfee");
                    postalfeeList.add(goodsPostalfee);
                    Log.e("feeList",postalfeeList.toString());
//                    if (postalfeeList.size() == wannaGoodsIdList.size()){
//                        Log.e("addres",address);
//                    }
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
                map.put("goodsId", goodsId);
                return map;
            }
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHashMap = new HashMap<>();
                String cookie = SharedPreferenceHelper.getCookie(CartActivity.this);
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }
    public void initView(){
        adapter = new ConfirmOrderAdapter();
        postalfeeList = new ArrayList<>();
        numberPrice = (TextView)findViewById(R.id.number_price);
        pay = (Button)findViewById(R.id.pay);
        confirmList = (ListView)findViewById(R.id.commit_list);
        addressRg = (RadioGroup)findViewById(R.id.addressGroup);
        wannaPriceList = new ArrayList<>();
        wannaPicList = new ArrayList<>();
        wannaCartIdList = new ArrayList<>();
        wannaGoodsNameList = new ArrayList<>();
        wannaNumList = new ArrayList<>();
        wannaSizeList = new ArrayList<>();
        wannaColorList = new ArrayList<>();
        wannaGoodsIdList = new ArrayList<>();
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


    public void getAddressRg(){
        int a = 0;
        Log.e("长度",addrIdList.toString());
        for (int i =0;i<addrIdList.size();i++){
            radioButton = new RadioButton(this);

            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,RadioGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15,0,0,0);
            radioButton.setId(i);
            radioButton.setPadding(80,0,0,0);
            radioButton.setText(addrProvinceList.get(i).toString()+addrCityList.get(i).toString()+addrAreaList.get(i).toString()+addrContentList.get(i).toString()+
                    "("+addrReceiverList.get(i).toString()+"收)"+"   电话号码:"+addrTleList.get(i).toString());
            Log.e("测试",addrProvinceList.get(i).toString());
            Log.e("默认地址",addrIsDefaultList.toString());
            if (addrIsDefaultList.get(i).equals("1")){
                a = i;

            }
            addressRg.addView(radioButton);
            //addressRg.check(a);
        }

        addressRg.check(a);
        address = addrProvinceList.get(a).toString()+addrCityList.get(a).toString()+addrAreaList.get(a).toString()+addrContentList.get(a).toString();
        Log.e("child",String.valueOf(addressRg.getChildCount()));
        addressRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int position) {
                Log.e("position",String.valueOf(position));
                address = addrProvinceList.get(position).toString()+addrCityList.get(position).toString()+addrAreaList.get(position).toString()+addrContentList.get(position).toString();
                Log.e("oncheckedchanged",address);
            }
        });

    }
    public void getAddress(){

        StringRequest request = new StringRequest(Request.Method.POST, addressUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                addrIdList.clear();
                userIdList.clear();
                addrProvinceList.clear();
                addrCityList.clear();
                addrAreaList.clear();
                addrContentList.clear();
                addrReceiverList.clear();
                addrTleList.clear();
                addrIsDefaultList.clear();
                Log.e("这是地址",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray addressArray = jsonObject.getJSONArray("address");
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
    class ConfirmOrderAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return wannaGoodsNameList.size();
        }

        @Override
        public Object getItem(int i) {
            return wannaGoodsNameList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Log.e("调用了这个adapter","");
            ConfirmHolder holder = new ConfirmHolder();
            if (view == null){
                view = LayoutInflater.from(CartActivity.this).inflate(R.layout.confirm_goods_item,null);
                holder.confirmPic = (NetworkImageView)view.findViewById(R.id.confirm_pic);
                holder.confirmColor = (TextView)view.findViewById(R.id.confirm_color);
                holder.confirmName = (TextView)view.findViewById(R.id.confirm_name);
                holder.confirmSize = (TextView)view.findViewById(R.id.confirm_size);
                holder.confirmNum = (TextView)view.findViewById(R.id.confirm_num);
                holder.confirmPrice = (TextView)view.findViewById(R.id.confirm_price);
                view.setTag(holder);
            }else {
                holder = (ConfirmHolder)view.getTag();
            }
            holder.confirmPic.setImageUrl(picUrl+wannaPicList.get(i),imageLoader);
            holder.confirmName.setText(wannaGoodsNameList.get(i));
            holder.confirmColor.setText(wannaColorList.get(i));
            holder.confirmSize.setText(wannaSizeList.get(i));
            holder.confirmNum.setText("数量："+wannaNumList.get(i));
            holder.confirmPrice.setText("单价："+wannaPriceList.get(i)+"元");
            return view;
        }
    }
    class ConfirmHolder{
        public NetworkImageView confirmPic;
        public TextView confirmName,confirmColor,confirmSize,confirmNum,confirmPrice;
    }
}
