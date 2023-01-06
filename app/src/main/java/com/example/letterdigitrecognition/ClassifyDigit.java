package com.example.letterdigitrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.redhoodhan.draw.DrawView;

public class ClassifyDigit extends AppCompatActivity {

    DrawView drawView ;
    Button clearButton ;
    TextView predictionTextview;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_digit);

        drawView = findViewById(R.id.draw_view_id);
        clearButton = findViewById(R.id.clear_button);
        predictionTextview = findViewById(R.id.prediction_text_view);

        drawView.setBrushSize(60.0f);



        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawView.clearCanvas(true);
                predictionTextview.setText("Please draw a digit");
            }
        });



    }
}