package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PlacementActivity extends AppCompatActivity {


    List<Cards> cardsList;

    //the recyclerview
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placement);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Placements");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //initializing the productlist
        cardsList = new ArrayList<>();

        Log.i("check","pass3");
        //adding some items to our list
        cardsList.add(
                new Cards(
                        "Company: uhg",
                        "Package : 12 LPA",
                        "Branch: cse\n1.vikas\n2.abhi"));

        cardsList.add(
                new Cards(
                        "Company : samsung",
                        "Package : 20 LPA",
                        "Branch: cse\n1.sudhanshu"));

        cardsList.add(
                new Cards(
                        "Company: amazon",
                        "Package : 30 LPA",
                        "Branch: cse\n1.ankit\n2.aditya"));

        //creating recyclerview adapter

        CardAdapter adapter = new CardAdapter(this, cardsList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
        Log.i("check","pass5");

    }
}
