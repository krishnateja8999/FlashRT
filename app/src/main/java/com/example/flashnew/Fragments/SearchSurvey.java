package com.example.flashnew.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.flashnew.Activities.Landing_Screen;
import com.example.flashnew.Adapters.MyStepperAdapter;
import com.example.flashnew.HelperClasses.AppPrefernces;
import com.example.flashnew.HelperClasses.DatabaseHelper;
import com.example.flashnew.HelperClasses.UploadImages;
import com.example.flashnew.Modals.SaveResearchDetailsModal;
import com.example.flashnew.R;
import com.example.flashnew.Server.ApiUtils;
import com.example.flashnew.Server.InternetConnectionChecker;
import com.example.flashnew.Server.Utils;
import com.google.cloud.storage.Storage;
import com.stepstone.stepper.StepperLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BATTERY_SERVICE;
import static com.example.flashnew.Server.Utils.REQUEST_IMAGE_CAPTURE;
import static com.example.flashnew.Server.Utils.VERSION;


public class SearchSurvey extends Fragment {

    private static final String TAG = "TAG";
    private CardView client_local, client_alberto, lastCardView;
    private Button button_abrir, confirm_enviar, takePic;
    private RadioButton radio1, radio2, radio3, radio4;
    private RadioGroup radioGroup, radioGroup1;
    private Spinner spi, spi2;
    private String[] values1, enderec, ausente, nao_visitado, outros;
    private String researchName;
    private Landing_Screen context;
    private TextView researchHawb;
    private StepperLayout mStepperLayout;
    private LinearLayout bl1, actions_lay1;
    private MyStepperAdapter mStepperAdapter;
    private File photoFile = null;
    private String currentPhotoPath;
    private DatabaseHelper mDatabaseHelper;
    private String contractCode, customerID, clientName, resListcode;
    private String timeStamp;
    private AppPrefernces prefernces;
    private InternetConnectionChecker checker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_search_survey, container, false);

        client_local = view.findViewById(R.id.client_local);
        client_alberto = view.findViewById(R.id.client_alberto);
        lastCardView = view.findViewById(R.id.lastCardView);
        button_abrir = view.findViewById(R.id.button_abrir);
        confirm_enviar = view.findViewById(R.id.confirm_enviar);
        takePic = view.findViewById(R.id.takePic);
        radio1 = view.findViewById(R.id.radio1);
        radio2 = view.findViewById(R.id.radio2);
        radio3 = view.findViewById(R.id.radio3);
        radio4 = view.findViewById(R.id.radio4);
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup1 = view.findViewById(R.id.radioGroup1);
        spi = view.findViewById(R.id.spi);
        spi2 = view.findViewById(R.id.spi2);
        bl1 = view.findViewById(R.id.bl1);
        actions_lay1 = view.findViewById(R.id.actions_lay1);
        mDatabaseHelper = new DatabaseHelper(context);
        prefernces = new AppPrefernces(context);
        checker = new InternetConnectionChecker(context);
        assert getArguments() != null;
        researchName = getArguments().getString("Research");
        contractCode = getArguments().getString("Contract_code");
        customerID = getArguments().getString("Customer_code");
        clientName = getArguments().getString("Client_name");
        resListcode = getArguments().getString("List_code");
        prefernces.setCustomerCode(customerID);
        prefernces.setContractCode(contractCode);
        prefernces.setHawbCodeRes(researchName);
        prefernces.setClientName(clientName);
        prefernces.setResearchListCode(resListcode);
        Log.e(TAG, "Tags: " + researchName);
        Log.e(TAG, "Tags: " + contractCode);
        Log.e(TAG, "Tags: " + customerID);
        Log.e(TAG, "Tags: " + clientName);
        Log.e(TAG, "Tags: " + resListcode);
        timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        researchHawb = view.findViewById(R.id.researchHawb);
        researchHawb.setText("Pesquisa: " + researchName);
        mStepperLayout = (StepperLayout) view.findViewById(R.id.stepperLayout);
        mStepperAdapter = new MyStepperAdapter(getChildFragmentManager(), context);
        mStepperLayout.setAdapter(mStepperAdapter);
        values1 = getResources().getStringArray(R.array.motivo_grupo);
        enderec = getResources().getStringArray(R.array.motivo_dev);
        ausente = getResources().getStringArray(R.array.motivo_ausente);
        nao_visitado = getResources().getStringArray(R.array.motivo_nao_visitado);
        outros = getResources().getStringArray(R.array.motivo_outros);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, values1);
        adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spi.setAdapter(adapter1);
        spi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spi2.setVisibility(View.GONE);
                } else if (position == 1) {
                    spi2.setVisibility(View.VISIBLE);
                    spi2.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, enderec);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spi2.setAdapter(adapter2);
                } else if (position == 2) {
                    spi2.setVisibility(View.VISIBLE);
                    spi2.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, ausente);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spi2.setAdapter(adapter2);
                } else if (position == 3) {
                    spi2.setVisibility(View.VISIBLE);
                    spi2.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, nao_visitado);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spi2.setAdapter(adapter2);
                } else if (position == 4) {
                    spi2.setVisibility(View.VISIBLE);
                    spi2.performClick();
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, outros);
                    adapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spi2.setAdapter(adapter2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //This overrides the radiogroup onCheckListener
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    if (checkedRadioButton.getText().equals("Sim")) {
                        client_alberto.setVisibility(View.VISIBLE);
                        lastCardView.setVisibility(View.GONE);
                        confirm_enviar.setVisibility(View.GONE);
                        radio3.setChecked(false);
                        radio4.setChecked(false);
                    } else {
                        lastCardView.setVisibility(View.VISIBLE);
                        confirm_enviar.setVisibility(View.VISIBLE);
                        client_alberto.setVisibility(View.GONE);
                        button_abrir.setVisibility(View.GONE);
                    }
                }
            }
        });

        //This overrides the radiogroup onCheckListener
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    if (checkedRadioButton.getText().equals("Sim")) {
                        button_abrir.setVisibility(View.VISIBLE);
                        confirm_enviar.setVisibility(View.GONE);

                    } else {
                        confirm_enviar.setVisibility(View.VISIBLE);
                        button_abrir.setVisibility(View.GONE);
                    }
                }
            }
        });

        button_abrir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bl1.setVisibility(View.GONE);
                actions_lay1.setVisibility(View.GONE);
                mStepperLayout.setVisibility(View.VISIBLE);
//                Fragment fr = new StepperTabs();
//                FragmentTransaction fragmentTransaction = ((AppCompatActivity) context)
//                        .getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.content, fr);
//                fragmentTransaction.commit();
            }
        });

        confirm_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinalDialog("Confirme as respostas", "Deseja finalizar?");
            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (Landing_Screen) context;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            photoFile = createImageFile();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(context, context.getPackageName(), photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mDatabaseHelper.AddResearchImages("/null", "sdf");
            String imageName = prefernces.getCustomerCode() + "_" + prefernces.getContractCode() + "_img_ft_especial_" + prefernces.getHawbCodeRes() + "_img_rt_" + prefernces.getClientName() + "_" + timeStamp + ".png";
            mDatabaseHelper.AddResearchImages(currentPhotoPath, imageName);
            Log.e(TAG, "SearchSurveyImagePath: " + currentPhotoPath + " ImageName" + imageName);
            takePic.setText("Tirar fot do local" + "   ✔");
        }
    }

    private void StoreResearchDetails() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        BatteryManager bm = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        String body = SetXml();

        SaveResearchDetailsModal detailsModal = new SaveResearchDetailsModal(researchName, formattedDate, batLevel, prefernces.getLatitude(),
                prefernces.getLongitude(), body, prefernces.getResearchListCode());
        boolean success = mDatabaseHelper.AddResearchDetails(detailsModal);
        System.out.println(success);
    }

    private String SetXml() {

        String One = "[{\"label\": \"BASE/FILIAL (SIGLA)\", \"required\":1, \"type\": \"input-name\", \"id\": \"input-177\", \"value\": []},\n" +
                "{\"label\":\"DATE DE VERIFICAÇÃO\",\"required\":1,\"type\":\"input-date\",\"id\":\"input-359\",\"value\":[]},\n" +
                "{\"label\":\"EMPRESA\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-502\",\"value\":[]},\n" +
                "{\"label\":\"CNPJ\",\"required\":1,\"type\":\"input-number\",\"id\":\"input-537\",\"value\":[]},\n" +
                "{\"label\":\"RESPONSÁVEL PELA BASE/FILIAL\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-512\",\"value\":[]},\n" +
                "{\"label\":\"ENDEREÇO\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-901\",\"value\":[]},\n" +
                "{\"label\":\"BAIRRO\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-442\",\"value\":[]},\n" +
                "{\"label\":\"CIDADE\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-33\",\"value\":[]},\n" +
                "{\"label\":\"UF\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-237\",\"value\":[]},\n" +
                "{\"label\":\"CEP\",\"required\":1,\"type\":\"input-number\",\"id\":\"input-22\",\"value\":[]},\n" +
                "{\"label\":\"TELEFONE\",\"required\":1,\"type\":\"input-number\",\"id\":\"input-401\",\"value\":[]},\n" +
                "{\"label\":\"E-MAIL\",\"required\":1,\"type\":\"input-email\",\"id\":\"input-335\",\"value\":[]}]";
        String arrayOne = "\"Survey\"" + ":" + One;

        String Two = "[{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"TODAS AS ENTRADAS, SAÍDAS, AMBIENTES DE MANUSEIO E CUSTÓDIA SÃO MONITORADOS PERMANENTEMENTE?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"input-881\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE UMA ENTRADA, SAÍDA, AMBIENTE DE MANUSEIO OU CUSTÓDIA COM MONITORAMENTO\",\"required\":1,\"type\":\"photo\",\"id\":\"radiogroup-786\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"POSSUI SISTEMA DE CFTV COM GRAVAÇÃO DE IMAGENS?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-772\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE O SETOR DE MONITORAMENTO DAS IMAGENS\",\"required\":1,\"type\":\"photo\",\"id\":\"input-334\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"POSSUI VIGILÂNCIA?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-547\",\"value\":[]},\n" +
                "{\"label\":\"QUANTIDADE DE VIGILANTES\",\"required\":false,\"type\":\"input-number\",\"id\":\"input-278\",\"value\":[]},\n" +
                "{\"label\":\"POSTOS DE OBSERVAÇÃO\",\"required\":false,\"type\":\"input-number\",\"id\":\"input-999\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE O ESQUEMA DE VIGILÂNCIA EM FUNCIONAMENTO\",\"required\":0,\"type\":\"photo\",\"id\":\"radiogroup-768\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"EXISTE HISTÓRICO DE SINISTRO NOS ÚLTIMOS 24 MESES? SE SIM, DETALHAR E INFORMAR PROVIDÊNCIAS TOMADAS.\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-911\",\"value\":[]},\n" +
                "{\"label\":\"PROVIDÊNCIAS TOMADAS\",\"required\":false,\"type\":\"textarea\",\"id\":\"input-89\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"AS IMAGENS GRAVADAS FICAM NO MESMO PRÉDIO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-309\",\"value\":[]},\n" +
                "{\"label\":\"SE NÃO, ONDE?\",\"required\":false,\"type\":\"textarea\",\"id\":\"input-377\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"POSSUI SISTEMA DE ALARME?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-744\",\"value\":[]},\n" +
                "{\"label\":\"TIPO DO SISTEMA DE ALARME (SE HOUVER)\",\"required\":false,\"type\":\"input\",\"id\":\"input-919\",\"value\":[]},\n" +
                "{\"label\":\"LOCAIS COBERTOS PELO SISTEMA DE ALARME (SE HOUVER)\",\"required\":false,\"type\":\"textarea\",\"id\":\"input-50\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE O SISTEMA DE ALARME (SE HOUVER)\",\"required\":false,\"type\":\"photo\",\"id\":\"textarea-695\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ CERCAS ELÉTRICAS NO PERÍMETRO DA EMPRESA?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-744\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE A CERCA ELÉTRICAS (SE HOUVER)\",\"required\":0,\"type\":\"photo\",\"id\":\"radiogroup-10\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"ALGUM TELHADO OU ENTRADA ABERTOS?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-355\",\"value\":[]},\n" +
                "{\"label\":\"SE SIM, COMO CONTROLA?\",\"required\":false,\"type\":\"textarea\",\"id\":\"input-928\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE O TELHADO OU ÁREA ABERTA (SE HOUVER)\",\"required\":false,\"type\":\"photo\",\"id\":\"textarea-82\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ ALGUM DUTO DE VENTILAÇÃO ABERTO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-121\",\"value\":[]},\n" +
                "{\"label\":\"SE SIM, COMO CONTROLA?\",\"required\":false,\"type\":\"textarea\",\"id\":\"textarea-189\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE O DUTO DE VENTILAÇÃO ABERTO (SE HOUVER)\",\"required\":false,\"type\":\"photo\",\"id\":\"textarea-452\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ ILUMINAÇÃO ADEQUADA EM TODO O PERÍMETRO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-916\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE UM LOCAL COM ILUMINAÇÃO ADEQUADA\",\"required\":1,\"type\":\"photo\",\"id\":\"radiogroup-223\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ SISTEMA DE ILUMINAÇÃO DE EMERGÊNCIA?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-559\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE O SISTEMA DE ILUMINAÇÃO DE EMERGÊNCIA (SE HOUVER)\",\"required\":0,\"type\":\"photo\",\"id\":\"radiogroup-696\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ SISTEMA DE CONTROLE DE ENTRADA E SAÍDA DE FUNCIONÁRIOS E VISITANTES?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-817\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE O SISTEMA DE CONTROLE DE ENTRADA E SAÍDA DE FUNCIONÁRIOS E VISITANTES (SE HOUVER)\",\"required\":0,\"type\":\"photo\",\"id\":\"radiogroup-432\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"TODO PERÍMETRO DA EMPRESA ESTÁ COBERTO POR CÂMERAS, INCLUSIVE O AMBIENTE DO COFRE?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-410\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFAR ALGUMA ÁREA COM VIGILÂNCIA POR CÂMERA\",\"required\":1,\"type\":\"photo\",\"id\":\"radiogroup-329\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ CONTROLE DE ENTRADA E SAÍDA DE PACOTES\\/ENTREGAS?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-413\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"HÁ CONTROLE PARA PREVENIR QUE VISITANTES ANDEM LIVREMENTE POR TODO O PERÍMETRO DA EMPRESA?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-990\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"SÃO EMITIDOS CRACHÁS OU IDENTIFICAÇÕES ESPECIAIS PARA VISITANTES?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-841\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"OS AMBIENTES MANUSEIO E COFRE SÃO DEVIDAMENTE SEGREGADOS E POSSUEM CONTROLE DE ACESSO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-430\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE A SEPARAÇÃO ENTRE OS AMBIENTES DE MANUSEIO E COFRE\",\"required\":1,\"type\":\"photo\",\"id\":\"radiogroup-159\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"OS FUNCIONÁRIOS CIRCULAM LIVREMENTE ENTRE OS AMBIENTES?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-330\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"A(S) ENTRADA(S) E SAÍDA(S) DE VEÍCULOS POSSUI PORTÕES COM SISTEMA DE ECLUSA\\/ENCLAUSURAMENTO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-771\",\"value\":[]},\n" +
                "{\"label\":\"FOTOGRAFE A ENTRADA\\/SAÍDA DE  VEÍCULOS\",\"required\":1,\"type\":\"photo\",\"id\":\"radiogroup-674\",\"value\":[]},\n" +
                "{\"options\":[\"SIM\",\"NÃO\"],\"label\":\"EXISTE UM PCN (PLANO DE CONTINUIDADE DE NEGÓCIO) APROVADO E TESTADO?\",\"required\":1,\"type\":\"radiogroup\",\"id\":\"radiogroup-302\",\"value\":[]},\n" +
                "{\"label\":\"SE SIM, ENCAMINHAR\\/ANEXAR DOCUMENTO COMPROBATÓRIO\\/ TESTES\",\"required\":false,\"type\":\"photo\",\"id\":\"input-45\",\"value\":[]},\n" +
                "{\"label\":\"COMENTÁRIOS\\/OBSERVAÇÕES\",\"required\":false,\"type\":\"textarea\",\"id\":\"input-601\",\"value\":[]}]";
        String arrayTwo = "\"Survey\"" + ":" + Two;

        String Three = "[{\"label\":\"RESPONSÁVEL PELAS INFORMAÇÕES DESTA PESQUISA\",\"required\":1,\"type\":\"input-name\",\"id\":\"input-292\",\"value\":[]},\n" +
                "{\"label\":\"AS INFORMAÇÕES NESTA PESQUISA FORAM PRESTADAS COM VERACIDADE E ASSUMO INTEGRAL RESPONSABILIDADE PELAS MESMAS. DECLARO ESTAR CIENTE DE QUE A FALSIDADE NAS INFORMAÇÕES IMPLICARÁ NAS PENALIDADES CABÍVEIS. COMPROMETO-ME A COMUNICAR Á TICKET QUAISQUER ALTERAÇÕES NESTAS INFORMAÇÕES NUM PRAZO MÁXIMO DE 07 DIAS.\",\"required\":1,\"type\":\"text\",\"id\":\"input-461\",\"value\":[]},\n" +
                "{\"label\":\"ASSINATURA DO GERENTE\\/DIRETOR RESPONSÁVEL\",\"required\":1,\"type\":\"signature\",\"id\":\"input-735\",\"value\":[]}]";
        String arrayThree = "\"Survey\"" + ":" + Three;

        String tab1 = " \"tab\": \"tabStrip\",\n" +
                "                \"label\": \"IDENTIFICAÇÃO\",\n" +
                "                \"type\": \"fieldset\",\n" +
                "                \"id\": \"fieldset-627\",\n" +
                "                \"value\": []";

        String tab2 = "\"tab\": \"tabStrip\",\n" +
                "                \"label\": \"CHECK LIST\",\n" +
                "                \"type\": \"fieldset\",\n" +
                "                \"id\": \"fieldset-797\",\n" +
                "                \"value\": []";

        String tab3 = "\"tab\": \"tabStrip\",\n" +
                "                \"label\": \"RESPONDENTE\",\n" +
                "                \"type\": \"fieldset\",\n" +
                "                \"id\": \"fieldset-59\",\n" +
                "                \"value\": []";

        return "<![CDATA[[{" + arrayOne + ", \n" + tab1 + "},\n {\n" + arrayTwo + ", \n" + tab2 + "},\n {\n" + arrayThree + ", \n" + tab3 + "}]]]>";
    }

    private void PutResearchData() {
        Cursor data = mDatabaseHelper.GetResearchDetails(); //table11
        RequestQueue queue;
        queue = Volley.newRequestQueue(context);
        String url2 = prefernces.getHostUrl() + ApiUtils.GET_LIST1;

        ArrayList<String> codHawb = new ArrayList<String>();
        ArrayList<String> dataHoraBaixa = new ArrayList<String>();
        ArrayList<String> nivelBateria = new ArrayList<String>();
        ArrayList<String> latitude = new ArrayList<String>();
        ArrayList<String> longitude = new ArrayList<String>();
        ArrayList<String> body1 = new ArrayList<String>();
        ArrayList<String> listCode = new ArrayList<String>();

        if (data.getCount() == 0) {
            Log.e(TAG, "PutJsonRequest: No Data");
        } else {
            while (data.moveToNext()) {
                codHawb.add(data.getString(1));
                dataHoraBaixa.add(data.getString(2));
                nivelBateria.add(data.getString(3));
                latitude.add(data.getString(4));
                longitude.add(data.getString(5));
                body1.add(data.getString(6));
                listCode.add(data.getString(7));

                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObj = new JSONObject();
                JSONObject jsonObj1 = new JSONObject();
                try {
                    jsonObj.put("codHawb", Utils.ConvertArrayListToString(codHawb));
                    jsonObj.put("dataHoraBaixa", Utils.ConvertArrayListToString(dataHoraBaixa));
                    jsonObj.put("nivelBateria", Utils.ConvertArrayListToString(nivelBateria));
                    jsonObj.put("latitude", Utils.ConvertArrayListToString(latitude));
                    jsonObj.put("longitude", Utils.ConvertArrayListToString(longitude));
                    jsonObj.put("xmlPesquisa", Utils.ConvertArrayListToString(body1));

                    jsonObj1.put("imei", prefernces.getIMEI());
                    jsonObj1.put("franquia", prefernces.getFranchise());
                    jsonObj1.put("sistema", prefernces.getSystem());
                    jsonObj1.put("lista", Utils.ConvertArrayListToString(listCode));
                    jsonObj1.put("entregas", jsonArray);
                    jsonArray.put(jsonObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "PutJsonRequest: " + jsonObj1);
                Log.e(TAG, "PutResearchDataURLSearchList: " + url2 + Utils.ConvertArrayListToString(listCode));

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url2 + Utils.ConvertArrayListToString(listCode), jsonObj1, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DeleteDataUponSendingOrSync();
                        Intent intent22 = new Intent("research_list_update");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent22);
                        Log.e(TAG, "onResponseResearchThree: " + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponseResearchThree: " + error.getMessage());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> params = new HashMap<String, String>();
                        String auth1 = "Basic "
                                + Base64.encodeToString((prefernces.getUserName() + ":" + prefernces.getPaso()).getBytes(),
                                Base64.NO_WRAP);
                        params.put("Authorization", auth1);
                        params.put("x-versao-rt", VERSION);
                        params.put("x-rastreador", "ricardo");
                        params.put("Content-Type", "application/json; charset=utf-8");
                        return params;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                };
                queue.add(request);

                codHawb.clear();
                dataHoraBaixa.clear();
                nivelBateria.clear();
                latitude.clear();
                longitude.clear();
                body1.clear();
                listCode.clear();
            }
            SendResearchImages();
        }
    }

    private void SendResearchImages() {
        Cursor data = mDatabaseHelper.GetResearchImages(); //table12
        ArrayList<String> ImagePath = new ArrayList<String>();
        ArrayList<String> ImageName = new ArrayList<String>();
        if (data.getCount() == 0) {
            Log.e(TAG, "SendResearchImages: No Data");
        } else {
            while (data.moveToNext()) {
                ImagePath.add(data.getString(1));
                ImageName.add(data.getString(2));
                try {
                    Storage storage = UploadImages.setCredentials(context.getAssets().open("key.json"));

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            UploadImages.transmitImageFile(storage, Utils.ConvertArrayListToString(ImagePath), Utils.ConvertArrayListToString(ImageName));
                        }
                    });
                    thread.start();
                } catch (Exception e) {
                    Log.e(TAG, "SendResearchImagesException: " + e.getMessage());
                }
                ImagePath.clear();
                ImageName.clear();
            }
            mDatabaseHelper.DeleteResearchImages();
        }
    }

    private void DeleteDataUponSendingOrSync() {
        Cursor data = mDatabaseHelper.GetResearchDetails(); //table11
        ArrayList<String> list = new ArrayList<String>();
        if (data.getCount() == 0) {
            Log.e(TAG, "DeleteDataUponSyncOrUpload: No Data");
        } else {
            while (data.moveToNext()) {
                list.add(data.getString(1));
                mDatabaseHelper.DeleteDataFromResearchList(Utils.ConvertArrayListToString(list));
                list.clear();
            }
            mDatabaseHelper.DeleteFromResearchDetails();
        }
    }

    private void FinalDialog(String successDialog, String successDesc) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(successDialog);
        builder1.setMessage(successDesc);
        builder1.setCancelable(false);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FinalDialog2("Sucesso", "Pesquisa finalizada com successo");
                mDatabaseHelper.CheckTickMarkInResearchLists();
                StoreResearchDetails();
                if (checker.checkInternetConnection()) {
                    PutResearchData();
                }
            }
        });
        builder1.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Creating dialog box
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    private void FinalDialog2(String successDialog2, String successDesc2) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle(successDialog2);
        builder1.setMessage(successDesc2);
        builder1.setCancelable(false);
        builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                changeFragment(new Search());
                Intent intent22 = new Intent("research_list_update");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent22);
            }
        });
        //Creating dialog box
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    public void changeFragment(Fragment fragment) {
        context.getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

}