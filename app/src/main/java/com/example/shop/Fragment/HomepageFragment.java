package com.example.shop.Fragment;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.shop.GoodsDetailActivity;
import com.example.shop.R;


import com.example.shop.model.GoodDetail;
import com.example.shop.util.BitmapCache;

import com.example.shop.util.SharedPreferenceHelper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shop.common.UrlAddress.newUrl;
import static com.example.shop.common.UrlAddress.picUrl;

public class HomepageFragment extends Fragment{
    List<String> nameList,picList,saleList,priceList,discountList,idList,cateIdList,discList,stockList;
    ListView lv_newGoods;
    RequestQueue queue = null;
    NetworkImageView f_goodsImage;
    TextView f_goodsName,f_saleCount,f_goodsPrice,f_goodsDiscount;
    ImageLoader imageLoader;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_homepage,container,false);

        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
        idList = new ArrayList<>();
        cateIdList = new ArrayList<>();
        discList = new ArrayList<>();
        stockList = new ArrayList<>();
        nameList = new ArrayList<>();
        picList = new ArrayList<>();
        saleList = new ArrayList<>();
        priceList = new ArrayList<>();
        discountList = new ArrayList<>();
        initView();

        imageLoader = new ImageLoader(queue,new BitmapCache());
        getNewGoods();
        itemListener();
    }
    public void itemListener(){
        lv_newGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra("goodsId",idList.get(i));
                intent.putExtra("pic",picList.get(i));
                startActivity(intent);
            }
        });
    }
    public void getNewGoods() {
        StringRequest request = new StringRequest(Request.Method.POST, newUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jo = new JSONObject(response);
                    JSONArray array = jo.getJSONArray("newGoods");
                    Log.e("arry",array.toString());
                    //System.out.println(array);
                    for (int i = 0;i<array.length();i++){
                        JSONObject jsonObject = array.getJSONObject(i);
                        String id = jsonObject.getString("goodsId");
                        String cate_id = jsonObject.getString("cateId");
                        String name = jsonObject.getString("goodsName");
                        String disc = jsonObject.getString("goodsDisc");
                        String price = jsonObject.getString("goodsPrice");
                        String discount = jsonObject.getString("goodsDiscount");
                        String stock = jsonObject.getString("goodsStock");
                        String pic = picUrl.concat(jsonObject.getString("goodsPic"));
                        String sales = jsonObject.getString("goodsSales");
                        idList.add(id);
                        cateIdList.add(cate_id);
                        nameList.add(name);
                        discList.add(disc);
                        priceList.add(price);
                        discountList.add(discount);
                        stockList.add(stock);
                        picList.add(pic);
                        saleList.add(sales);

                    }
                    Log.e("pci",picList.toString());
//                    Log.e("map",map.toString());
                    MyAdapter adapter = new MyAdapter();
                    lv_newGoods.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            //写入Cookie
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHashMap = new HashMap<>();
                String cookie = SharedPreferenceHelper.getCookie(getActivity());
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }
    private void initView(){
        lv_newGoods = (ListView)getActivity().findViewById(R.id.newGoodsList);
        f_goodsImage = (NetworkImageView)getActivity().findViewById(R.id.goodsImg);
        f_goodsName = (TextView)getActivity().findViewById(R.id.goodsName);
        f_saleCount = (TextView)getActivity().findViewById(R.id.saleCount);
        f_goodsPrice = (TextView)getActivity().findViewById(R.id.price);
        f_goodsDiscount = (TextView)getActivity().findViewById(R.id.discount);
    }
    class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return nameList.size();
        }

        @Override
        public Object getItem(int i) {
            return nameList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = new ViewHolder();
            if (view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.new_goods_item,null);
                holder.name = (TextView)view.findViewById(R.id.goodsName);
                holder.sale = (TextView)view.findViewById(R.id.saleCount);
                holder.price = (TextView)view.findViewById(R.id.price);
                holder.discount = (TextView)view.findViewById(R.id.discount);
                holder.image = (NetworkImageView) view.findViewById(R.id.goodsImg);
                view.setTag(holder);
            }else {
                holder = (ViewHolder)view.getTag();
            }
            holder.name.setText(nameList.get(i).toString());
            holder.sale.setText("共销售:"+saleList.get(i).toString()+"件");
            holder.price.setText(priceList.get(i).toString()+"元");
            holder.discount.setText(discountList.get(i).toString()+"元");
            holder.image.setImageUrl(picList.get(i).toString(),imageLoader);
            return view;
        }
    }
    class ViewHolder{
        public TextView name;
        public TextView sale;
        public TextView price;
        public TextView discount;
        public NetworkImageView image;
    }
}

