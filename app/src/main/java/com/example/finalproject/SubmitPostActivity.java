package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class SubmitPostActivity extends AppCompatActivity {

    EditText PostTitle, PostDescription;
    ImageView Image;
    Button ImageButton,Submit;

    DatabaseReference databaseReference, userInfoRefernce;
    StorageReference storageReference;
    FirebaseUser user;
    FirebaseAuth auth;

    Uri imageUri;
    Uri downloadUrl;
    UploadTask uploadTask;
    String url,email;




    ProgressDialog progress;


    public void set_image(View view)
    {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 12);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // progress.setMessage("Updating Profile Pic");

        if (requestCode == 12 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            Picasso.get().load(imageUri).into(Image);

        }
    }

    public void set_image_util(View view) {

        progress.show();
        final StorageReference imageRef = storageReference;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Image.setDrawingCacheEnabled(true);
        Image.buildDrawingCache();

        ((BitmapDrawable) Image.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 50, baos);
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
                        progress.dismiss();
                        uploadTask.cancel();
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       uploadTask.cancel();
                                       progress.cancel();
                                   }
                               }

        ).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                           downloadUrl = uri;
                           Toast.makeText(getApplicationContext(), downloadUrl.toString(), Toast.LENGTH_SHORT).show();

                           String title = PostTitle.getText().toString();
                           String desc = PostDescription.getText().toString();
                           Long tsLong = System.currentTimeMillis()/1000;
                           //String ts = tsLong.toString();

                           PostInfo obj = new PostInfo(title,desc,downloadUrl.toString(),tsLong);

                           userInfoRefernce.push().setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful())
                                   {
                                       Toast.makeText(SubmitPostActivity.this, "post updated to user", Toast.LENGTH_SHORT).show();
                                   }
                                   else
                                   {
                                       Toast.makeText(SubmitPostActivity.this, "post not updated to user", Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });


                           databaseReference.push().setValue(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()) {
                                       progress.dismiss();
                                       Toast.makeText(getApplicationContext(), "post updated to postInfo", Toast.LENGTH_SHORT).show();
                                   } else {
                                       progress.dismiss();
                                       Toast.makeText(getApplicationContext(), "post not Updated to postImfo", Toast.LENGTH_SHORT).show();
                                   }

                                   finish();
                                   Intent i = new Intent(getApplicationContext(),HomePageActivity.class);
                                   startActivity(i);


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
        setContentView(R.layout.activity_submit_article);

        PostTitle = (EditText)findViewById(R.id.PostTitle);
        PostDescription = (EditText)findViewById(R.id.PostDescription);
        Image = (ImageView)findViewById(R.id.Image);
        ImageButton = (Button)findViewById(R.id.ImageButton);
        Submit = (Button)findViewById(R.id.Submit);

        progress=new ProgressDialog(this);
        progress.setMessage("uploading...");
        progress.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post").child("CodingClub");
        storageReference = FirebaseStorage.getInstance().getReference().child("Post").child(user.getUid()+ts);
        userInfoRefernce = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid()).child("Post");

    }
}
