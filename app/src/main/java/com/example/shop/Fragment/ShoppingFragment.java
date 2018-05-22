package com.example.shop.Fragment;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.shop.CartActivity;
import com.example.shop.MainActivity;
import com.example.shop.R;
import com.example.shop.util.BitmapCache;
import com.example.shop.util.SharedPreferenceHelper;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shop.R.id.select_all;
import static com.example.shop.common.UrlAddress.deleteMyCartUrl;
import static com.example.shop.common.UrlAddress.getMyCartUrl;
import static com.example.shop.common.UrlAddress.picUrl;


public class ShoppingFragment extends Fragment {
    RequestQueue queue = null;
    ListView cartList;
    List<String> cartIdList,goodsIdList,cartNameList,cartSizeList,cartColorList,cartPriceList,cartNumList,cartPicList;
    ArrayList<String> wannaColorList,wannaGoodsIdList,wannaSizeList,wannaNumList,wannaGoodsNameList,wannaPriceList,wannaPicList,wannaCartIdList;
    ImageLoader imageLoader;
    CartAdapter adapter;
    CheckBox selectAll;
    String total,totalNum;
    Button deleteSelected,paySelected;
    Map<Integer,Integer> pickerNumber;
    TextView totalPriceShow;
    Map<Integer,Boolean> selectedMap;
    int deleteCount=0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.cart_toolbar);
        toolbar.setTitle("购物车");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        queue = Volley.newRequestQueue(getActivity());
        init();
        getMyCart();
        CheckBoxListener();
        ButtonOnclick();

    }
    public void ButtonOnclick(){
        deleteSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    for (int i = 0;i<selectedMap.size();i++){
                        if (selectedMap.get(i) == true){
                            deleteMyCart(cartIdList.get(i));
                        }

                    }



            }
        });
        paySelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int m = 0;m<selectedMap.size();m++){
                    if (selectedMap.get(m) == true){
                        PayCart();
                    }
                }
                Intent intent = new Intent(getActivity(), CartActivity.class);
                intent.putStringArrayListExtra("wannaColor",wannaColorList)
                        .putStringArrayListExtra("goodsId",wannaGoodsIdList)
                        .putStringArrayListExtra("wannaSize",wannaSizeList)
                        .putStringArrayListExtra("wannaNum",wannaNumList)
                        .putStringArrayListExtra("wannaGoodsName",wannaGoodsNameList)
                        .putStringArrayListExtra("price",wannaPriceList)
                        .putStringArrayListExtra("pic",wannaPicList)
                        .putExtra("cartId",wannaCartIdList);
                startActivity(intent);
            }
        });
    }
    public void deleteMyCart(final String deleteId){
        StringRequest request = new StringRequest(Request.Method.POST, deleteMyCartUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int count = 0;
                deleteCount = deleteCount + 1;
                for (int i = 0;i<selectedMap.size();i++){
                    if (selectedMap.get(i) == true){
                        count = count + 1;
                    }
                }
                if (deleteCount == count){
                    MainActivity mainActivity = (MainActivity)getActivity();
                    mainActivity.reLoadCartFragment();
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
                map.put("cartId",String.valueOf(deleteId));
                return map;
            }
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
    public void PayCart(){
        wannaCartIdList.clear();
        wannaColorList.clear();
        wannaGoodsIdList.clear();
        wannaSizeList.clear();
        wannaNumList.clear();
        wannaGoodsNameList.clear();
        wannaPicList.clear();
        wannaPriceList.clear();
        for (int i = 0;i<selectedMap.size();i++){
            if (selectedMap.get(i)==true){
                wannaCartIdList.add(cartIdList.get(i));
                wannaPicList.add(cartPicList.get(i));
                wannaSizeList.add(cartSizeList.get(i));
                wannaPriceList.add(cartPriceList.get(i));
                wannaNumList.add(cartNumList.get(i));
                wannaGoodsNameList.add(cartNameList.get(i));
                wannaGoodsIdList.add(goodsIdList.get(i));
                wannaColorList.add(cartColorList.get(i));
            }
        }

    }
    public void getTotalPrice(){
        Log.e("total",total);
        if (total != null){
            totalPriceShow.setText(total);
        }
    }
    public void CheckBoxListener(){
        Map<Integer,Boolean> isCheck = adapter.getMap();
        selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (selectAll.isChecked()){
                    adapter.initCheck(true);
                    adapter.notifyDataSetChanged();
                }else {
                    adapter.initCheck(false);
                    adapter.notifyDataSetChanged();
                }
            }
        });

            ;

    }
    public void init(){
        wannaCartIdList = new ArrayList<>();
        wannaColorList = new ArrayList<>();
        wannaGoodsIdList = new ArrayList<>();
        wannaGoodsNameList = new ArrayList<>();
        wannaNumList = new ArrayList<>();
        wannaPriceList = new ArrayList<>();
        wannaSizeList = new ArrayList<>();
        wannaPicList = new ArrayList<>();
        adapter = new CartAdapter();
        cartIdList = new ArrayList<>();
        goodsIdList = new ArrayList<>();
        cartNameList = new ArrayList<>();
        cartSizeList = new ArrayList<>();
        cartColorList = new ArrayList<>();
        cartPriceList = new ArrayList<>();
        cartNumList = new ArrayList<>();
        cartPicList = new ArrayList<>();
        selectedMap = new HashMap<>();
        pickerNumber = new HashMap<>();
        deleteSelected = (Button)getActivity().findViewById(R.id.delete_selected);
        paySelected = (Button)getActivity().findViewById(R.id.pay_all);
        selectAll = (CheckBox)getActivity().findViewById(R.id.select_all);
        cartList = (ListView)getActivity().findViewById(R.id.cart_list);
        totalPriceShow = (TextView)getActivity().findViewById(R.id.total_price);
        imageLoader = new ImageLoader(queue,new BitmapCache());
    }
    public void getMyCart(){
        StringRequest request = new StringRequest(Request.Method.POST, getMyCartUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("购物车清单",response);
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray jsonArray = jo.getJSONArray("cart");
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String cartId = jsonObject.getString("cartId");
                        cartIdList.add(cartId);
                        String goodsId = jsonObject.getString("goodsId");
                        goodsIdList.add(goodsId);
                        String cartName = jsonObject.getString("cartName");
                        cartNameList.add(cartName);
                        String cartSize = jsonObject.getString("cartSize");
                        cartSizeList.add(cartSize);
                        String cartColor = jsonObject.getString("cartColor");
                        cartColorList.add(cartColor);
                        String cartPrice = jsonObject.getString("cartPrice");
                        cartPriceList.add(cartPrice);
                        String cartPic = jsonObject.getString("cartPic");
                        cartPicList.add(cartPic);
                        String cartNum = jsonObject.getString("cartNum");
                        cartNumList.add(cartNum);
                    }

                    cartList.setAdapter(adapter);
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
                String cookie = SharedPreferenceHelper.getCookie(getActivity());
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }
    class CartAdapter extends BaseAdapter{
        private Map<Integer, Boolean> isCheck = new HashMap<Integer, Boolean>();
        public void initCheck(boolean flag) {
            // map集合的数量和list的数量是一致的
            for (int i = 0; i < cartIdList.size(); i++) {
                // 设置默认的显示
                isCheck.put(i, flag);
            }
        }

        public Map<Integer,Boolean> getMap(){
            return isCheck;
        }
        @Override
        public int getCount() {
            return cartIdList.size();
        }

        @Override
        public Object getItem(int i) {
            return cartIdList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            CartHolder cartHolder = new CartHolder();
            if (view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.cart_item,null);
                cartHolder.cartCheck = (CheckBox)view.findViewById(R.id.add_select);
                cartHolder.cartImage = (NetworkImageView)view.findViewById(R.id.cart_pic);
                cartHolder.cartName = (TextView)view.findViewById(R.id.cart_name);
                cartHolder.cartSize = (TextView)view.findViewById(R.id.cart_size);
                cartHolder.cartColor = (TextView)view.findViewById(R.id.cart_color);
                cartHolder.cartNum = (NumberPicker)view.findViewById(R.id.cart_num);
                view.setTag(cartHolder);
            }else {
                cartHolder = (CartHolder)view.getTag();
            }
            cartHolder.cartName.setText(cartNameList.get(i));
            cartHolder.cartSize.setText(cartSizeList.get(i));
            cartHolder.cartColor.setText(cartColorList.get(i));
            cartHolder.cartImage.setImageUrl(picUrl+cartPicList.get(i),imageLoader);
            cartHolder.cartNum.setMax(100);
            cartHolder.cartNum.setMin(1);
            cartHolder.cartNum.setUnit(1);
            Log.e("cartnum",cartNumList.toString());
            cartHolder.cartNum.setValueChangedListener(new ValueChangedListener() {
                @Override
                public void valueChanged(int value, ActionEnum action) {
                    pickerNumber.put(i,value);
                    cartNumList.set(i,String.valueOf(value));
                    //cartNumList.add(i,String.valueOf(value));
                    adapter.notifyDataSetChanged();
                    Log.e("value",cartNumList.toString());
                    //cartNumList.add(i,);
                    Log.e("pickerNumber",pickerNumber.toString());
                }
            });
            cartHolder.cartCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    isCheck.put(i,b);
                    adapter.notifyDataSetChanged();
                }
            });
            if (pickerNumber.get(i) == null){
                pickerNumber.put(i,1);
            }
            if (isCheck.get(i) == null){
                isCheck.put(i,false);
            }
            cartHolder.cartCheck.setChecked(isCheck.get(i));
            selectedMap.put(i,cartHolder.cartCheck.isChecked());
            Log.e("selectedMap",selectedMap.toString());
            float totalPrice = 0;
            int number = 0;
            for (int j = 0; j < selectedMap.size();j++){
                if (selectedMap.get(j) == true){
                    totalPrice = totalPrice + (Float.parseFloat(cartPriceList.get(j))*pickerNumber.get(j));
                    number = number + pickerNumber.get(j);
                }
            }
            Log.e("totalPrice",String.valueOf(totalPrice));
            total = String.valueOf(totalPrice);
            totalNum = String.valueOf(number);
            Log.e("total",total);
            getTotalPrice();
            return view;
        }
    }
    class CartHolder{
        public NetworkImageView cartImage;
        public TextView cartName,cartSize,cartColor;
        public NumberPicker cartNum;
        public CheckBox cartCheck;
    }

}
