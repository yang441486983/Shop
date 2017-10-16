package com.example.shop.model;

/**
 * Created by 杨超 on 2017/9/12.
 */

public class Reg {
    private String reg;

    public Reg(String reg) {
        this.reg = reg;
    }

    @Override
    public String toString() {
        return "Reg{" +
                "reg='" + reg + '\'' +
                '}';
    }

    public String getReg() {
        return reg;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }
}
