package com.app.pharmacy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import com.app.pharmacy.common.entity.Cosmatic;
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

public class AddCosmatic extends AppCompatActivity {

    private View topAppBar;

    private TextInputEditText cosmaticName, price_field, email_field, manufature_field, des_field;

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
        setContentView(R.layout.activity_add_cosmatic);

        db = FirebaseFirestore.getInstance();
        topAppBar = (View) findViewById(R.id.topAppBar);
        cosmaticName = (TextInputEditText) findViewById(R.id.cosmaticName);
        price_field = findViewById(R.id.price_field);
        email_field = findViewById(R.id.email_field);
        manufature_field = findViewById(R.id.location_field);
        des_field = findViewById(R.id.des_field);
        save_button = findViewById(R.id.save_button);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        if (ContextCompat.checkSelfPermission(AddCosmatic.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddCosmatic.this, new String[]{
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

                String Cosmatic_name = cosmaticName.getText().toString();
                String price = price_field.getText().toString();
                String email = email_field.getText().toString();
                String manufacture = manufature_field.getText().toString();
                String des = des_field.getText().toString();
                String image = "";
                if (getImage() != null) {
                    image = getImage();
                }

                if (TextUtils.isEmpty(Cosmatic_name) && TextUtils.isEmpty(price) && TextUtils.isEmpty(email) && TextUtils.isEmpty(manufacture)) {

                    // 2. Warning message
                    TastyToast.makeText(
                            getApplicationContext(),
                            "required filed !",
                            TastyToast.LENGTH_LONG,
                            TastyToast.WARNING
                    );
                } else {

                    addDataToFirestore(Cosmatic_name, price, email, manufacture, des, image);
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

    /**
     * image docode
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


    public String encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imageEncoded;
    }


    private void addDataToFirestore(String cosmatic_name, String price, String email, String manufacture, String des, String image) {
        CollectionReference dbCourses = db.collection("stock");
        Cosmatic cosmatic = new Cosmatic();
        cosmatic.setCosName(cosmatic_name);
        cosmatic.setCosPrice(price);
        cosmatic.setEmail(email);
        cosmatic.setManfacture(manufacture);
        cosmatic.setDescription(des);
        cosmatic.setImage(image);
        cosmatic.setCosmatic(true);
        cosmatic.setMedicine(false);
        cosmatic.setFirst(false);
        cosmatic.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        // below method is use to add data to Firebase Firestore.
        dbCourses.add(cosmatic).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                // after the data addition is successful
                // we are displaying a success toast message.
                // 1. Success message
                TastyToast.makeText(
                        getApplicationContext(),
                        "Your Cosmatic Details has been Successfully added!",
                        TastyToast.LENGTH_LONG,
                        TastyToast.SUCCESS
                );
                startActivity(new Intent(getApplicationContext(), cosmaticStock.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // this method is called when the data addition process is failed.
                // displaying a toast message when data addition is failed.
                TastyToast.makeText(
                        getApplicationContext(),
                        "Fail to add Cosmatics" + e,
                        TastyToast.LENGTH_LONG,
                        TastyToast.ERROR
                );
            }
        });
    }

}



