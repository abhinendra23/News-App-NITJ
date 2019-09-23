package com.example.finalproject;

public class Article {
    private String headline;
    private String writer;
    private int image;


    public Article(String headline, String writer, int image) {
        this.headline = headline;
        this.writer = writer;
        this.image = image;
    }

    public String getHeadline() {
        return headline;
    }

    public String getWriter() {
        return writer;
    }

    public int getImage() {
        return image;
    }
}