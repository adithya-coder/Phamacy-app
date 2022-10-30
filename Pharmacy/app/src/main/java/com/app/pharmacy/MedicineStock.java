package com.app.pharmacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.app.pharmacy.common.CosmaticAdapter;
import com.app.pharmacy.common.MedicineAdapter;
import com.app.pharmacy.common.entity.Cosmatic;
import com.app.pharmacy.common.entity.Medicine;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedicineStock extends AppCompatActivity {
    private View topAppBar;
    private FloatingActionButton float_button;
    private RecyclerView view;
    private ArrayList<Medicine> medicineArrayList;
    private MedicineAdapter adapter;
    private FirebaseFirestore db;
    private MaterialToolbar toolbar;
    private TextView name, price, des;
    private SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CircleImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_stock);

        db = FirebaseFirestore.getInstance();
        medicineArrayList  = new ArrayList<Medicine>();
        adapter = new MedicineAdapter(MedicineStock.this, medicineArrayList);

        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        searchView = (SearchView) findViewById(R.id.searchView);
        name = findViewById(R.id.name);
        view = findViewById(R.id.rcView);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        getMedicine();
        medicineArrayList = new ArrayList<Medicine>();
        adapter = new MedicineAdapter(MedicineStock.this, medicineArrayList);
        float_button = findViewById(R.id.float_button);

        float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddMedicine.class));
            }
        });

        view.setAdapter(adapter);
        EventChangeListener();

        topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Owner.class));
                finish();
            }
        });

        view.setAdapter(adapter);
        EventChangeListener();
    }

    private void EventChangeListener() {
        String userId = (FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.collection("stock").whereIn("userId", Collections.singletonList(userId)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {

                if (error != null) {
//
                    Log.e("db error", error.getMessage());
                    return;
                }


                for (DocumentChange dc : value.getDocumentChanges()) {
                    //  if(dc.getDocument().getString("userId").toString() == userId.toString()){
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        if(dc.getDocument().getBoolean("medicine")==true){

                        Medicine medicine = dc.getDocument().toObject(Medicine.class);
                        medicine.setId(dc.getDocument().getId());
                        System.out.println("\nimagesssssssss----------------------\n" + medicine.getImage());
                        medicineArrayList.add(medicine);
                    }

                    }
                    adapter.notifyDataSetChanged();

                }
            }
        });
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Medicine> filteredlist = new ArrayList<Medicine>();

        // running a for loop to compare elements.
        for (Medicine item : medicineArrayList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getMedicineName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            TastyToast.makeText(
                    getApplicationContext(),
                    "No Data Found..",
                    TastyToast.LENGTH_LONG,
                    TastyToast.CONFUSING
            );

        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }
    private void getMedicine() {
        String userId = (FirebaseAuth.getInstance().getCurrentUser().getUid());
        DocumentReference documentReference = db.collection("stock").document();
        //       View review = navigationView.getHeaderView(0);
//        name = review.findViewById(R.id.names);
//        price = review.findViewById(R.id.username);
//        imageView = review.findViewById(R.id.profilePic);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText(documentSnapshot.getString("medicine"));
//              price.setText(documentSnapshot.getString("cosPrice"));
//             des.setText(documentSnapshot.getString("description"));
                if (documentSnapshot.getString("image") != null) {
                    imageView.setImageBitmap(decodeBitmapAndSaveToFirebase(documentSnapshot.getString("image")));
                }
            }
        });

    }

    public Bitmap decodeBitmapAndSaveToFirebase(String image) {
        //decode base64 string to image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

}







