package com.app.pharmacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.pharmacy.common.CosmaticAdapter;
import com.app.pharmacy.common.FirstAdapter;
import com.app.pharmacy.common.entity.Cosmatic;
import com.app.pharmacy.common.entity.FirstAid;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FirstaidStock extends AppCompatActivity {
    private View topAppBar;
    private FloatingActionButton float_button;
    private RecyclerView view;
    private ArrayList<FirstAid> firstAidArrayList;
    private FirstAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstaid_stock);

        db = FirebaseFirestore.getInstance();
        firstAidArrayList  = new ArrayList<FirstAid>();
        adapter = new FirstAdapter(FirstaidStock.this, firstAidArrayList);

        topAppBar = findViewById(R.id.topAppBar);
        float_button = findViewById(R.id.float_button);

        float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddFirstaid.class));
            }
        });
        topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Owner.class));
                finish();
            }
        });
        // view.setAdapter(adapter);
    }

}