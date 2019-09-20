package com.example.finalproject;

public class Product {
    public static final int TEXT_TYPE=0;
    public static final int IMAGE_TYPE=1;
    private int type;
    //private int id;
    private String title,url;
    private String desc;
    //private double rating;
    //private double price;
    private int image;


    public Product(int type, String title, String desc, String url) {
        this.type=type;

        this.title = title;
        this.desc = desc;

        this.url = url;
    }

    public int getType() {
        return type;
    }
    public String getUrl() {
        return url;
    }


    public String getTitle() {
        return title;
    }
    public String getShortdesc() {
        return desc;
    }



    public int getImage() {
        return image;
    }
}
