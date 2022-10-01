package com.shareservice.model;

import java.time.LocalDate;
import java.util.List;

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

    public static class Dividends{

        private String registryCloseDate;

        private double value;

        public Dividends() {
        }

        public String getRegistryCloseDate() {
            return registryCloseDate;
        }

        public void setRegistryCloseDate(String registryCloseDate) {
            this.registryCloseDate = registryCloseDate;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

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

    public int getLotSize() {
        return lotSize;
    }

    public void setLotSize(int lotSize) {
        this.lotSize = lotSize;
    }

    public double getLotPrice() {
        return lotPrice;
    }

    public void setLotPrice(double lotPrice) {
        this.lotPrice = lotPrice;
    }

    public List<Dividends> getDividendsList() {
        return dividendsList;
    }

    public void setDividendsList(List<Dividends> dividendsList) {
        this.dividendsList = dividendsList;
    }

    public int getDividendsInYear() {
        return dividendsInYear;
    }

    public void setDividendsInYear(int dividendsInYear) {
        this.dividendsInYear = dividendsInYear;
    }

    public List<Dividends> getDividendsInYearList() {
        return dividendsInYearList;
    }

    public void setDividendsInYearList(List<Dividends> dividendsInYearList) {
        this.dividendsInYearList = dividendsInYearList;
    }

    public double getDividendsIncome() {
        return dividendsIncome;
    }

    public void setDividendsIncome(double dividendsIncome) {
        this.dividendsIncome = dividendsIncome;
    }

    public double getDividendsIncomeLot() {
        return dividendsIncomeLot;
    }

    public void setDividendsIncomeLot(double dividendsIncomeLot) {
        this.dividendsIncomeLot = dividendsIncomeLot;
    }

    public double getDividendsIncomePrc() {
        return dividendsIncomePrc;
    }

    public void setDividendsIncomePrc(double dividendsIncomePrc) {
        this.dividendsIncomePrc = dividendsIncomePrc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Share{" +
                "secId='" + secId + '\'' +
                ", shortName='" + shortName + '\'' +
                ", latName='" + latName + '\'' +
                ", prevPrice=" + prevPrice +
                ", lotSize=" + lotSize +
                ", lotPrice=" + lotPrice +
                ", dividendsInYear=" + dividendsInYear +
                ", dividendsIncome=" + dividendsIncome +
                ", dividendsIncomeLot=" + dividendsIncomeLot +
                ", dividendsIncomePrc=" + dividendsIncomePrc +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", dividendsList=" + dividendsList +
                '}';
    }
}
