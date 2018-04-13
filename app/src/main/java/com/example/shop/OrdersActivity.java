package com.example.shop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shop.common.UrlAddress.getMyOrdersUrl;
import static com.example.shop.common.UrlAddress.picUrl;

public class OrdersActivity extends AppCompatActivity {
    ListView myOrderList;
    ImageLoader imageLoader;
    List orderIdList,orderCodeList,userIdList,orderStatusList,orderPostalfeeList,orderDateList,odetailIdList
            ,goodsIdList,odetailNameList,odetailSizeList,odetailColorList,odetailPriceList,odetailNumList,odetailPicList;
    RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        queue = Volley.newRequestQueue(OrdersActivity.this);
        initView();
        getAllOrders();
    }
    public void initView(){
        imageLoader = new ImageLoader(queue,new BitmapCache());
        myOrderList = (ListView)findViewById(R.id.orderList);
        orderIdList = new ArrayList();
        orderCodeList = new ArrayList();
        userIdList = new ArrayList();
        orderStatusList = new ArrayList();
        orderPostalfeeList = new ArrayList();
        orderDateList = new ArrayList();
        odetailIdList = new ArrayList();
        goodsIdList = new ArrayList();
        odetailNameList = new ArrayList();
        odetailSizeList = new ArrayList();
        odetailColorList = new ArrayList();
        odetailPriceList = new ArrayList();
        odetailNumList = new ArrayList();
        odetailPicList = new ArrayList();
    }
    public void getAllOrders(){
        StringRequest request = new StringRequest(Request.Method.POST, getMyOrdersUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("response",response);
                    JSONObject jo = new JSONObject(response);
                    JSONObject jsonObject = jo.getJSONObject("orders");
                    JSONArray listArray = jsonObject.getJSONArray("list");
                    Log.e("listArray",listArray.toString());
                    for (int i = 0;i<listArray.length();i++){
                        int a = listArray.length();
                        String string = String.valueOf(a);
                        Log.e("listArray.length",string);
                        JSONObject object = listArray.getJSONObject(i);
                        String orderId = object.getString("orderId");
                        orderIdList.add(orderId);
                        Log.e("orderIdList",orderIdList.toString());
                        String orderCode = object.getString("orderCode");
                        orderCodeList.add(orderCode);
                        String userId = object.getString("userId");
                        userIdList.add(userId);
                        String orderStatus = object.getString("orderStatus");
                        orderStatusList.add(orderStatus);
                        String orderPostalfee = object.getString("orderPostalfee");
                        orderPostalfeeList.add(orderPostalfee);
                        String orderDate = object.getString("orderDate");
                        orderDateList.add(orderDate);
                        JSONArray detailsArray = object.getJSONArray("odetails");
                        JSONObject detailObject = (JSONObject)detailsArray.get(0);
                        Log.e("detailObject",detailObject.toString());

                            String odetailId = detailObject.getString("odetailId");
                            odetailIdList.add(odetailId);
                            String goodsId = detailObject.getString("goodsId");
                            goodsIdList.add(goodsId);
                            String odetailName = detailObject.getString("odetailName");
                            odetailNameList.add(odetailName);
                            String odetailSize = detailObject.getString("odetailSize");
                            odetailSizeList.add(odetailSize);
                            String odetailColor = detailObject.getString("odetailColor");
                            odetailColorList.add(odetailColor);
                            String odetailPrice = detailObject.getString("odetailPrice");
                            odetailPriceList.add(odetailPrice);
                            String odetailNum = detailObject.getString("odetailNum");
                            odetailNumList.add(odetailNum);
                            String odetailPic = picUrl.concat(detailObject.getString("odetailPic"));
                            odetailPicList.add(odetailPic);
                    }
                    Log.e("odetailPicList",odetailPicList.toString());
                    MyAdapter adapter = new MyAdapter();
                    myOrderList.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            //写入Cookie
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHashMap = new HashMap<>();
                String cookie = SharedPreferenceHelper.getCookie(OrdersActivity.this);
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return orderIdList.size();
        }

        @Override
        public Object getItem(int i) {
            return orderIdList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = new ViewHolder();
            if (view == null){
                view = LayoutInflater.from(OrdersActivity.this).inflate(R.layout.order_item,null);
                holder.orderPic = (NetworkImageView)view.findViewById(R.id.orderPic);
                holder.orderCode = (TextView)view.findViewById(R.id.orderCode);
                holder.orderDate = (TextView)view.findViewById(R.id.orderDate);
                holder.orderListInfo = (TextView)view.findViewById(R.id.orderListInfo);
                holder.orderPrice = (TextView)view.findViewById(R.id.orderPrice);
                holder.orderTotalPrice = (TextView)view.findViewById(R.id.orderTotalPrice);
                view.setTag(holder);
            }else {
                holder = (ViewHolder)view.getTag();
            }
            holder.orderPic.setImageUrl(odetailPicList.get(i).toString(),imageLoader);
            holder.orderCode.setText("订单号:"+orderCodeList.get(i).toString());
            holder.orderDate.setText("订单日期:"+orderDateList.get(i).toString());
            holder.orderListInfo.setText(odetailNameList.get(i).toString()+"    "+odetailSizeList.get(i).toString()+"    "+odetailColorList.get(i).toString());
            holder.orderPrice.setText("商品价格:"+odetailPriceList.get(i).toString()+"元");
            //FATAL EXCEPTION:main
            //java.lang.NumberFormatException:For input string:"12.0"
            double totalPrice = Double.parseDouble(orderPostalfeeList.get(i).toString())+Double.parseDouble(odetailPriceList.get(i).toString());
            holder.orderTotalPrice.setText("总价:"+String.valueOf(totalPrice)+"元");
            return view;
        }
    }
    class ViewHolder{
        NetworkImageView orderPic;
        TextView orderCode,orderDate,orderListInfo,orderPrice,orderTotalPrice;
    }
}
