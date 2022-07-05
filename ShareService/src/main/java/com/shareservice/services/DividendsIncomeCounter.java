package com.shareservice.services;

import com.shareservice.model.Share;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DividendsIncomeCounter {

    public Share countDividendsIncome(Share share) {
        List<Share.Dividends> dividendsList = share.getDividendsList();
        LocalDate nowDate = LocalDate.now();
        LocalDate afterYear = nowDate.plusYears(1);
        BigDecimal dividendsIncome = BigDecimal.ZERO;
        List<Share.Dividends> dividendsInYearList = new ArrayList<>();
        int n = 0;

        for (int i = 0; i < dividendsList.size(); i++) {
            Share.Dividends dividends = dividendsList.get(i);
            if (nowDate.toEpochDay() < dividends.getRegistryCloseDate().toEpochDay()
                    && dividends.getRegistryCloseDate().toEpochDay() < afterYear.toEpochDay()) {
                dividendsIncome = dividendsIncome.add(new BigDecimal(String.valueOf(dividends.getValue())));
                n = n + 1;
                dividendsInYearList.add(dividends);
            }

        }


        share.setDividendsInYear(n);

        if (!dividendsIncome.equals(BigDecimal.ZERO)) {
            share.setDividendsIncome(dividendsIncome.doubleValue());
            share.setDividendsIncomeLot(dividendsIncome.multiply(new BigDecimal(String.valueOf(share.getLotSize())))
                    .doubleValue());
            share.setDividendsInYearList(dividendsInYearList);
            BigDecimal dividendsIncome100 = dividendsIncome.multiply(new BigDecimal("100"));
            BigDecimal dividendsIncomePrc = dividendsIncome100
                    .divide(new BigDecimal(String.valueOf(share.getPrevPrice())), 3, RoundingMode.CEILING);
            share.setDividendsIncomePrc(dividendsIncomePrc.doubleValue());

        } else {
            share.setMessage("Нет известных выплат дивидендов " +
                    "в течение последующих 12 месяцев");
        }

        return share;
    }

}