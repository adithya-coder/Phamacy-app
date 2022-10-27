package com.app.pharmacy;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.pharmacy.common.entity.Cosmatics;
import com.app.pharmacy.common.entity.Pharmacy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;

import lombok.NonNull;

public class AddCosmatics extends AppCompatActivity {

    private View topAppBar;

    private TextInputEditText cosmatic_name_field, price_field, manufacture_field, des_field;

    private Button save_button;

    private ImageButton imageButton;

    private ImageView imageView;

    private FirebaseFirestore db;

    private String image;

    private StorageReference storageReference;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //  getSupportActionBar().hide();
        setContentView(R.layout.activity_add_pharmacy);
        db = FirebaseFirestore.getInstance();
        topAppBar = (View) findViewById(R.id.topAppBar);
        cosmatic_name_field = findViewById(R.id.name_field);
        price_field = findViewById(R.id.price_field);
        manufacture_field = findViewById(R.id.manu_field);
        des_field = findViewById(R.id.des_field);
        save_button = findViewById(R.id.save_button);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        if (ContextCompat.checkSelfPermission(AddCosmatics.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddCosmatics.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Owner.class));
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = cosmatic_name_field.getText().toString();
                String price = price_field.getText().toString();
                String manufacture = manufacture_field.getText().toString();
                String des = des_field.getText().toString();
                String image = "";
                if (getImage() != null) {
                    image = getImage();
                }

                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(price) && TextUtils.isEmpty(manufacture)) {

                    // 2. Warning message
                    TastyToast.makeText(
                            getApplicationContext(),
                            "required filed !",
                            TastyToast.LENGTH_LONG,
                            TastyToast.WARNING
                    );
                } else {

                    addDataToFirestore(name, price, manufacture, des, image);
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            // imageView.setImageBitmap(bitmap);
            setImage(encodeBitmapAndSaveToFirebase(bitmap));
            if (getImage() != null) {
                imageButton.setImageIcon(Icon.createWithBitmap(decodeBitmapAndSaveToFirebase(getImage())));
            }
        }
    }

    private Bitmap decodeBitmapAndSaveToFirebase(String image) {
        //decode base64 string to image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }


    private String encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imageEncoded;
    }

    private void addDataToFirestore(String name, String price, String manufacture, String des, String image) {

        // creating a collection reference
        // for our Firebase Firetore database.
        CollectionReference dbCourses = db.collection("cosmatics");
        // adding our data to our courses object class.
        Cosmatics cosmatics = new Cosmatics();
        cosmatics.setCosmaticName(name);
        cosmatics.setPrice(price);
        cosmatics.setManufactureName(manufacture);
        cosmatics.setDescription(des);
        cosmatics.setImage(image);
        cosmatics.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        // below method is use to add data to Firebase Firestore.
        dbCourses.add(cosmatics).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                // 1. Success message
                TastyToast.makeText(
                        getApplicationContext(),
                        "Your Cosmatic has been Successfully added!",
                        TastyToast.LENGTH_LONG,
                        TastyToast.SUCCESS
                );
                startActivity(new Intent(getApplicationContext(), Owner.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                TastyToast.makeText(
                        getApplicationContext(),
                        "Fail to add Cosmatic" + e,
                        TastyToast.LENGTH_LONG,
                        TastyToast.ERROR
                );
            }
        });
    }

}







