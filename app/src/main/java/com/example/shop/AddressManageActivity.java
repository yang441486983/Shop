package com.example.shop;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shop.util.SharedPreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shop.common.UrlAddress.addressUrl;
import static com.example.shop.common.UrlAddress.delAddressUrl;
import static com.example.shop.common.UrlAddress.handleAddressUrl;
import static com.example.shop.common.UrlAddress.setDefaultAddressUrl;

public class AddressManageActivity extends AppCompatActivity {
    RequestQueue queue = null;
    List<String> addrIdList,addrReceiverList,addrTelList,addrProvinceList,addrCityList,addrAreaList,addrContentList,addrIsdefaultList;
    ListView addressList;
    Button addAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manage);
        queue = Volley.newRequestQueue(this);
        init();
        getMyAddress();
        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editAddressDialog(null,null,null,null,null,null,null);
            }
        });
    }
    public void init(){
        addressList = (ListView)findViewById(R.id.address_list);
        addAddress = (Button)findViewById(R.id.add_address);
        addrIdList = new ArrayList<>();
        addrReceiverList = new ArrayList<>();
        addrTelList = new ArrayList<>();
        addrProvinceList = new ArrayList<>();
        addrCityList = new ArrayList<>();
        addrAreaList = new ArrayList<>();
        addrContentList = new ArrayList<>();
        addrIsdefaultList = new ArrayList<>();
    }
    public void getMyAddress(){
        StringRequest request = new StringRequest(Request.Method.POST, addressUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("address",response);
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray jsonArray = jo.getJSONArray("address");
                    for (int i = 0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String addrId = jsonObject.getString("addrId");
                        String province = jsonObject.getString("addrProvince");
                        String city = jsonObject.getString("addrCity");
                        String area = jsonObject.getString("addrArea");
                        String content = jsonObject.getString("addrContent");
                        String receiver = jsonObject.getString("addrReceiver");
                        String tel = jsonObject.getString("addrTel");
                        String isDefault = jsonObject.getString("addrIsdefault");
                        addrIdList.add(addrId);
                        addrProvinceList.add(province);
                        addrCityList.add(city);
                        addrAreaList.add(area);
                        addrContentList.add(content);
                        addrReceiverList.add(receiver);
                        addrTelList.add(tel);
                        addrIsdefaultList.add(isDefault);
                    }
                    Log.e("addrReceiverList",addrReceiverList.toString());
                    Log.e("isDefault",addrIsdefaultList.toString());
                    AddressAdapter addressAdapter = new AddressAdapter();
                    addressList.setAdapter(addressAdapter);
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
                String cookie = SharedPreferenceHelper.getCookie(AddressManageActivity.this);
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }
    class AddressAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return addrIdList.size();
        }

        @Override
        public Object getItem(int i) {
            return addrIdList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            addressHolder holder = new addressHolder();
            if(view == null){
                view = LayoutInflater.from(AddressManageActivity.this).inflate(R.layout.address_item,null);
                holder.receiverName = (TextView)view.findViewById(R.id.receiver_name);
                holder.receiverTel = (TextView)view.findViewById(R.id.receiver_tel);
                holder.receiverProvince = (TextView)view.findViewById(R.id.receiver_province);
                holder.receiverCity = (TextView)view.findViewById(R.id.receiver_city);
                holder.receiverArea = (TextView)view.findViewById(R.id.receiver_area);
                holder.receiverContent = (TextView)view.findViewById(R.id.receiver_content);
                holder.isDefault = (RadioButton)view.findViewById(R.id.receiver_isDefault);
                holder.edit = (Button)view.findViewById(R.id.address_edit);
                holder.delete = (Button)view.findViewById(R.id.address_delete);
                view.setTag(holder);
            }else {
                holder = (addressHolder)view.getTag();
            }
            holder.receiverName.setText(addrReceiverList.get(i).toString());
            holder.receiverTel.setText(addrTelList.get(i));
            holder.receiverProvince.setText(addrProvinceList.get(i)+",");
            holder.receiverCity.setText(addrCityList.get(i)+",");
            holder.receiverArea.setText(addrAreaList.get(i)+",");
            holder.receiverContent.setText(addrContentList.get(i));
            if (addrIsdefaultList.get(i).equals("1")){
                holder.isDefault.setChecked(true);
            }else {
                holder.isDefault.setChecked(false);
            }
            holder.isDefault.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setDefaultAddress(addrIdList.get(i));

                }
            });
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editAddressDialog(addrReceiverList.get(i),addrTelList.get(i),addrProvinceList.get(i),addrCityList.get(i),addrAreaList.get(i),addrContentList.get(i),addrIdList.get(i));

                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delAddress(addrIdList.get(i));
                }
            });
            return view;
        }
    }
    class addressHolder{
        public TextView receiverName,receiverTel,receiverProvince,receiverCity,receiverArea,receiverContent;
        public RadioButton isDefault;
        public Button edit,delete;
    }
    protected void setDefaultAddress(final String changeId){
        StringRequest request = new StringRequest(Request.Method.POST, setDefaultAddressUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                finish();
                Intent intent = new Intent(AddressManageActivity.this,AddressManageActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            //提交参数
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("addrId",changeId);
                return map;
            }
            //写入Cookie
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHashMap = new HashMap<>();
                String cookie = SharedPreferenceHelper.getCookie(AddressManageActivity.this);
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }
    protected void editAddressDialog(String name,String tel,String province,String city,String area,String content,String addressId){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View addressEditView = layoutInflater.inflate(R.layout.edit_address_item,null);
        final EditText editName = (EditText)addressEditView.findViewById(R.id.et_name);
        final EditText editTel = (EditText)addressEditView.findViewById(R.id.et_tel);
        final EditText editProvince = (EditText)addressEditView.findViewById(R.id.et_province);
        final EditText editCity = (EditText)addressEditView.findViewById(R.id.et_city);
        final EditText editArea = (EditText)addressEditView.findViewById(R.id.et_area);
        final EditText editContent = (EditText)addressEditView.findViewById(R.id.et_content);
        AlertDialog.Builder builder = new AlertDialog.Builder(AddressManageActivity.this);
        builder.setTitle("编辑收件地址:");
        editName.setText(name);
        editTel.setText(tel);
        editProvince.setText(province);
        editCity.setText(city);
        editArea.setText(area);
        editContent.setText(content);
        final String id = addressId;
        builder.setView(addressEditView);
        builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (editName.getText().length() != 0&&editTel.getText().length() != 0&&editProvince.getText().length() != 0&& editCity.getText().length() != 0&& editArea.getText().length() != 0&&editContent.getText().length() != 0){
                    StringRequest request = new StringRequest(Request.Method.POST, handleAddressUrl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            finish();
                            Intent intent = new Intent(AddressManageActivity.this,AddressManageActivity.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(AddressManageActivity.this,"保存地址信息失败",Toast.LENGTH_SHORT).show();
                        }
                    }){
                        //提交参数
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            if (id != null){
                                map.put("addrId",id);
                            }
                            map.put("addrReceiver",editName.getText().toString());
                            map.put("addrTel",editTel.getText().toString());
                            map.put("addrProvince",editProvince.getText().toString());
                            map.put("addrCity",editCity.getText().toString());
                            map.put("addrArea",editArea.getText().toString());
                            map.put("addrContent",editArea.getText().toString());
                            return map;
                        }
                        //写入Cookie
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> localHashMap = new HashMap<>();
                            String cookie = SharedPreferenceHelper.getCookie(AddressManageActivity.this);
                            localHashMap.put("Cookie", cookie);
                            return localHashMap;
                        }
                    };
                    queue.add(request);
                }else {
                    Toast.makeText(AddressManageActivity.this,"请填写完整信息",Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    protected void delAddress(String addressId){
        final String delId = addressId;
        StringRequest request = new StringRequest(Request.Method.POST, delAddressUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                finish();
                Intent intent = new Intent(AddressManageActivity.this,AddressManageActivity.class);
                startActivity(intent);
                Toast.makeText(AddressManageActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddressManageActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
            }
        }) {
            //提交参数
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("addrId",delId);
                return map;
        }
            //写入Cookie
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> localHashMap = new HashMap<>();
                String cookie = SharedPreferenceHelper.getCookie(AddressManageActivity.this);
                localHashMap.put("Cookie", cookie);
                return localHashMap;
            }
        };
        queue.add(request);
    }
}
