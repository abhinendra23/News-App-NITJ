package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ClubActivity extends AppCompatActivity {


    public void clickClub(View view)
    {
        Intent i = new Intent(getApplicationContext(),clubpage.class);
        startActivity(i);
    }

    public List<Society> clubList = new ArrayList<Society>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Societies");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.i("check","pass1");
        for(int i=0;i<=50;i++) {
            Log.i("check","pass2");
            Society society = new Society("club"+i);
            clubList.add(society);
        }
        Log.i("check","pass3");
        ListView listView;
        listView = (ListView) findViewById(R.id.listView);
        Log.i("check","pass4");
        //ListViewAdapter adap = new ListViewAdapter(this, EMPLOYEE_LIST);
        listView.setAdapter(new ListViewAdapter(this, clubList));
        Log.i("check","pass5");
    }


}

