package com.example.shop.model;

/**
 * Created by 杨超 on 2017/9/28.
 */

public class GoodsViewGroupItem {
    private String key;
    private String value;
    public GoodsViewGroupItem(String key,String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
