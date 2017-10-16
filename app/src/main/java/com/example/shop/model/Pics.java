package com.example.shop.model;

/**
 * Created by 杨超 on 2017/9/24.
 */

public class Pics {
    private int picId;
    private int goodsId;
    private String picUrl;
    public void setPicId(int picId) {
        this.picId = picId;
    }
    public int getPicId() {
        return picId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
    public int getGoodsId() {
        return goodsId;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public String getPicUrl() {
        return picUrl;
    }


}
