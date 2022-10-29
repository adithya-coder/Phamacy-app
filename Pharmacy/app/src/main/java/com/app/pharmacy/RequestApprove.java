package com.app.pharmacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.app.pharmacy.common.PharmacyAdapter;
import com.app.pharmacy.common.RequestAdapter;
import com.app.pharmacy.common.entity.Pharmacy;
import com.app.pharmacy.common.entity.ProductRequest;
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

public class RequestApprove extends AppCompatActivity {
    private View topAppBar;
    private FloatingActionButton float_button;
    private RecyclerView view;
    private ArrayList<ProductRequest> pharmacyArrayList;
    private RequestAdapter adapter;
    private FirebaseFirestore db;
    private TextView name, names, username;
    private MaterialToolbar toolbar;
    private SearchView searchView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private CircleImageView imageView;
    // private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   getSupportActionBar().hide();
        setContentView(R.layout.activity_owner);

        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.more) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                    return true;
                } else if (item.getItemId() == R.id.search) {
                    SearchView searchView = (SearchView) item.getActionView();
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            //filter(s);
                            return false;
                        }
                    });
                    return true;
                } else {
                    return false;
                }
            }
        });
//        dialog = new ProgressDialog(this);
//        dialog.setCancelable(false);
//        dialog.setMessage("Loading...");
//        dialog.show();
        searchView = (SearchView) findViewById(R.id.searchView);
        name = findViewById(R.id.name);
        view = findViewById(R.id.rcView);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        getUserDetail();
        pharmacyArrayList = new ArrayList<ProductRequest>();
        adapter = new RequestAdapter(RequestApprove.this, pharmacyArrayList);

        float_button = findViewById(R.id.float_button);


        float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddPharmacy.class));
            }
        });

        view.setAdapter(adapter);
        EventChangeListener();
    }

    private void EventChangeListener() {
        String userId = (FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.collection("request").whereIn("userId", Collections.singletonList(userId)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {

                if (error != null) {
//                    if (dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
                    Log.e("db error", error.getMessage());
                    return;
                }


                for (DocumentChange dc : value.getDocumentChanges()) {
                    //  if(dc.getDocument().getString("userId").toString() == userId.toString()){
                    if (dc.getType() == DocumentChange.Type.ADDED) {

                        ProductRequest request = dc.getDocument().toObject(ProductRequest.class);
                        request.setId(dc.getDocument().getId());
//                        System.out.println("\nimagesssssssss----------------------\n" + pharmacy.getImage());
                        pharmacyArrayList.add(request);
                    }

                    // }
                    adapter.notifyDataSetChanged();
//                    if (dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
                }
            }
        });
    }

    //    private Pharmacy Mapper(DocumentChange snapshot, Pharmacy pharmacy){
//
//        pharmacy.setDescription(snapshot.getDocument().get("description").toString());
//        pharmacy.setEmail(snapshot.getDocument().get("email").toString());
//        pharmacy.setImage( snapshot.getDocument().get("image").toString());
//        pharmacy.setLocation(snapshot.getDocument().get("lication").toString());
//        pharmacy.setMobile(snapshot.getDocument().get("mobile").toString());
//        pharmacy.setName(snapshot.getDocument().get("name").toString());
//        pharmacy.setUserId(snapshot.getDocument().get("userId").toString());
//
//
//    }
//    private void filter(String text) {
//        // creating a new array list to filter our data.
//        ArrayList<Pharmacy> filteredlist = new ArrayList<Pharmacy>();
//
//        // running a for loop to compare elements.
//        for (Pharmacy item : pharmacyArrayList) {
//            // checking if the entered string matched with any item of our recycler view.
//            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
//                // if the item is matched we are
//                // adding it to our filtered list.
//                filteredlist.add(item);
//            }
//        }
//        if (filteredlist.isEmpty()) {
//            // if no item is added in filtered list we are
//            // displaying a toast message as no data found.
//            TastyToast.makeText(
//                    getApplicationContext(),
//                    "No Data Found..",
//                    TastyToast.LENGTH_LONG,
//                    TastyToast.CONFUSING
//            );
//
//        } else {
//            // at last we are passing that filtered
//            // list to our adapter class.
//            adapter.filterList(filteredlist);
//        }
//    }

    private void getUserDetail() {
        String userId = (FirebaseAuth.getInstance().getCurrentUser().getUid());
        DocumentReference documentReference = db.collection("Users").document(userId);
        View hader = navigationView.getHeaderView(0);
        names = hader.findViewById(R.id.names);
        username = hader.findViewById(R.id.username);
        imageView = hader.findViewById(R.id.profilePic);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name.setText("Hi! " + documentSnapshot.getString("name"));
                names.setText(documentSnapshot.getString("name"));
                username.setText(documentSnapshot.getString("email"));
                if (documentSnapshot.getString("image") != null) {
                    imageView.setImageBitmap(decodeBitmapAndSaveToFirebase(documentSnapshot.getString("image")));
                }
            }
        });

    }

    /**
     * decode byte image.
     *
     * @param image
     * @return
     */
    public Bitmap decodeBitmapAndSaveToFirebase(String image) {
        //decode base64 string to image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

}