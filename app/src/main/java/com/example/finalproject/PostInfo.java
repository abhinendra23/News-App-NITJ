package com.example.finalproject;

import java.io.Serializable;

public class PostInfo implements Serializable {

    public String title, description, image_url;

    public PostInfo(String title, String description, String image_url) {
        this.title = title;
        this.description = description;
        this.image_url = image_url;
    }

    public PostInfo()
    {

    }
}
