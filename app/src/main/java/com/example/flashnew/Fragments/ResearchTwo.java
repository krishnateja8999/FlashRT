package com.example.flashnew.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
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
import android.widget.Toast;

import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.R;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.flashnew.Server.Utils.REQUEST_IMAGE_CAPTURE;

public class ResearchTwo extends Fragment implements BlockingStep {

    private RadioGroup researchRGroup1, researchRGroup2, researchRGroup3, researchRGroup4, researchRGroup5, researchRGroup6,
            researchRGroup7, researchRGroup8, researchRGroup9, researchRGroup10, researchRGroup11, researchRGroup12, researchRGroup13,
            researchRGroup14, researchRGroup15, researchRGroup16, researchRGroup17, researchRGroup18, researchRGroup19;
    private EditText vigilantes, observacao, comments;
    private TextView photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9, photo10, photo11, photo12, photo13;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11, image12, image13;
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
        researchRGroup7 = v.findViewById(R.id.researchRGroup7);
        researchRGroup8 = v.findViewById(R.id.researchRGroup8);
        researchRGroup9 = v.findViewById(R.id.researchRGroup9);
        researchRGroup10 = v.findViewById(R.id.researchRGroup10);
        researchRGroup11 = v.findViewById(R.id.researchRGroup11);
        researchRGroup12 = v.findViewById(R.id.researchRGroup12);
        researchRGroup13 = v.findViewById(R.id.researchRGroup13);
        researchRGroup14 = v.findViewById(R.id.researchRGroup14);
        researchRGroup15 = v.findViewById(R.id.researchRGroup15);
        researchRGroup16 = v.findViewById(R.id.researchRGroup16);
        researchRGroup17 = v.findViewById(R.id.researchRGroup17);
        researchRGroup18 = v.findViewById(R.id.researchRGroup18);
        researchRGroup19 = v.findViewById(R.id.researchRGroup19);
        vigilantes = v.findViewById(R.id.vigilantes);
        observacao = v.findViewById(R.id.observacao);
        comments = v.findViewById(R.id.comments);

        //camera
        photo1 = v.findViewById(R.id.photo1);
        photo2 = v.findViewById(R.id.photo2);
        photo3 = v.findViewById(R.id.photo3);
        photo4 = v.findViewById(R.id.photo4);
        photo5 = v.findViewById(R.id.photo5);
        photo6 = v.findViewById(R.id.photo6);
        photo7 = v.findViewById(R.id.photo7);
        photo8 = v.findViewById(R.id.photo8);
        photo9 = v.findViewById(R.id.photo9);
        photo10 = v.findViewById(R.id.photo10);
        photo11 = v.findViewById(R.id.photo11);
        photo12 = v.findViewById(R.id.photo12);
        photo13 = v.findViewById(R.id.photo13);

        //tick
        image1 = v.findViewById(R.id.image1);
        image2 = v.findViewById(R.id.image2);
        image3 = v.findViewById(R.id.image3);
        image4 = v.findViewById(R.id.image4);
        image5 = v.findViewById(R.id.image5);
        image6 = v.findViewById(R.id.image6);
        image7 = v.findViewById(R.id.image7);
        image8 = v.findViewById(R.id.image8);
        image9 = v.findViewById(R.id.image9);
        image10 = v.findViewById(R.id.image10);
        image11 = v.findViewById(R.id.image11);
        image12 = v.findViewById(R.id.image12);
        image13 = v.findViewById(R.id.image13);

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

        photo6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(6);
            }
        });

        photo7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(7);
            }
        });

        photo8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(8);
            }
        });

        photo9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(9);
            }
        });

        photo10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(10);
            }
        });

        photo11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(11);
            }
        });

        photo12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(12);
            }
        });

        photo13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(13);
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
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
        }
    }

    private void Validate(StepperLayout.OnNextClickedCallback callback) {
        callback.goToNextStep();
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

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        Validate(callback);
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

}