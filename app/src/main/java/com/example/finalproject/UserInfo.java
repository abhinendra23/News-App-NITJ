package com.example.finalproject;

import java.io.Serializable;

public class UserInfo implements Serializable {

    public String name,password,email,profile_pic_url="https://firebasestorage.googleapis.com/v0/b/campusnewsletter-d9e2d.appspot.com/o/usericon.png?alt=media&token=d5362826-9eb7-4cb3-ba60-90846847240b";

    PostInfo post;
    // list of post objects has to be created
    public boolean isClubHead = false;

    public UserInfo(String name,String password,String email)
    {
        this.name = name;
        this.password = password;
        this.email = email;
    }
    public UserInfo()
    {

    }
}
