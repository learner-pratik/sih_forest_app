package com.example.forest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeActivity extends AppCompatActivity {
    //Button btnMap,btnPrediction,btnTask;
    LinearLayout cvMap,cvPrediction,cvTask,cvAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cvMap=findViewById(R.id.cvMap);
        cvPrediction=findViewById(R.id.cvPrediction);
        cvTask=findViewById(R.id.cvTask);
        cvAlert=findViewById(R.id.cvAlert);

        cvTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeActivity.this,TaskActivity.class);
                startActivity(a);
            }
        });

        cvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeActivity.this,LocationActivity.class);
                startActivity(a);
            }
        });

        cvPrediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(HomeActivity.this,LocationActivity.class);
                startActivity(a);
            }
        });

    }
}