package com.shareservice.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class Share {

    private String secId;

    private String shortName;

    private String latName;

    private double prevPrice;

    private int lotSize;

    private double lotPrice;

    private int dividendsInYear;

    private double dividendsIncome;

    private double dividendsIncomeLot;

    private double dividendsIncomePrc;

    private String error;

    private String message;

    private List<Dividends> dividendsList;

    private List<Dividends> dividendsInYearList;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Dividends{

        private String registryCloseDate;

        private double value;

    }

}
