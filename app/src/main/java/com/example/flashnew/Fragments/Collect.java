package com.example.flashnew.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.google.zxing.*;

import net.skoumal.fragmentback.BackFragment;

import javax.xml.transform.Result;

import static android.app.Activity.RESULT_OK;

public class Collect extends Fragment implements BackFragment {
    private final int permsRequestCode = 200;
    private boolean mCameraPermission = false;
    private String profile_image = "Not changed";
    private boolean mReadImagesFromStorage = false;
    public static final int REQUEST_CODE_GALLERY = 0x1;

    private boolean mAccessImagesFromLocalStorage = false;
    public static final int REQUEST_CODE_CROP_IMAGE = 0x3;
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
    CircularImageView iv_profile_auditor;
    ProgressBar progressBar, progressBar1;
    String token;
    String TAG = "Something";
    private final String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    TextView title, imei;
    Button button, start, start1;
    LinearLayout bl, bl1;
    RelativeLayout rl;
    Button conf, can;
    Spinner spinner;
    private static final int CAMERA_REQUEST = 1888;
    AppPrefernces appPrefernces;
    ImageView imageView, imageView1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.collect, container, false);
        conf = view.findViewById(R.id.buttonConfirm);
        can = view.findViewById(R.id.buttonCancel);
        title = view.findViewById(R.id.actionbarTitle);
        appPrefernces = new AppPrefernces(getContext());
        spinner = view.findViewById(R.id.targetOptions);
        imageView = view.findViewById(R.id.img);
        imageView1 = view.findViewById(R.id.img1);
        imei = view.findViewById(R.id.actionbarImei);
        button = view.findViewById(R.id.coletaScan);
        bl = view.findViewById(R.id.bl);
        bl1 = view.findViewById(R.id.buttonLayout);
        rl = view.findViewById(R.id.rl);
        start = view.findViewById(R.id.start);
        start1 = view.findViewById(R.id.start1);
        title.setText("Coletas");
        imei.setText("IMEI : 9876543210123");
        String[] values1 =
                {"Selecione Coletar", "Recolhidas", "Não coletado"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values1);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCameraToTakePic();
            }
        });
        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (appPrefernces.getLanding().equals("Raghu")) {
                    imageView.setImageResource(R.drawable.ic_right);
                    start.setVisibility(View.GONE);
                    appPrefernces.setLanding("N/A");
                } else {
                    imageView1.setImageResource(R.drawable.ic_right);
                    start1.setVisibility(View.GONE);
                }
                bl.setVisibility(View.VISIBLE);
                bl1.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);


            }
        });
        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bl.setVisibility(View.VISIBLE);
                bl1.setVisibility(View.VISIBLE);
                rl.setVisibility(View.GONE);
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appPrefernces.setLanding("Raghu");
                bl.setVisibility(View.GONE);
                bl1.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
            }
        });
        start1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bl.setVisibility(View.GONE);
                bl1.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == CAMERA_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // imageView.setImageBitmap(photo);]
        }*/
        Log.d("jaya", "activity result");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String content = data.getStringExtra("SCAN_RESULT");
                Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openCameraToTakePic() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), permissions[1]) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getContext(), permissions[2]) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(permissions, permsRequestCode);
            else {
                if ((ContextCompat.checkSelfPermission(getContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED)) {
                    try {
                        Log.d("jaya", "opencamera1");
                        Intent cameraIntent = new Intent("com.google.zxing.client.android.SCAN");
                        cameraIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                        startActivityForResult(cameraIntent, 0);
                    } catch (Exception e) {

                    }
                } else
                    requestPermissions(permissions, permsRequestCode);
            }
        } else {
            mCameraPermission = true;
            mAccessImagesFromLocalStorage = true;
            mReadImagesFromStorage = true;
            Log.d("jaya", "opencamera");
            try {
                Intent cameraIntent = new Intent("com.google.zxing.client.android.SCAN");
                cameraIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(cameraIntent, 0);
            } catch (Exception e) {

            }
        }
    }


    @Override
    public boolean onBackPressed() {
        super.getActivity().onBackPressed();

        Intent intent = new Intent(getContext(), Collect.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(R.animator.left_to_right, R.animator.right_to_left);
        startActivity(intent);

        return true;
    }

    @Override
    public int getBackPriority() {
        return NORMAL_BACK_PRIORITY;
    }
}
