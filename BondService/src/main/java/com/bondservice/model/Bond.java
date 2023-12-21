package com.bondservice.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
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

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Coupon {

        private String couponDate;

        private double value;

        private double valueprc;

    }

}
