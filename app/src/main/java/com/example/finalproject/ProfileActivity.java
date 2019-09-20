package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
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


    ImageView User_ProfilePic, ChangeProfilePic, ChangeName, ChangePassowrd, Savechanges;
    TextView Name, Email, Back_to_Home, User_name, User_email, User_password, SignOut;


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


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();



        databaseReference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid());
        storageReference = FirebaseStorage.getInstance().getReference().child("UserInfo").child(user.getUid());



        User_ProfilePic = (ImageView)findViewById(R.id.User_ProfilePic);
        ChangeProfilePic= (ImageView)findViewById(R.id.ChangeProfilePic);
        ChangeName = (ImageView)findViewById(R.id.ChangeName);
        ChangePassowrd = (ImageView)findViewById(R.id.ChangePassword);
        Savechanges = (ImageView)findViewById(R.id.SaveChanges);

        Name = (TextView)findViewById(R.id.Name);
        Email = (TextView)findViewById(R.id.Email);
        Back_to_Home = (TextView)findViewById(R.id.Back_to_Home);
        User_name = (TextView)findViewById(R.id.User_name);
        User_email = (TextView)findViewById(R.id.User_email);
        User_password = (TextView)findViewById(R.id.User_password);
        SignOut = (TextView)findViewById(R.id.SignOut);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                email = dataSnapshot.child("email").getValue().toString();
                Email.setText(email);
                User_email.setText(email);
                url = dataSnapshot.child("profile_pic_url").getValue().toString();
                Picasso.get().load(url).transform(new CircleTransform()).into(User_ProfilePic);


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

            }
        });

        ChangePassowrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }
}
