package com.example.shop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.example.shop.model.GoodDetail;
import com.example.shop.model.GoodsViewGroupItem;
import com.example.shop.util.BitmapCache;
import com.example.shop.util.GoodsViewGroup;
import com.example.shop.util.SharedPreferenceHelper;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shop.common.UrlAddress.addCartUrl;
import static com.example.shop.common.UrlAddress.detailsUrl;
import static com.example.shop.common.UrlAddress.picUrl;

public class GoodsDetailActivity extends AppCompatActivity{
    GoodsViewGroup sizeGroup,colorGroup;
    TextView gDetail,gPrice,gDiscount,gSale;
    String goodsId;
    RequestQueue queue = null;
    String wannaColorId,wannaSizeId,wannaColor,wannaSize,wannaNum,wannaGoodsName,pic,wannaPrice;
    ArrayList<String> wannaColorList,wannaSizeList,wannaGoodsNameList,wannaPicList,wannaPriceList,wannaNumList,wannaGoodsIdList;
    DiscreteScrollView scrollView;
    List img,colors,sizes,colorsId,sizesId;
    Button buy,close,confirm,addCart;
    PopupWindow popupWindow;
    ImageLoader imageLoader;
    com.travijuu.numberpicker.library.NumberPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        queue = Volley.newRequestQueue(this);
        imageLoader = new ImageLoader(queue,new BitmapCache());
        Intent intent = getIntent();
        initView();
        pic = intent.getStringExtra("pic");
        goodsId = intent.getStringExtra("goodsId");
        getDetails();
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buy();
            }
        });

    }

    public void Buy(){
        View view = LayoutInflater.from(GoodsDetailActivity.this).inflate(R.layout.popup_window_item,null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        colorGroup = (GoodsViewGroup)view.findViewById(R.id.color_group);
        sizeGroup = (GoodsViewGroup)view.findViewById(R.id.size_group);
        sizeGroup.addItemViews(getSizeItems());
        sizeGroup.setGroupClickListener(new GoodsViewGroup.OnGroupItemClickListener() {
            @Override
            public void onGroupItemClick(int itemPos, String key, String value) {
                Toast.makeText(GoodsDetailActivity.this,"选择了"+value,Toast.LENGTH_LONG).show();
                wannaSizeId = sizesId.get(itemPos).toString();
                wannaSize = value;
            }
        });
        colorGroup.addItemViews(getColorItems());
        colorGroup.setGroupClickListener(new GoodsViewGroup.OnGroupItemClickListener() {
            @Override
            public void onGroupItemClick(int itemPos, String key, String value) {
                Toast.makeText(GoodsDetailActivity.this,"选择了"+value,Toast.LENGTH_LONG).show();
                wannaColorId = colorsId.get(itemPos).toString();
                wannaColor = value;
            }
        });
        picker = (com.travijuu.numberpicker.library.NumberPicker) view.findViewById(R.id.numPicker);
        picker.setMin(0);
        picker.setMax(1000);
        addCart = (Button)view.findViewById(R.id.add_cart);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest request = new StringRequest(Request.Method.POST, addCartUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        popupWindow.dismiss();
                        Log.e("添加成功","");
                        Toast.makeText(GoodsDetailActivity.this,"添加购物车成功",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
                    //提交参数
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        wannaNum = String.valueOf(picker.getValue());
                        map.put("goodsId",goodsId);
                        map.put("cartName",wannaGoodsName);
                        map.put("cartSize",wannaSize);
                        map.put("cartColor",wannaColor);
                        map.put("cartPrice",wannaPrice);
                        map.put("cartNum",wannaNum);
                        map.put("cartPic",pic.replace("http://192.168.253.1:8080/Frame2",""));
                        Log.e("map",map.toString());
                        return map;
                    }
                    //写入Cookie
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> localHashMap = new HashMap<>();
                        String cookie = SharedPreferenceHelper.getCookie(GoodsDetailActivity.this);
                        localHashMap.put("Cookie", cookie);
                        return localHashMap;
                    }
                };
                queue.add(request);
            }
        });
        confirm = (Button)view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wannaNum = String.valueOf(picker.getValue());
                wannaColorList.add(wannaColor);
                wannaSizeList.add(wannaSize);
                wannaGoodsIdList.add(goodsId);
                wannaGoodsNameList.add(wannaGoodsName);
                wannaPicList.add(pic);
                wannaPriceList.add(wannaPrice);
                wannaNumList.add(wannaNum);
                Intent intent = new Intent(GoodsDetailActivity.this,CartActivity.class);
                intent.putStringArrayListExtra("goodsId",wannaGoodsIdList)
                        .putStringArrayListExtra("wannaColor",wannaColorList)
                        .putStringArrayListExtra("wannaSize",wannaSizeList)
                        .putStringArrayListExtra("wannaNum",wannaNumList)
                        .putStringArrayListExtra("wannaGoodsName",wannaGoodsNameList)
                        .putStringArrayListExtra("price",wannaPriceList)
                        .putStringArrayListExtra("pic",wannaPicList);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        close = (Button)view.findViewById(R.id.close);
        popupWindow.setContentView(view);
        View rootView = LayoutInflater.from(GoodsDetailActivity.this).inflate(R.layout.activity_goods_detail,null);
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM,0,0);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });


    }

    private List<GoodsViewGroupItem> getColorItems() {
        List<GoodsViewGroupItem> items = new ArrayList<>();
        for (int i = 0;i<colors.size();i++){
            items.add(new GoodsViewGroupItem(i+"",colors.get(i).toString()));

        }
        return items;
    }

    private List<GoodsViewGroupItem> getSizeItems() {
        List<GoodsViewGroupItem> items = new ArrayList<>();
        for (int i = 0;i<sizes.size();i++){
            items.add(new GoodsViewGroupItem(i+"",sizes.get(i).toString()));

        }
        return items;
    }

    public void initView(){
        scrollView = (DiscreteScrollView)findViewById(R.id.scroll);
        wannaColorList = new ArrayList<>();
        wannaGoodsIdList = new ArrayList<>();
        wannaGoodsNameList = new ArrayList<>();
        wannaNumList = new ArrayList<>();
        wannaPicList = new ArrayList<>();
        wannaPriceList = new ArrayList<>();
        wannaSizeList = new ArrayList<>();
        img = new ArrayList();
        colors = new ArrayList();
        sizes = new ArrayList();
        colorsId = new ArrayList();
        sizesId = new ArrayList();
        gDetail = (TextView)findViewById(R.id.g_detail);
        gPrice = (TextView)findViewById(R.id.gd_price);
        gDiscount = (TextView)findViewById(R.id.gd_discount);
        gSale = (TextView)findViewById(R.id.gd_sale);
        buy = (Button)findViewById(R.id.buy);

    }
    public void getDetails(){
        StringRequest request = new StringRequest(Request.Method.POST, detailsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONObject jsonObject = jo.getJSONObject("goodsDetail");
                    System.out.println("aaa"+jsonObject);
                    JSONArray picArray = jsonObject.getJSONArray("pics");
                    System.out.println("bbb"+picArray);
                    String id = jsonObject.getString("goodsId");
                    String cate_id = jsonObject.getString("cateId");
                    String name = jsonObject.getString("goodsName");
                    wannaGoodsName = name;
                    System.out.println("ccc"+name);
                    String disc = jsonObject.getString("goodsDisc");
                    String price = jsonObject.getString("goodsPrice");
                    wannaPrice = price;
                    String discount = jsonObject.getString("goodsDiscount");
                    String stock = jsonObject.getString("goodsStock");
                    String sales = jsonObject.getString("goodsSales");
                    String Material = jsonObject.getString("goodsMaterial");
                    String origin = jsonObject.getString("goodsOrigin");
                    String postalfee = jsonObject.getString("goodsPostalfee");
                    String date = jsonObject.getString("goodsDate");
                    JSONObject colorObject = jo.getJSONObject("goodsColors");
                    JSONArray colorArray = colorObject.getJSONArray("colors");
                    for (int i = 0;i<colorArray.length();i++){
                        JSONObject object = colorArray.getJSONObject(i);
                        String color = object.getString("colorName");
                        String colorId = object.getString("colorId");
                        colorsId.add(colorId);
                        colors.add(color);
                    }
                    JSONObject sizeObject = jo.getJSONObject("goodsSizes");
                    JSONArray sizeArray = sizeObject.getJSONArray("sizes");
                    for (int i = 0;i<sizeArray.length();i++){
                        JSONObject object = sizeArray.getJSONObject(i);
                        String size = object.getString("sizeName");
                        String sizeId = object.getString("sizeId");
                        sizesId.add(sizeId);
                        sizes.add(size);
                    }
                    gDetail.setText(disc);
                    gPrice.setText(price);
                    gDiscount.setText(discount);
                    gSale.setText("共售出："+sales+"件");
                    for (int i = 0;i<picArray.length();i++){
                        JSONObject object = picArray.getJSONObject(i);
                        System.out.println("ddd"+object);
                        String imgUrl = picUrl.concat(object.getString("picUrl"));
                        img.add(imgUrl);
                        System.out.println(img);
                    }
                    PicAdapter adapter = new PicAdapter(img);
                    scrollView.setAdapter(adapter);


                }catch (JSONException e){
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
                String cookie = SharedPreferenceHelper.getCookie(GoodsDetailActivity.this);
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }
    public class PicAdapter extends RecyclerView.Adapter<ViewHolder>{
        public List data = null;
        public PicAdapter(List data){
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fliper_item,parent,false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.imageView.setImageUrl(data.get(position).toString(),imageLoader);

        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public NetworkImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView)itemView.findViewById(R.id.img);
        }
    }
//
}
