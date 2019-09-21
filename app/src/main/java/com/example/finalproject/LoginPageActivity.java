package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class LoginPageActivity extends AppCompatActivity {


    FirebaseAuth auth;
    EditText id,pass;
    TextView ForgotPassword;
    GoogleSignInClient googleSignInClient ;
    SignInButton signInButton;
    Button forgotPassword;
    DatabaseReference rootReference, dataRefer;
    StorageReference storageReference;
    FirebaseUser user;
    boolean Session;
    Button LoginButton;
    TextView here;
    private ProgressDialog progress;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("login_activity","yes ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);


        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();




        id = (EditText)findViewById(R.id.LoginNameInput);
        pass = (EditText)findViewById(R.id.LoginPasswordInput);
        ForgotPassword = (TextView) findViewById(R.id.ForgotPassword);
        rootReference = FirebaseDatabase.getInstance().getReference();
        LoginButton = (Button)findViewById(R.id.LoginButton);
        here = (TextView)findViewById(R.id.RegisterHere);
        progress=new ProgressDialog(this);
        progress.setMessage("Logging in...");
        progress.setCancelable(false);


        if(currentUser!=null && currentUser.isEmailVerified())
        {
            Log.i("hello",currentUser.getEmail().toString());
            Intent i = new Intent(getApplicationContext(),HomePageActivity.class);

            Toast.makeText(getApplicationContext(), "Welcome " ,Toast.LENGTH_SHORT).show();

            startActivity(i);
            finish();
        }

        here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);

            }
        });


        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(id.getText().toString().equals("") || pass.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(), "email or password can't be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progress.show();

                    auth.signInWithEmailAndPassword(id.getText().toString(),pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            user = auth.getCurrentUser();
                            Log.i("checking","0");
                            if(task.isSuccessful() && user.isEmailVerified() )
                            {

//                                Toast.makeText(getApplicationContext(), "Welcome1 " ,Toast.LENGTH_SHORT).show();
                                String name = "xyz";
                                String id = user.getEmail();
                                String password =pass.getText().toString();
                                final UserInfo obj = new UserInfo(name,password,id);

                                dataRefer = rootReference.child("UserInfo");

                                dataRefer.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(!dataSnapshot.hasChild(user.getUid()))
                                        {
                                            rootReference.child("UserInfo").child(user.getUid()).setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful() )
                                                    {

                                                        Intent i = new Intent(getApplicationContext(),SetNameActivity.class);
                                                        finish();
                                                        progress.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Welcome " ,Toast.LENGTH_SHORT).show();

                                                        startActivity(i);

                                                    }
                                                    else
                                                    {
                                                        progress.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Some Error Occured. Please try again later.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            Intent i = new Intent(getApplicationContext(),HomePageActivity.class);
                                            finish();
//                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            progress.dismiss();
                                            Toast.makeText(getApplicationContext(), "Welcome " ,Toast.LENGTH_SHORT).show();

                                            startActivity(i);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });




                                //updateUI()
                            }
                            else if(!task.isSuccessful())
                            {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Some Error Occured", Toast.LENGTH_SHORT).show();
                            }
                            else if(task.isSuccessful() && !user.isEmailVerified())
                            {
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Please Verify your Email first", Toast.LENGTH_SHORT).show();
                            }
//                            Toast.makeText(getApplicationContext(), "Welcome 2" ,Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });


        ForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(i);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this,gso);
        signInButton = (SignInButton)findViewById(R.id.signInButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.show();
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 9001);
            }
        });



        //SESSION();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 9001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                account.getEmail();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, "Error occured Try Again", Toast.LENGTH_SHORT).show();
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //  Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = auth.getCurrentUser();
                            Intent i = new Intent(getApplicationContext(),HomePageActivity.class);

                            progress.dismiss();
                            Toast.makeText(LoginPageActivity.this, "logged in Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(i);




                            //updateUI(user);
                        } else {
                            Toast.makeText(LoginPageActivity.this, "Error occured try again", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

}
