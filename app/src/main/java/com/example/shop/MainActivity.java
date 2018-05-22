package com.example.shop;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.shop.Fragment.HomepageFragment;
import com.example.shop.Fragment.HotSalesFragment;
import com.example.shop.Fragment.MyFragment;
import com.example.shop.Fragment.ShoppingFragment;
import com.example.shop.util.SharedPreferenceHelper;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static com.example.shop.common.UrlAddress.picUrl;
import static com.example.shop.common.UrlAddress.searchUrl;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener{
    HomepageFragment homepageFragment;
    HotSalesFragment hotSalesFragment;
    MyFragment myFragment;
    ShoppingFragment shoppingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottomBar);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.homepage,"首页")).
                addItem(new BottomNavigationItem(R.drawable.kind,"分类")).
                addItem(new BottomNavigationItem(R.drawable.shoppingcart,"购物车")).
                addItem(new BottomNavigationItem(R.drawable.my,"我的")).initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
    }

    private void setDefaultFragment() {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        homepageFragment=new HomepageFragment();
        fragmentTransaction.add(R.id.fragmentshow,homepageFragment);
        fragmentTransaction.commit();
    }
    private void hideFragments(FragmentTransaction transaction){
        if (homepageFragment != null){
            transaction.hide(homepageFragment);
        }
        if (hotSalesFragment != null){
            transaction.hide(hotSalesFragment);
        }
        if (shoppingFragment != null){
            transaction.hide(shoppingFragment);
        }
        if (myFragment != null){
            transaction.hide(myFragment);
        }
    }

    @Override
    public void onTabSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (position){
            case 0:
                if (homepageFragment == null) {
                    homepageFragment = new HomepageFragment();
                    fragmentTransaction.add(R.id.fragmentshow,homepageFragment);
                }else {
                    fragmentTransaction.show(homepageFragment);
                }
                break;
            case 1:
                if (hotSalesFragment == null){
                    hotSalesFragment = new HotSalesFragment();
                    fragmentTransaction.add(R.id.fragmentshow,hotSalesFragment);
                }else {
                    fragmentTransaction.show(hotSalesFragment);
                }
                break;
            case 2:
                if (shoppingFragment == null){
                    shoppingFragment = new ShoppingFragment();
                    fragmentTransaction.add(R.id.fragmentshow,shoppingFragment);
                }else {
                    reLoadCartFragment();
                    fragmentTransaction.show(shoppingFragment);
                }
                break;
            case 3:
                if (myFragment == null){
                    myFragment = new MyFragment();
                    fragmentTransaction.add(R.id.fragmentshow,myFragment);
                }else {
                    fragmentTransaction.show(myFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    public void reLoadCartFragment(){
        getSupportFragmentManager().beginTransaction().remove(shoppingFragment).commit();
        shoppingFragment = new ShoppingFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentshow,shoppingFragment).commit();
    }

}
