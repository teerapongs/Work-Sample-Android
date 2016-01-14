package com.bananacoding.weather1.model;

/**
 * Created by chamnarn on 12/28/15.
 */
public class RSSWeather {
    String title;
    String link;
    String temp;
    String pubdate;
    String woeid;


    public RSSWeather() {
    }

    public RSSWeather(String title, String link, String temp, String pubdate) {
        this.title = title;
        this.link = link;
        this.temp = temp;
        this.pubdate = pubdate;

    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getLink() { return link;}

    public void setLink(String link) {
        this.link = link;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public RSSWeather(String woeid) {
        this.woeid = woeid;
    }

    public  String getWoeid(){ return woeid;}

    public void setWoeid(String woeid){this.woeid = woeid;}

}
