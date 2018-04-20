package com.example.shop.Fragment;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.example.shop.R;
import com.example.shop.SearchActivity;
import com.example.shop.util.BitmapCache;
import com.example.shop.util.SharedPreferenceHelper;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.shop.common.UrlAddress.getAllCatesUrl;
import static com.example.shop.common.UrlAddress.getGoodsByCateUrl;
import static com.example.shop.common.UrlAddress.hotSalesUrl;
import static com.example.shop.common.UrlAddress.picUrl;
import static com.example.shop.common.UrlAddress.searchUrl;

public class HotSalesFragment extends Fragment {
    RequestQueue queue = null;
    List<String> tabId,tabName;
    TabLayout kindTab;
    ViewPager kindPager;
    kindAdapter adapter;
    int cateId;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_kinds,container,false);

        setHasOptionsMenu(true);
        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
        tabId = new ArrayList<>();
        tabName = new ArrayList<>();
        getAllCates();

    }
    public void init() {
        kindPager = (ViewPager)getActivity().findViewById(R.id.kind_pager);
        kindTab = (TabLayout)getActivity().findViewById(R.id.kind_tab);
        adapter = new kindAdapter(getFragmentManager());
        Log.e("测试",tabName.toString());
        for (int i = 0;i<tabName.size();i++){
            kindTab.addTab(kindTab.newTab().setText(tabName.get(i).toString()));
            Log.e("导航栏",tabName.get(i));
        }
        kindPager.setAdapter(adapter);
        kindTab.setupWithViewPager(kindPager);
        for (int i = 0;i<tabName.size();i++){
            kindTab.getTabAt(i).setText(tabName.get(i));
        }
        kindTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                kindPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
    public void getAllCates(){
        StringRequest request = new StringRequest(Request.Method.POST, getAllCatesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("tab",response);
                try {
                    JSONObject jo = new JSONObject(response);
                    JSONArray catesArray = jo.getJSONArray("cates");
                    for (int i = 0;i<catesArray.length();i++){
                        JSONObject jsonObject = catesArray.getJSONObject(i);
                        String id = jsonObject.getString("cateId");
                        String name = jsonObject.getString("cateName");
                        tabId.add(id);
                        tabName.add(name);
                    }
                    init();
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
    public void getGoodsByCate(){
        StringRequest request = new StringRequest(Request.Method.POST, getGoodsByCateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){//提交参数
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("cateId",String.valueOf(cateId));
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
    }
    class kindAdapter extends FragmentPagerAdapter {

        public kindAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putString("kindId",tabId.get(position));
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return tabId.size();
        }
    }

}