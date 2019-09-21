package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNameActivity extends AppCompatActivity {

    EditText Name;
    Button Submit;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;


    public void Submit(View view)
    {
        String name = Name.getText().toString();

        databaseReference.setValue(name);
        finish();

        Intent i = new Intent(getApplicationContext(),HomePageActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);

        Name = (EditText)findViewById(R.id.Navigation_Name);
        Submit = (Button) findViewById(R.id.Submit);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid()).child("name");



    }


}
