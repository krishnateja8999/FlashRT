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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.R;
import com.example.flashnew.Server.Utils;
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
            researchRGroup14, researchRGroup15, researchRGroup16, researchRGroup17, researchRGroup18, researchRGroup19, researchRGroup20;
    private RadioButton selectedRadioButton1, selectedRadioButton2, selectedRadioButton3, selectedRadioButton4, selectedRadioButton5, selectedRadioButton6,
            selectedRadioButton7, selectedRadioButton8, selectedRadioButton9, selectedRadioButton10, selectedRadioButton11, selectedRadioButton12,
            selectedRadioButton13, selectedRadioButton14, selectedRadioButton15, selectedRadioButton16, selectedRadioButton17, selectedRadioButton18, selectedRadioButton19, selectedRadioButton20;
    private EditText eText1, eText2, eText3, eText4, eText5, eText6, eText7, eText8, eText9;
    private TextView photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9, photo10, photo11, photo12, photo13, photo14;
    private ImageView image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11, image12, image13, image14;
    private String imagePath1 = "null", imagePath2 = "null", imagePath7 = "null", imagePath10 = "null", imagePath11 = "null", imagePath12 = "null";
    private String imagePath3, imagePath4, imagePath5, imagePath6, imagePath8, imagePath9, imagePath13, imagePath14;
    private File photoFile = null;
    private String currentPhotoPath;
    private Landing_Screen context;
    private AppPrefernces prefernces;
    public static final String TAG = "TAG";
    private DatabaseHelper mDatabaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_research_two, container, false);

        prefernces = new AppPrefernces(context);
        mDatabaseHelper = new DatabaseHelper(context);
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
        researchRGroup20 = v.findViewById(R.id.researchRGroup20);
        eText1 = v.findViewById(R.id.eText1);
        eText2 = v.findViewById(R.id.eText2);
        eText3 = v.findViewById(R.id.eText3);
        eText4 = v.findViewById(R.id.eText4);
        eText5 = v.findViewById(R.id.eText5);
        eText6 = v.findViewById(R.id.eText6);
        eText7 = v.findViewById(R.id.eText7);
        eText8 = v.findViewById(R.id.eText8);
        eText9 = v.findViewById(R.id.eText9);

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
        photo14 = v.findViewById(R.id.photo14);

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
        image14 = v.findViewById(R.id.image14);

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

        photo14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(14);
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
                    mDatabaseHelper.AddResearchImages("/null", "sdf");
                    imagePath1 = currentPhotoPath;
                    image1.setImageResource(R.drawable.ic_right);
                    //        <customer code>_<contract code>_<image type>*_<hawb>_img_rt_<customer number>**_<AAAAMMDDHHMMSS>.png
                    //        3586_4801_img_ar_02406021825_img_rt_OMtest001_20200111171205.png
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    imagePath2 = currentPhotoPath;
                    image2.setImageResource(R.drawable.ic_right);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    imagePath3 = currentPhotoPath;
                    image3.setImageResource(R.drawable.ic_right);
                }
                break;
            case 4:
                if (resultCode == RESULT_OK) {
                    imagePath4 = currentPhotoPath;
                    image4.setImageResource(R.drawable.ic_right);
                }
                break;
            case 5:
                if (resultCode == RESULT_OK) {
                    imagePath5 = currentPhotoPath;
                    image5.setImageResource(R.drawable.ic_right);
                }
                break;
            case 6:
                if (resultCode == RESULT_OK) {
                    imagePath6 = currentPhotoPath;
                    image6.setImageResource(R.drawable.ic_right);
                }
                break;
            case 7:
                if (resultCode == RESULT_OK) {
                    imagePath7 = currentPhotoPath;
                    image7.setImageResource(R.drawable.ic_right);
                }
                break;
            case 8:
                if (resultCode == RESULT_OK) {
                    imagePath8 = currentPhotoPath;
                    image8.setImageResource(R.drawable.ic_right);
                }
                break;
            case 9:
                if (resultCode == RESULT_OK) {
                    imagePath9 = currentPhotoPath;
                    image9.setImageResource(R.drawable.ic_right);
                }
                break;
            case 10:
                if (resultCode == RESULT_OK) {
                    imagePath10 = currentPhotoPath;
                    image10.setImageResource(R.drawable.ic_right);
                }
                break;
            case 11:
                if (resultCode == RESULT_OK) {
                    imagePath11 = currentPhotoPath;
                    image11.setImageResource(R.drawable.ic_right);
                }
                break;
            case 12:
                if (resultCode == RESULT_OK) {
                    imagePath12 = currentPhotoPath;
                    image12.setImageResource(R.drawable.ic_right);
                }
                break;
            case 13:
                if (resultCode == RESULT_OK) {
                    imagePath13 = currentPhotoPath;
                    image13.setImageResource(R.drawable.ic_right);
                }
                break;
            case 14:
                if (resultCode == RESULT_OK) {
                    imagePath14 = currentPhotoPath;
                    image14.setImageResource(R.drawable.ic_right);
                }
                break;
        }
    }

    private void Validate(StepperLayout.OnNextClickedCallback callback) {
        int selectedId1 = researchRGroup1.getCheckedRadioButtonId();
        int selectedId2 = researchRGroup2.getCheckedRadioButtonId();
        int selectedId3 = researchRGroup3.getCheckedRadioButtonId();
        int selectedId4 = researchRGroup4.getCheckedRadioButtonId();
        int selectedId5 = researchRGroup5.getCheckedRadioButtonId();
        int selectedId6 = researchRGroup6.getCheckedRadioButtonId();
        int selectedId7 = researchRGroup7.getCheckedRadioButtonId();
        int selectedId8 = researchRGroup8.getCheckedRadioButtonId();
        int selectedId9 = researchRGroup9.getCheckedRadioButtonId();
        int selectedId10 = researchRGroup10.getCheckedRadioButtonId();
        int selectedId11 = researchRGroup11.getCheckedRadioButtonId();
        int selectedId12 = researchRGroup12.getCheckedRadioButtonId();
        int selectedId13 = researchRGroup13.getCheckedRadioButtonId();
        int selectedId14 = researchRGroup14.getCheckedRadioButtonId();
        int selectedId15 = researchRGroup15.getCheckedRadioButtonId();
        int selectedId16 = researchRGroup16.getCheckedRadioButtonId();
        int selectedId17 = researchRGroup17.getCheckedRadioButtonId();
        int selectedId18 = researchRGroup18.getCheckedRadioButtonId();
        int selectedId19 = researchRGroup19.getCheckedRadioButtonId();
        int selectedId20 = researchRGroup20.getCheckedRadioButtonId();
        selectedRadioButton1 = (RadioButton) researchRGroup1.findViewById(selectedId1);
        selectedRadioButton2 = (RadioButton) researchRGroup2.findViewById(selectedId2);
        selectedRadioButton3 = (RadioButton) researchRGroup3.findViewById(selectedId3);
        selectedRadioButton4 = (RadioButton) researchRGroup4.findViewById(selectedId4);
        selectedRadioButton5 = (RadioButton) researchRGroup5.findViewById(selectedId5);
        selectedRadioButton6 = (RadioButton) researchRGroup6.findViewById(selectedId6);
        selectedRadioButton7 = (RadioButton) researchRGroup7.findViewById(selectedId7);
        selectedRadioButton8 = (RadioButton) researchRGroup8.findViewById(selectedId8);
        selectedRadioButton9 = (RadioButton) researchRGroup9.findViewById(selectedId9);
        selectedRadioButton10 = (RadioButton) researchRGroup10.findViewById(selectedId10);
        selectedRadioButton11 = (RadioButton) researchRGroup11.findViewById(selectedId11);
        selectedRadioButton12 = (RadioButton) researchRGroup12.findViewById(selectedId12);
        selectedRadioButton13 = (RadioButton) researchRGroup13.findViewById(selectedId13);
        selectedRadioButton14 = (RadioButton) researchRGroup14.findViewById(selectedId14);
        selectedRadioButton15 = (RadioButton) researchRGroup15.findViewById(selectedId15);
        selectedRadioButton16 = (RadioButton) researchRGroup16.findViewById(selectedId16);
        selectedRadioButton17 = (RadioButton) researchRGroup17.findViewById(selectedId17);
        selectedRadioButton18 = (RadioButton) researchRGroup18.findViewById(selectedId18);
        selectedRadioButton19 = (RadioButton) researchRGroup19.findViewById(selectedId19);
        selectedRadioButton20 = (RadioButton) researchRGroup20.findViewById(selectedId20);
        //Log.e("TAG", "Validate: " + selectedRadioButton1.getText().toString());

        String s = "Por favor selecione: ";
        String s1 = "OK";
        String s2 = "Selecione e preencha todos os campos obrigatórios.";

        if (researchRGroup1.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list1), s1);
        } else if (imagePath1.equals("null")) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list2), s1);
        } else if (researchRGroup2.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list3), s1);
        } else if (imagePath2.equals("null")) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list4), s1);
        } else if (researchRGroup3.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list5), s1);
        } else if (researchRGroup4.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list15), s1);
        } else if (researchRGroup5.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list17), s1);
        } else if (researchRGroup6.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list19), s1);
        } else if (researchRGroup7.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list23), s1);
        } else if (researchRGroup8.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list25), s1);
        } else if (researchRGroup20.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, "HÁ ALGUM DUTO DE VENTILAÇÃO ABERTO? *", s1);
        } else if (researchRGroup9.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list28), s1);
        } else if (imagePath7.equals("null")) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list29), s1);
        } else if (researchRGroup10.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list30), s1);
        } else if (researchRGroup11.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list32), s1);
        } else if (researchRGroup12.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list34), s1);
        } else if (imagePath10.equals("null")) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list35), s1);
        } else if (researchRGroup13.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list36), s1);
        } else if (researchRGroup14.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list37), s1);
        } else if (researchRGroup15.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list38), s1);
        } else if (researchRGroup16.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list39), s1);
        } else if (imagePath11.equals("null")) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list40), s1);
        } else if (researchRGroup17.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list9), s1);
        } else if (researchRGroup18.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list10), s1);
        } else if (imagePath12.equals("null")) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list11), s1);
        } else if (researchRGroup19.getCheckedRadioButtonId() == -1) {
            Utils.DialogClass(context, s, getResources().getString(R.string.check_list12), s1);
        } else {
            JsonObjectCheckList(selectedRadioButton1.getText().toString(), imagePath1, selectedRadioButton2.getText().toString(), imagePath2, selectedRadioButton3.getText().toString(),
                    eText1.getText().toString(), eText2.getText().toString(), imagePath3, selectedRadioButton4.getText().toString(), eText3.getText().toString(), selectedRadioButton5.getText().toString(),
                    eText4.getText().toString(), selectedRadioButton6.getText().toString(), eText5.getText().toString(), eText6.getText().toString(), imagePath4, selectedRadioButton7.getText().toString(),
                    imagePath5, selectedRadioButton8.getText().toString(), eText7.getText().toString(), imagePath14, selectedRadioButton20.getText().toString(), eText8.getText().toString(), imagePath6,
                    selectedRadioButton9.getText().toString(), imagePath7, selectedRadioButton10.getText().toString(), imagePath8, selectedRadioButton11.getText().toString(), imagePath9, selectedRadioButton12.getText().toString(),
                    imagePath10, selectedRadioButton13.getText().toString(), selectedRadioButton14.getText().toString(), selectedRadioButton15.getText().toString(), selectedRadioButton16.getText().toString(),
                    imagePath11, selectedRadioButton17.getText().toString(), selectedRadioButton18.getText().toString(), imagePath12, selectedRadioButton19.getText().toString(), imagePath13, eText9.getText().toString());

            callback.goToNextStep();
        }
    }

    private void JsonObjectCheckList(String radio1, String photo1path, String radio2, String photo2path, String radio3, String text1, String text2, String photo3path, String radio4, String text3, String radio5, String text4, String radio6, String text5,
                                     String text6, String photo4path, String radio7, String photo5path, String radio8, String text7, String text8, String radio9, String text9, String photo6path, String radio10, String photo7path,
                                     String radio11, String photo8path, String radio12, String photo9path, String radio13, String photo10path, String radio14, String radio15, String radio16, String radio17, String photo11path, String radio18, String radio19, String photo12path, String radio20, String photo13path, String text10) {
        String object2 = "[{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"TODAS AS ENTRADAS, SAÍDAS, AMBIENTES DE MANUSEIO E CUSTÓDIA SÃO MONITORADOS PERMANENTEMENTE?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"input-881\",\"value\":[\"" + radio1 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE UMA ENTRADA, SAÍDA, AMBIENTE DE MANUSEIO OU CUSTÓDIA COM MONITORAMENTO\",\"required\":1,\"type\":\"photo\",\"id\":\"radiogroup-786\",\"value\":[\"" + photo1path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"POSSUI SISTEMA DE CFTV COM GRAVAÇÃO DE IMAGENS?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-772\",\"value\":[\"" + radio2 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE O SETOR DE MONITORAMENTO DAS IMAGENS\",\"required\":1,\"type\":\"photo\",\"id\":\"input-334\",\"value\":[\"" + photo2path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"POSSUI VIGILÂNCIA?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-547\",\"value\":[\"" + radio3 + "\"]},\n" +
                "{\"label\":\"QUANTIDADE DE VIGILANTES\",\"required\":false,\"type\":\"input-number\",\"id\":\"input-278\",\"value\":[\"" + text1 + "\"]},\n" +
                "{\"label\":\"POSTOS DE OBSERVAÇÃO\",\"required\":false,\"type\":\"input-number\",\"id\":\"input-999\",\"value\":[\"" + text2 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE O ESQUEMA DE VIGILÂNCIA EM FUNCIONAMENTO\",\"required\":0,\"type\":\"photo\",\"id\":\"radiogroup-768\",\"value\":[\"" + photo3path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"EXISTE HISTÓRICO DE SINISTRO NOS ÚLTIMOS 24 MESES? SE SIM, DETALHAR E INFORMAR PROVIDÊNCIAS TOMADAS.\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-911\",\"value\":[\"" + radio4 + "\"]},\n" +
                "{\"label\":\"PROVIDÊNCIAS TOMADAS\",\"required\":false,\"type\":\"textarea\",\"id\":\"input-89\",\"value\":[\"" + text3 + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"AS IMAGENS GRAVADAS FICAM NO MESMO PRÉDIO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-309\",\"value\":[\"" + radio5 + "\"]},\n" +
                "{\"label\":\"SE NÃO, ONDE?\",\"required\":false,\"type\":\"textarea\",\"id\":\"input-377\",\"value\":[\"" + text4 + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"POSSUI SISTEMA DE ALARME?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-744\",\"value\":[\"" + radio6 + "\"]},\n" +
                "{\"label\":\"TIPO DO SISTEMA DE ALARME (SE HOUVER)\",\"required\":false,\"type\":\"input\",\"id\":\"input-919\",\"value\":[\"" + text5 + "\"]},\n" +
                "{\"label\":\"LOCAIS COBERTOS PELO SISTEMA DE ALARME (SE HOUVER)\",\"required\":false,\"type\":\"textarea\",\"id\":\"input-50\",\"value\":[\"" + text6 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE O SISTEMA DE ALARME (SE HOUVER)\",\"required\":false,\"type\":\"photo\",\"id\":\"textarea-695\",\"value\":[\"" + photo4path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ CERCAS ELÉTRICAS NO PERÍMETRO DA EMPRESA?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-744\",\"value\":[\"" + radio7 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE A CERCA ELÉTRICAS (SE HOUVER)\",\"required\":0,\"type\":\"photo\",\"id\":\"radiogroup-10\",\"value\":[\"" + photo5path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"ALGUM TELHADO OU ENTRADA ABERTOS?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-355\",\"value\":[\"" + radio8 + "\"]},\n" +
                "{\"label\":\"SE SIM, COMO CONTROLA?\",\"required\":false,\"type\":\"textarea\",\"id\":\"input-928\",\"value\":[\"" + text7 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE O TELHADO OU ÁREA ABERTA (SE HOUVER)\",\"required\":false,\"type\":\"photo\",\"id\":\"textarea-82\",\"value\":[\"" + text8 + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ ALGUM DUTO DE VENTILAÇÃO ABERTO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-121\",\"value\":[\"" + radio9 + "\"]},\n" +
                "{\"label\":\"SE SIM, COMO CONTROLA?\",\"required\":false,\"type\":\"textarea\",\"id\":\"textarea-189\",\"value\":[\"" + text9 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE O DUTO DE VENTILAÇÃO ABERTO (SE HOUVER)\",\"required\":false,\"type\":\"photo\",\"id\":\"textarea-452\",\"value\":[\"" + photo6path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ ILUMINAÇÃO ADEQUADA EM TODO O PERÍMETRO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-916\",\"value\":[\"" + radio10 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE UM LOCAL COM ILUMINAÇÃO ADEQUADA\",\"required\":1,\"type\":\"photo\",\"id\":\"radiogroup-223\",\"value\":[\"" + photo7path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ SISTEMA DE ILUMINAÇÃO DE EMERGÊNCIA?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-559\",\"value\":[\"" + radio11 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE O SISTEMA DE ILUMINAÇÃO DE EMERGÊNCIA (SE HOUVER)\",\"required\":0,\"type\":\"photo\",\"id\":\"radiogroup-696\",\"value\":[\"" + photo8path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ SISTEMA DE CONTROLE DE ENTRADA E SAÍDA DE FUNCIONÁRIOS E VISITANTES?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-817\",\"value\":[\"" + radio12 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE O SISTEMA DE CONTROLE DE ENTRADA E SAÍDA DE FUNCIONÁRIOS E VISITANTES (SE HOUVER)\",\"required\":0,\"type\":\"photo\",\"id\":\"radiogroup-432\",\"value\":[\"" + photo9path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"TODO PERÍMETRO DA EMPRESA ESTÁ COBERTO POR CÂMERAS, INCLUSIVE O AMBIENTE DO COFRE?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-410\",\"value\":[\"" + radio13 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFAR ALGUMA ÁREA COM VIGILÂNCIA POR CÂMERA\",\"required\":1,\"type\":\"photo\",\"id\":\"radiogroup-329\",\"value\":[\"" + photo10path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ CONTROLE DE ENTRADA E SAÍDA DE PACOTES\\/ENTREGAS?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-413\",\"value\":[\"" + radio14 + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ CONTROLE PARA PREVENIR QUE VISITANTES ANDEM LIVREMENTE POR TODO O PERÍMETRO DA EMPRESA?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-990\",\"value\":[\"" + radio15 + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"SÃO EMITIDOS CRACHÁS OU IDENTIFICAÇÕES ESPECIAIS PARA VISITANTES?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-841\",\"value\":[\"" + radio16 + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"OS AMBIENTES MANUSEIO E COFRE SÃO DEVIDAMENTE SEGREGADOS E POSSUEM CONTROLE DE ACESSO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-430\",\"value\":[\"" + radio17 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE A SEPARAÇÃO ENTRE OS AMBIENTES DE MANUSEIO E COFRE\",\"required\":1,\"type\":\"photo\",\"id\":\"radiogroup-159\",\"value\":[\"" + photo11path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"OS FUNCIONÁRIOS CIRCULAM LIVREMENTE ENTRE OS AMBIENTES?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-330\",\"value\":[\"" + radio18 + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"A(S) ENTRADA(S) E SAÍDA(S) DE VEÍCULOS POSSUI PORTÕES COM SISTEMA DE ECLUSA\\/ENCLAUSURAMENTO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-771\",\"value\":[\"" + radio19 + "\"]},\n" +
                "{\"label\":\"FOTOGRAFE A ENTRADA\\/SAÍDA DE  VEÍCULOS\",\"required\":1,\"type\":\"photo\",\"id\":\"radiogroup-674\",\"value\":[\"" + photo12path + "\"]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"EXISTE UM PCN (PLANO DE CONTINUIDADE DE NEGÓCIO) APROVADO E TESTADO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-302\",\"value\":[\"" + radio20 + "\"]},\n" +
                "{\"label\":\"SE SIM, ENCAMINHAR\\/ANEXAR DOCUMENTO COMPROBATÓRIO\\/ TESTES\",\"required\":false,\"type\":\"photo\",\"id\":\"input-45\",\"value\":[\"" + photo13path + "\"]},\n" +
                "{\"label\":\"COMENTÁRIOS\\/OBSERVAÇÕES\",\"required\":false,\"type\":\"textarea\",\"id\":\"input-601\",\"value\":[\"" + text10 + "\"]}]";

        String array11 = "\"Survey\"" + ":" + object2;
        prefernces.setResearchTwoDetails(array11);


        Log.e("TAG", "JsonObjectIdentityStringArray: " + array11);


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
        //callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

}