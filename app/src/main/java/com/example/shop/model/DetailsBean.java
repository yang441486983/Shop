package com.example.shop.model;

/**
 * Created by 杨超 on 2017/9/24.
 */

public class DetailsBean {
    private GoodsSizes goodsSizes;
    private GoodsColors goodsColors;
    private GoodDetail goodsDetail;
    public void setGoodsSizes(GoodsSizes goodsSizes) {
        this.goodsSizes = goodsSizes;
    }
    public GoodsSizes getGoodsSizes() {
        return goodsSizes;
    }

    public void setGoodsColors(GoodsColors goodsColors) {
        this.goodsColors = goodsColors;
    }
    public GoodsColors getGoodsColors() {
        return goodsColors;
    }

    public void setGoodsDetail(GoodDetail goodsDetail) {
        this.goodsDetail = goodsDetail;
    }
    public GoodDetail getGoodsDetail() {
        return goodsDetail;
    }

}
