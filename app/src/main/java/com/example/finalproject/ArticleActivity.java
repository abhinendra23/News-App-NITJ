package com.example.finalproject;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity {

    public void clickArticle(View view)
    {
        Intent i = new Intent(getApplicationContext(), ArticlePage.class);
        startActivity(i);
    }

    List<Article> articleList;

    //the recyclerview
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Articles");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initializing the productlist
        articleList = new ArrayList<Article>();

        Log.i("check","pass3");
        //adding some items to our list
        articleList.add(
                new Article(
                        "abcdefg",
                        "vikas",
                        R.drawable.b1));

        articleList.add(
                new Article(
                        "hijklmn",
                        "sudhanshu",
                        R.drawable.b2));

        articleList.add(
                new Article(
                        "fdxgcfhgjbh",
                        "fghjk",
                        R.drawable.b3));
        articleList.add(
                new Article(
                        "sthj",
                        "edfghuji",
                        R.drawable.b4));
        articleList.add(
                new Article(
                        "dfghjk",
                        "rdftgyhujik",
                        R.drawable.b5));
        articleList.add(
                new Article(
                        "edrftgyhuj",
                        "rdftgyhuj",
                        R.drawable.b6));

        //creating recyclerview adapter

        ArticleAdapter adapter = new ArticleAdapter(this, articleList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
        Log.i("check","pass5");
    }
}


