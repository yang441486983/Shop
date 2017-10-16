package com.example.shop.model;

import java.util.List;

/**
 * Created by 杨超 on 2017/9/24.
 */

public class GoodsColors {
    private int goodsId;
    private int cateId;
    private String goodsName;
    private String goodsDisc;
    private int goodsPrice;
    private int goodsDiscount;
    private int goodsStock;
    private String goodsOrigin;
    private String goodsMaterial;
    private int goodsPostalfee;
    private String goodsDate;
    private int goodsSales;
    private String goodsPic;
    private String goodsPicFile;
    private List<String> pics;
    private List<String> sizes;
    private List<Colors> colors;
    private List<Stocks> stocks;
    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
    public int getGoodsId() {
        return goodsId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }
    public int getCateId() {
        return cateId;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsDisc(String goodsDisc) {
        this.goodsDisc = goodsDisc;
    }
    public String getGoodsDisc() {
        return goodsDisc;
    }

    public void setGoodsPrice(int goodsPrice) {
        this.goodsPrice = goodsPrice;
    }
    public int getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsDiscount(int goodsDiscount) {
        this.goodsDiscount = goodsDiscount;
    }
    public int getGoodsDiscount() {
        return goodsDiscount;
    }

    public void setGoodsStock(int goodsStock) {
        this.goodsStock = goodsStock;
    }
    public int getGoodsStock() {
        return goodsStock;
    }

    public void setGoodsOrigin(String goodsOrigin) {
        this.goodsOrigin = goodsOrigin;
    }
    public String getGoodsOrigin() {
        return goodsOrigin;
    }

    public void setGoodsMaterial(String goodsMaterial) {
        this.goodsMaterial = goodsMaterial;
    }
    public String getGoodsMaterial() {
        return goodsMaterial;
    }

    public void setGoodsPostalfee(int goodsPostalfee) {
        this.goodsPostalfee = goodsPostalfee;
    }
    public int getGoodsPostalfee() {
        return goodsPostalfee;
    }

    public void setGoodsDate(String goodsDate) {
        this.goodsDate = goodsDate;
    }
    public String getGoodsDate() {
        return goodsDate;
    }

    public void setGoodsSales(int goodsSales) {
        this.goodsSales = goodsSales;
    }
    public int getGoodsSales() {
        return goodsSales;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }
    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPicFile(String goodsPicFile) {
        this.goodsPicFile = goodsPicFile;
    }
    public String getGoodsPicFile() {
        return goodsPicFile;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }
    public List<String> getPics() {
        return pics;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }
    public List<String> getSizes() {
        return sizes;
    }

    public void setColors(List<Colors> colors) {
        this.colors = colors;
    }
    public List<Colors> getColors() {
        return colors;
    }

    public void setStocks(List<Stocks> stocks) {
        this.stocks = stocks;
    }
    public List<Stocks> getStocks() {
        return stocks;
    }

}
