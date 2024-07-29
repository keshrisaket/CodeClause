package com.example.pdfreader;

import static com.github.barteksc.pdfviewer.util.FitPolicy.*;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.github.barteksc.pdfviewer.PDFView;


public class MainActivity extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        pdfView=findViewById(R.id.pdfView);

        pdfView.fromAsset("pdf.pdf")
                .nightMode(true)
                .load();




    }
}