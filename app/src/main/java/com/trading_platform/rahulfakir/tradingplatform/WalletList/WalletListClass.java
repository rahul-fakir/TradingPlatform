package com.trading_platform.rahulfakir.tradingplatform.WalletList;

/**
 * Created by rahulfakir on 7/18/17.
 */

public class WalletListClass {
    private String title, genre, year;

    public WalletListClass() {
    }

    public WalletListClass(String title, String genre, String year) {
        this.title = title;
        this.genre = genre;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}