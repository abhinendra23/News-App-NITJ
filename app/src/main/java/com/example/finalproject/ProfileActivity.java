package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;


class CircleTransform implements Transformation {
    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    @Override
    public String key() {
        return "circle";
    }
}

public class ProfileActivity extends AppCompatActivity {


    ImageView User_ProfilePic, ChangeProfilePic, ChangeName, ChangePassowrd, Savechanges,SaveName,SavePassword;
    TextView Name, Email, Back_to_Home,  User_email, SignOut;
    EditText User_name, User_password;

    Button DeleteAccount;


    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser user;
    FirebaseAuth auth;

    Uri imageUri;
    Uri downloadUrl;
    UploadTask uploadTask;
    String url,email;

    ProgressDialog progressBar;


    public void Change_profile_pic(View view)
    {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 12);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setMessage("Updating Profile Pic");

        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            Picasso.get().load(imageUri).transform(new CircleTransform()).into(User_ProfilePic);
            Savechanges.setAlpha(1.0f);
            ImageView changephotobutton = findViewById(R.id.ChangeProfilePic);
            ImageView setphotbutton = findViewById(R.id.SaveChanges);
            setphotbutton.setVisibility(View.VISIBLE);
            changephotobutton.setVisibility(View.INVISIBLE);




        }
    }

        public void change_profile_pic_util(View view) {


            progressBar.show();
            final StorageReference imageRef = storageReference;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            User_ProfilePic.setDrawingCacheEnabled(true);
            User_ProfilePic.buildDrawingCache();

            ((BitmapDrawable) User_ProfilePic.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] image_data = baos.toByteArray();
            uploadTask = imageRef.putBytes(image_data);

            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                }
            }).addOnPausedListener(
                    new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.dismiss();
                            uploadTask.cancel();
                        }
                    }
            ).addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {
                                           uploadTask.cancel();
                                           progressBar.cancel();
                                       }
                                   }

            ).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri;
                            Toast.makeText(ProfileActivity.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();

                            databaseReference.child("profile_pic_url").setValue(downloadUrl.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        progressBar.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Profile pic Updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressBar.dismiss();
                                        Toast.makeText(ProfileActivity.this, "Some error occured", Toast.LENGTH_SHORT).show();
                                    }

                                    Picasso.get().load(imageUri).transform(new CircleTransform()).into(User_ProfilePic);
                                    Savechanges.setAlpha(0f);
                                    ImageView changephotobutton = findViewById(R.id.ChangeProfilePic);
                                    ImageView setphotbutton = findViewById(R.id.SaveChanges);
                                    setphotbutton.setVisibility(View.INVISIBLE);
                                    changephotobutton.setVisibility(View.VISIBLE);

                                }
                            });
                        }
                    });
                }
                                   }

            );
        }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid());
        storageReference = FirebaseStorage.getInstance().getReference().child("UserInfo").child(user.getUid());



        User_ProfilePic = (ImageView)findViewById(R.id.User_ProfilePic);
        ChangeProfilePic= (ImageView)findViewById(R.id.ChangeProfilePic);
        ChangeName = (ImageView)findViewById(R.id.ChangeName);
        ChangePassowrd = (ImageView)findViewById(R.id.ChangePassword);
        Savechanges = (ImageView)findViewById(R.id.SaveChanges);

        Name = (TextView)findViewById(R.id.Navigation_Name);
        Email = (TextView)findViewById(R.id.Email);
        Back_to_Home = (TextView)findViewById(R.id.Back_to_Home);
        User_name = (EditText) findViewById(R.id.User_name);
        User_email = (TextView)findViewById(R.id.User_email);
        User_password = (EditText) findViewById(R.id.User_Password);
        SignOut = (TextView)findViewById(R.id.SignOut);
        SaveName = (ImageView)findViewById(R.id.SaveName);
        SavePassword = (ImageView)findViewById(R.id.SavePasssword);




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                email = dataSnapshot.child("email").getValue().toString();

                Email.setText(email);
                User_email.setText(email);
                url = dataSnapshot.child("profile_pic_url").getValue().toString();
                Picasso.get().load(url).transform(new CircleTransform()).into(User_ProfilePic);

                String name=dataSnapshot.child("name").getValue().toString();
                Name.setText(name);
                User_name.setText(name);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        ChangeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,12);
            }
        });

        Back_to_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();


            }
        });

        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setMessage("Signing Out...");
                progressBar.show();

                auth.signOut();
                finish();
                progressBar.dismiss();
                Intent i = new Intent(getApplicationContext(),LoginPageActivity.class);
                startActivity(i);
            }
        });

        ChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText editText = findViewById(R.id.User_name);
                editText.setFocusableInTouchMode(true);
                editText.setInputType(1);
                ImageView editname = (ImageView) findViewById(R.id.ChangeName);
                editname.setVisibility(View.INVISIBLE);
                editText.setText("");
                editText.setHint("Enter name");
                ImageView setnamebutton = (ImageView) findViewById(R.id.SaveName);
                setnamebutton.setVisibility(View.VISIBLE);




            }
        });

        ChangePassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText password = findViewById(R.id.User_Password);
                password.setFocusableInTouchMode(true);
                password.setInputType(1);
                ImageView editpassword = (ImageView) findViewById(R.id.ChangePassword);
                editpassword.setVisibility(View.INVISIBLE);
                password.setText("");
                password.setHint("Enter new password");
                ImageView setpasswordbutton = (ImageView) findViewById(R.id.SavePasssword);
                setpasswordbutton.setVisibility(View.VISIBLE);


            }
        });

        SaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView editname = (ImageView) findViewById(R.id.ChangeName);
                editname.setVisibility(View.VISIBLE);
                ImageView setnamebutton = (ImageView) findViewById(R.id.SaveName);
                setnamebutton.setVisibility(View.INVISIBLE);
                final EditText editText = (EditText) findViewById(R.id.User_name);
                editText.setInputType(0);
                editText.setFocusableInTouchMode(false);


                final String name=editText.getText().toString();

                databaseReference.child("name").setValue(name).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Log.i("check","name uploded to db successfully");
                            editText.setText(name);
                            Name.setText(name);
                        }
                        else
                        {
                            Log.i("check","can't uploded to db ");

                        }

                    }
                });

            }
        });

        SavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView editpassword = (ImageView) findViewById(R.id.ChangePassword);
                editpassword.setVisibility(View.VISIBLE);
                ImageView setpasswordbutton = (ImageView) findViewById(R.id.SavePasssword);
                setpasswordbutton.setVisibility(View.INVISIBLE);
                final EditText password = (EditText) findViewById(R.id.User_Password);
                password.setInputType(0);

                password.setFocusableInTouchMode(false);


                final String pass=password.getText().toString();
                if(pass.length()<6)
                {
                    Toast.makeText(ProfileActivity.this, "Password length cannot be less than 6", Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.child("password").setValue(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                user.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            Toast.makeText(ProfileActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                password.setText("********");
                            } else {
                                Log.i("check", "can't uploded password to db ");

                            }

                        }
                    });
                }
            }
        });




    }
}
