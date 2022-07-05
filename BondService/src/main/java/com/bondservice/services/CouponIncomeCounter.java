package com.bondservice.services;

import com.bondservice.model.Bond;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class CouponIncomeCounter {


    public Bond countCouponIncome(Bond bond) {
        List<Bond.Coupon> coupons = bond.getCoupons();
        LocalDate nowDate = LocalDate.now();
        LocalDate afterYear = nowDate.plusYears(1);
        long couponPeriod = coupons.get(2).getCouponDate().toEpochDay()
                - coupons.get(1).getCouponDate().toEpochDay();
        BigDecimal couponIncome = BigDecimal.ZERO;
        double couponValue = 0;
        double couponPercent = 0;
        LocalDate nextCoupon = null;
        boolean amortization = false;

        for (int i = 0; i < coupons.size(); i++) {
            Bond.Coupon coupon = coupons.get(i);
            if (i > 0 && coupons.get(i).getValue()
                    < coupons.get(i - 1).getValue()) {
                amortization = true;
            }
            if (coupon.getCouponDate().toEpochDay() - nowDate.toEpochDay() < couponPeriod &&
                    coupon.getCouponDate().toEpochDay() > nowDate.toEpochDay()) {
                BigDecimal dayCouponValue = new BigDecimal(String.valueOf(coupon.getValue()))
                        .divide(new BigDecimal(String.valueOf(couponPeriod)), 3, RoundingMode.CEILING);
                BigDecimal NKD = new BigDecimal(String.valueOf(coupon.getCouponDate().toEpochDay() - nowDate.toEpochDay()))
                        .multiply(dayCouponValue);
                couponIncome = couponIncome.add(NKD);
                couponValue = coupon.getValue();
                couponPercent = coupon.getValueprc();
                nextCoupon = coupon.getCouponDate();

            } else if (coupon.getCouponDate().toEpochDay() > nowDate.toEpochDay() &&
                    afterYear.toEpochDay() > coupon.getCouponDate().toEpochDay()) {
                couponIncome = couponIncome.add(new BigDecimal(String.valueOf(coupon.getValue())));

                if (couponValue == 0) {
                    couponValue = coupon.getValue();
                    couponPercent = coupon.getValueprc();
                    nextCoupon = coupon.getCouponDate();
                }
            } else if (coupon.getCouponDate().toEpochDay() - afterYear.toEpochDay() < couponPeriod &&
                    coupon.getCouponDate().toEpochDay() > afterYear.toEpochDay()) {
                BigDecimal dayCouponValue = new BigDecimal(String.valueOf(coupon.getValue()))
                        .divide(new BigDecimal(String.valueOf(couponPeriod)), 3, RoundingMode.CEILING);
                BigDecimal NKD = new BigDecimal(String.valueOf(coupon.getCouponDate().toEpochDay() - afterYear.toEpochDay()))
                        .multiply(dayCouponValue);
                couponIncome = couponIncome.add(new BigDecimal(String.valueOf(coupon.getValue()))
                        .subtract(NKD));

            }
        }

        if (!couponIncome.equals(BigDecimal.ZERO)) {
            bond.setCouponIncome(couponIncome.doubleValue());
            bond.setNextCouponDate(nextCoupon);
            bond.setAmortization(amortization);
            bond.setCouponPeriod(couponPeriod);
            bond.setCouponNextValue(couponValue);
            bond.setCouponPercent(couponPercent);


            BigDecimal couponIncome100 = couponIncome.multiply(new BigDecimal("100"));
            BigDecimal couponIncomePercent= couponIncome100
                    .divide(new BigDecimal(String.valueOf(bond.getPrevPrice())), 3, RoundingMode.CEILING);
            bond.setCouponIncomePercent(couponIncomePercent.doubleValue());

        } else{
            bond.setMessage("Нет объявленных выплат купонов " +
                    "в течение последующих 12 месяцев");
        }

        return bond;
    }
}
