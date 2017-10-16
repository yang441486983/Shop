package com.example.shop.model;

/**
 * Created by 杨超 on 2017/9/24.
 */

public class Stocks {
    private int stockId;
    private int goodsId;
    private int sizeId;
    private String sizeName;
    private int colorId;
    private String colorName;
    private int stockNum;
    private int salesNum;
    public void setStockId(int stockId) {
        this.stockId = stockId;
    }
    public int getStockId() {
        return stockId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }
    public int getGoodsId() {
        return goodsId;
    }

    public void setSizeId(int sizeId) {
        this.sizeId = sizeId;
    }
    public int getSizeId() {
        return sizeId;
    }

    public void setSizeName(String sizeName) {
        this.sizeName = sizeName;
    }
    public String getSizeName() {
        return sizeName;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }
    public int getColorId() {
        return colorId;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }
    public String getColorName() {
        return colorName;
    }

    public void setStockNum(int stockNum) {
        this.stockNum = stockNum;
    }
    public int getStockNum() {
        return stockNum;
    }

    public void setSalesNum(int salesNum) {
        this.salesNum = salesNum;
    }
    public int getSalesNum() {
        return salesNum;
    }

}
