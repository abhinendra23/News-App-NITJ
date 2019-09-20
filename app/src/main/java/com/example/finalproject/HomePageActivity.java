package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.i("ankit","hello1");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Log.i("ankit","hello2");
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(HomePageActivity.this, "Ran", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        navigationView.getHeaderView(0).setBackgroundColor(Color.RED);


        int a = 0;

        Log.i("check", "pass1");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Log.i("check", "pass2");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //initializing the productlist
        productList = new ArrayList<>();

        Log.i("check", "pass3");
        //adding some items to our list
        productList.add(
                new Product(
                        1,
                        "Apple MacBook Air Core i5 5th Gen - (8 GB/128 GB SSD/Mac OS Sierra)",
                        "13.3 inch, Silver, 1.35 kg",
                        4.3,
                        60000,
                        R.drawable.macbook));

        productList.add(
                new Product(
                        1,
                        "Dell Inspiron 7000 Core i5 7th Gen - (8 GB/1 TB HDD/Windows 10 Home)",
                        "14 inch, Gray, 1.659 kg",
                        4.3,
                        60000,
                        R.drawable.dellinspiron));

        productList.add(
                new Product(
                        1,
                        "Microsoft Surface Pro 4 Core m3 6th Gen - (4 GB/128 GB SSD/Windows 10)",
                        "13.3 inch, Silver, 1.35 kg",
                        4.3,
                        60000,
                        R.drawable.surface));

        //creating recyclerview adapter
        Log.i("check", "pass4");
        ProductAdapter adapter = new ProductAdapter(this, productList);

        //setting adapter to recyclerview


        recyclerView.setAdapter(adapter);
        Log.i("check", "pass5");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Log.i("kuch bhi","kuch bmnmnmnh");

        int id = item.getItemId();
        item.setChecked(true);
        // Handle navigation view item clicks here.
        //i= item.getItemId();

        if (id == R.id.articles) {
            // Handle the camera action
            Log.i("kuch bhi","kuch bh");
            Toast.makeText(this, "Kdkfalkj", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.circulars) {

        } else if (id == R.id.events) {
            Log.i("kuch bhi","kuch bh");
        } else if (id == R.id.placements) {


        } else if (id == R.id.societies) {

        } else if (id == R.id.submit_aricles) {

        } else if (id == R.id.feedback) {

        }else if (id == R.id.profile_account) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.i("kuch bhi", "kuch bmnmnmnh");
        //noinspection SimplifiableIfStatement
        if (id == 4) {
            return true;
        }

       return super.onOptionsItemSelected(item);
    }




}
