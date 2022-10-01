package com.userinterface.model;

import java.time.LocalDate;
import java.util.List;

public class Bond {

    private String secId;

    private String shortName;

    private String latName;

    private double prevPrice;

    private long couponPeriod;

    private double couponNextValue;

    private double couponPercent;

    private boolean amortization;

    private String nextCouponDate;

    private double couponIncome;

    private double couponIncomePercent;

    private List<Coupon> coupons;

    private String error;

    private String message;

    public static class Coupon {

        private String couponDate;

        private double value;

        private double valueprc;

        public Coupon() {
        }

        public String getCouponDate() {
            return couponDate;
        }

        public void setCouponDate(String couponDate) {
            this.couponDate = couponDate;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public double getValueprc() {
            return valueprc;
        }

        public void setValueprc(double valueprc) {
            this.valueprc = valueprc;
        }
    }

    public Bond() {

    }

    public String getSecId() {
        return secId;
    }

    public void setSecId(String secId) {
        this.secId = secId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLatName() {
        return latName;
    }

    public void setLatName(String latName) {
        this.latName = latName;
    }

    public double getPrevPrice() {
        return prevPrice;
    }

    public void setPrevPrice(double prevPrice) {
        this.prevPrice = prevPrice;
    }

    public long getCouponPeriod() {
        return couponPeriod;
    }

    public void setCouponPeriod(long couponPeriod) {
        this.couponPeriod = couponPeriod;
    }

    public double getCouponNextValue() {
        return couponNextValue;
    }

    public void setCouponNextValue(double couponNextValueValue) {
        this.couponNextValue = couponNextValueValue;
    }

    public double getCouponPercent() {
        return couponPercent;
    }

    public void setCouponPercent(double couponPercent) {
        this.couponPercent = couponPercent;
    }

    public String isAmortization() {
        if(amortization){
            return "есть";
        } else {
            return "нет";
        }
    }

    public void setAmortization(boolean amortization) {
        this.amortization = amortization;
    }

    public String getNextCouponDate() {
        return nextCouponDate;
    }

    public void setNextCouponDate(String nextCoupon) {
        this.nextCouponDate = nextCoupon;
    }

    public double getCouponIncome() {
        return couponIncome;
    }

    public void setCouponIncome(double couponIncome) {
        this.couponIncome = couponIncome;
    }

    public double getCouponIncomePercent() {
        return couponIncomePercent;
    }

    public void setCouponIncomePercent(double couponIncomePercent) {
        this.couponIncomePercent = couponIncomePercent;
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Bond{" +
                "secId='" + secId + '\'' +
                ", shortName='" + shortName + '\'' +
                ", latName='" + latName + '\'' +
                ", prevPrice=" + prevPrice +
                ", couponPeriod=" + couponPeriod +
                ", couponNextValue=" + couponNextValue +
                ", couponPercent=" + couponPercent +
                ", amortization=" + amortization +
                ", nextCouponDate=" + nextCouponDate +
                ", couponIncome=" + couponIncome +
                ", couponIncomePercent=" + couponIncomePercent +
                ", coupons=" + coupons +
                '}';
    }
}
