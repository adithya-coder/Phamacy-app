package com.app.pharmacy;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class catgory extends AppCompatActivity {
    private ImageButton cosmaticBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catgory);
        cosmaticBtn =findViewById(R.id.cos_btn);
        cosmaticBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(catgory.this,CosmaticStock.class);
                startActivity(intent);

            }
        });
    }
}