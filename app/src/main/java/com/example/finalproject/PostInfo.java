package com.example.finalproject;

import java.io.Serializable;

public class PostInfo implements Serializable {

    public String title, description, image_url;
    public long  timestamp;

    public PostInfo(String title, String description, String image_url,long timestamp) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
        this.timestamp= -timestamp;
    }

    public PostInfo()
    {

    }
}
