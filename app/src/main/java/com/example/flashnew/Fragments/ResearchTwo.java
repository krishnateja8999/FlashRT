package com.example.flashnew.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.flashnew.Server.Utils.REQUEST_IMAGE_CAPTURE;

public class ResearchTwo extends Fragment implements Step {

    private RadioGroup researchRGroup1, researchRGroup2, researchRGroup3, researchRGroup4, researchRGroup5, researchRGroup6;
    private EditText vigilantes, observacao, comments;
    private TextView photo1, photo2, photo3, photo4, photo5;
    private ImageView image1, image2, image3, image4, image5;
    private File photoFile = null;
    private String currentPhotoPath;
    private Landing_Screen context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_research_two, container, false);

        researchRGroup1 = v.findViewById(R.id.researchRGroup1);
        researchRGroup2 = v.findViewById(R.id.researchRGroup2);
        researchRGroup3 = v.findViewById(R.id.researchRGroup3);
        researchRGroup4 = v.findViewById(R.id.researchRGroup4);
        researchRGroup5 = v.findViewById(R.id.researchRGroup5);
        researchRGroup6 = v.findViewById(R.id.researchRGroup6);
        vigilantes = v.findViewById(R.id.vigilantes);
        observacao = v.findViewById(R.id.observacao);
        comments = v.findViewById(R.id.comments);

        //camera
        photo1 = v.findViewById(R.id.photo1);
        photo2 = v.findViewById(R.id.photo2);
        photo3 = v.findViewById(R.id.photo3);
        photo4 = v.findViewById(R.id.photo4);
        photo5 = v.findViewById(R.id.photo5);

        //tick
        image1 = v.findViewById(R.id.image1);
        image2 = v.findViewById(R.id.image2);
        image3 = v.findViewById(R.id.image3);
        image4 = v.findViewById(R.id.image4);
        image5 = v.findViewById(R.id.image5);

        photo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(1);
            }
        });

        photo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(2);
            }
        });

        photo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(3);
            }
        });

        photo4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(4);
            }
        });

        photo5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(5);
            }
        });

        return v;
    }

    private void dispatchTakePictureIntent(int resultCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(context, context.getPackageName(), photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, resultCode);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        String fileName = "temp";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(fileName, ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        String a = image.getName();

        Log.d("TAG", "createImageFileCollect: " + currentPhotoPath);
        Log.d("TAG", "createImageFileCollect: " + a);
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                }
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Landing_Screen) context;
    }
}