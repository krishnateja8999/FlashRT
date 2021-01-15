package com.example.flashnew.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import static com.example.flashnew.Server.Utils.REQUEST_IMAGE_CAPTURE;

public class Profile_Class extends AppCompatActivity {
    private Bitmap photo, OutImage;
    private AppPrefernces prefernces;
    private CircularImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__class);
        Toolbar toolbar = findViewById(R.id.toolbar_t);
        EditText name = findViewById(R.id.name);
        EditText userName = findViewById(R.id.userName);
        EditText passWord = findViewById(R.id.passWord);
        imageView = findViewById(R.id.iv_profile_auditor);
        ImageView take_pic1 = findViewById(R.id.take_pic1);
        prefernces = new AppPrefernces(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        name.setText(prefernces.getName());
        userName.setText(prefernces.getUserName());
        passWord.setText(prefernces.getPaso());
        if (prefernces.getProfileImage().equals(" ")) {
            Log.e("TAG", "NoProfilePic");
        } else {
            imageView.setImageBitmap(decodeBase64(prefernces.getProfileImage()));
        }

        take_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            photo = (Bitmap) data.getExtras().get("data");
            OutImage = Bitmap.createScaledBitmap(photo, 300, 400, true);
            imageView.setImageBitmap(OutImage);
            prefernces.setProfileImage(encodeTobase64(OutImage));
            Intent intent2 = new Intent("header_pic_update");
            LocalBroadcastManager.getInstance(Profile_Class.this).sendBroadcast(intent2);
        }
    }

    // method for bitmap to base64
    public static String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    // method for base64 to bitmap
    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}