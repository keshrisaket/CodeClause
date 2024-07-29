package com.example.real_timelanguagetranslationapp;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    EditText source_language;
    TextView destination_language;
    MaterialButton source_language_choose_btn;
    MaterialButton target_language_choose_btn;
    AppCompatButton translate;
    private String sourceLanguageText ;

    private ArrayList<Modellanguage> languageArrayList= null;


    // progressdialog to show while translation is in progress
    private ProgressDialog progressDialog;
    // translator object for configruning it with the source and destination language
    private Translator translator;
    // translatorOptions to set source and destination language eg english to hindi
    private TranslatorOptions translatorOptions;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        source_language=findViewById(R.id.source_language);
        destination_language=findViewById(R.id.destination_language);
        source_language_choose_btn=findViewById(R.id.source_language_choose_btn);
        target_language_choose_btn=findViewById(R.id.target_language_choose_btn);
        translate=findViewById(R.id.translate);

        loadAvailableLanguages();

        // init setup progress dialog
        progressDialog= new ProgressDialog(this);
        progressDialog.setTitle("Please Weight ");
        progressDialog.setCanceledOnTouchOutside(false);


        source_language_choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 sourceLanguageChoose();
            }
        });



        target_language_choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              target_language_choose();
            }
        });

        translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    validateData();
            }
        });

    }

    private void loadAvailableLanguages(){
        languageArrayList=new ArrayList<>();

        List<String > languageCodeList=TranslateLanguage.getAllLanguages();

        for ( String languageCode : languageCodeList) {
            String languageTitle = new Locale(languageCode).getDisplayLanguage();

            System.out.println(languageCode+" "+languageTitle);

            Modellanguage modellanguage=new Modellanguage(languageCode,languageTitle);
            languageArrayList.add(modellanguage);
        }
    }

    private void validateData(){
          sourceLanguageText=source_language.getText().toString().trim();

        if (sourceLanguageText.isEmpty()) {
           showToast("Enter text to translate");
        }else{
            startTranslate();
            System.out.println(sourceLanguageText);
        }
    }

    private void startTranslate() {
        progressDialog.setMessage("Translation is in Progress");
        progressDialog.show();

        // Retrieve the selected languages
        String sourceLanguageTitle = source_language_choose_btn.getText().toString().trim();
        String destinationLanguageTitle = target_language_choose_btn.getText().toString().trim();

        // Convert language titles to language codes
        String sourceLanguageCode = getLanguageCode(sourceLanguageTitle);
        String destinationLanguageCode = getLanguageCode(destinationLanguageTitle);

        System.out.println(sourceLanguageCode + " to " + destinationLanguageCode);

        // Set up the translator options
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(sourceLanguageCode)
                        .setTargetLanguage(destinationLanguageCode)
                        .build();
        final Translator translator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()

                .build();
        System.out.println(conditions);

        // Download the model if needed and perform the translation
        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(MainActivity.this, "Model downloaded successfully", Toast.LENGTH_SHORT).show();
                                progressDialog.setMessage("Translating...");

                                translator.translate(sourceLanguageText)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                Toast.makeText(MainActivity.this, "Failed to Translate: " + e, Toast.LENGTH_SHORT).show();
                                                System.out.println(e);
                                            }
                                        })
                                        .addOnSuccessListener(new OnSuccessListener<String>() {
                                            @Override
                                            public void onSuccess(String s) {
                                                System.out.println(s);
                                                progressDialog.dismiss();
                                                destination_language.setText(s);
                                            }
                                        });
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Failed due to " + e, Toast.LENGTH_SHORT).show();
                        System.out.println(e);
                    }
                });
    }

    private String getLanguageCode(String languageTitle) {
        for (Modellanguage languageModel : languageArrayList) {
            if (languageModel.getLanguageTitle().equals(languageTitle)) {
                return languageModel.getLanguageCode();
            }
        }
        return TranslateLanguage.ENGLISH; // default to English if not found
    }




    private void sourceLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(this, source_language_choose_btn);

        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();
                String sourceLanguageTitle = languageArrayList.get(position).getLanguageTitle();

                source_language_choose_btn.setText(sourceLanguageTitle);
                source_language.setHint("Enter " + sourceLanguageTitle);

                return true;
            }
        });
    }

    private void target_language_choose(){
        PopupMenu popupMenu = new PopupMenu(this, target_language_choose_btn);

        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();
                String sourceLanguageTitle = languageArrayList.get(position).getLanguageTitle();

                target_language_choose_btn.setText(sourceLanguageTitle);
                destination_language.setHint("Converted  In  : " + sourceLanguageTitle);

                return true;
            }
        });


    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}