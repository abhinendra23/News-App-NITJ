package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;


public class HomePageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;
    DatabaseReference databaseReference, userReference;
    StorageReference storageReference;
    FirebaseAuth auth;
    FirebaseUser user;


    TextView Navigation_Name, Navigation_email;
    ImageView ProfilePic ;

    public void OnClick(View view)
    {
        Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post").child("CodingClub");
        userReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid());

        Navigation_Name = (TextView)findViewById(R.id.Navigation_Name);
        Navigation_email = (TextView)findViewById(R.id.Navigation_email);
        ProfilePic = (ImageView)findViewById(R.id.ProfilePic);

//        userReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Log.i("abhi",user.getEmail().toString());
//                String email = dataSnapshot.child("email").getValue().toString();
//                Log.i("abhi",user.getEmail().toString());
//                Log.i("abhi",email);
//
//                Navigation_email.setText(email);
//                Log.i("abhi",user.getEmail().toString());
//                String name = dataSnapshot.child("name").getValue().toString();
//                Log.i("abhi",user.getEmail().toString());
//                Navigation_Name.setText(name);
//                Log.i("abhi",user.getEmail().toString());
//                String url = dataSnapshot.child("profile_pic_url").getValue().toString();
//                Log.i("abhi",user.getEmail().toString());
//
//                Picasso.get().load(url).transform(new CircleTransform()).into(ProfilePic);
//                Log.i("abhi",user.getEmail().toString());
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

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
                int id = item.getItemId();
                if (id == R.id.articles) {

                    Intent i = new Intent(getApplicationContext(),ArticleActivity.class);
                    startActivity(i);


                }


                else if (id == R.id.placements) {

                    Intent i = new Intent(getApplicationContext(),PlacementActivity.class);
                    startActivity(i);



                }
                else if (id == R.id.societies) {

                    Intent i = new Intent(getApplicationContext(),ClubActivity.class);

                    startActivity(i);


                }
                else if (id == R.id.submit_post) {

                    userReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String type = dataSnapshot.child("type").getValue().toString();
                            Log.i("Usertype",type);
                            if(type.equals("EndUser"))
                            {
                                Intent i = new Intent(getApplicationContext(),SubmitPostActivity.class);
                                startActivity(i);

                            }
                            else if(type.equals("ClubHead"))
                            {
                                Intent i = new Intent(getApplicationContext(),SubmitPostActivity.class);
                                startActivity(i);

                            }
                            else
                            {
                                Intent i = new Intent(getApplicationContext(),PlacementInput.class);
                                startActivity(i);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else if (id == R.id.feedback) {

                }
                else if (id == R.id.profile_account) {

                    Intent i = new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(i);
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        navigationView.getHeaderView(0).setBackgroundColor(Color.RED);
        findViewById(R.id.Navigation_email);


        int a = 0;

        productList = new ArrayList<>();

        Log.i("check", "pass1");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        Log.i("check", "pass2");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ProductAdapter adapter = new ProductAdapter(HomePageActivity.this, productList);
        recyclerView.setAdapter(adapter);



        //initializing the productlist

        ArrayList<PostInfo> list = new ArrayList<>();
//        productList = new ArrayList<>();

        databaseReference.orderByChild("timestamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PostInfo post = dataSnapshot.getValue(PostInfo.class);
                Product obj = new Product(Product.IMAGE_TYPE,post.title,post.description,post.image_url);
                productList.add(obj);
                System.out.println("size: " + productList.size());
                adapter.notifyDataSetChanged();

                //setting adapter to recyclerview


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        databaseReference.addValueEventListener(
//                new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                productList.clear();
//                for(DataSnapshot postSnapshot: dataSnapshot.getChildren())
//                {
//                    PostInfo post = postSnapshot.getValue(PostInfo.class);
//                    Product obj = new Product(Product.IMAGE_TYPE,post.title,post.description,post.image_url);
//                    productList.add(obj);
//                    System.out.println("size: " + productList.size());
//
//                }
//
//                ProductAdapter adapter = new ProductAdapter(HomePageActivity.this, productList);
//
//                //setting adapter to recyclerview
//
//
//                recyclerView.setAdapter(adapter);
//           }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        Log.i("check", "pass3");
        //adding some items to our list


        //creating recyclerview adapter
        Log.i("check", "pass4");
//        Log.i("check",productList.get(0).getUrl());

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
            Log.i("kuch bhi", "kuch bh");
            Toast.makeText(this, "Kdkfalkj", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.placements) {



        } else if (id == R.id.societies) {


        } else if (id == R.id.submit_post) {

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
