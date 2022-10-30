package com.app.pharmacy;

import androidx.annotation.RequiresApi;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;

public class UpdateCosmatic extends AppCompatActivity {

    private View topAppBar;

    private TextInputEditText cosmatic_title_field, price_field, email_field, manufature_field, des_field;

    private Button update_button, close_button;

    private ImageButton imageButton;

    private String image;

    private FirebaseFirestore db;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cosmatic);

        db = FirebaseFirestore.getInstance();
        topAppBar = (View) findViewById(R.id.topAppBar);
        cosmatic_title_field = findViewById(R.id.cosmaticName);
        price_field = findViewById(R.id.price_field);
        email_field = findViewById(R.id.email_field);
        manufature_field = findViewById(R.id.location_field);
        des_field = findViewById(R.id.des_field);
        update_button = findViewById(R.id.update_button);
        close_button = findViewById(R.id.close_button);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        if (ContextCompat.checkSelfPermission(UpdateCosmatic.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UpdateCosmatic.this, new String[]{
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

        Cosmatic cosmatic = (Cosmatic) getIntent().getSerializableExtra("cosmatic");
        cosmatic_title_field.setText(cosmatic.getCosName());
        price_field.setText(cosmatic.getCosPrice());
        email_field.setText(cosmatic.getEmail());
        manufature_field.setText(cosmatic.getManfacture());
        des_field.setText(cosmatic.getDescription());
        if (cosmatic.getImage() != null) {
            imageButton.setImageIcon(Icon.createWithBitmap(decodeBitmapAndSaveToFirebase(cosmatic.getImage())));
            setImage(cosmatic.getImage());
        }

        topAppBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Owner.class));
            }
        });

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), cosmaticStock.class));
                finish();
            }
        });

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String cosmatic_name = cosmatic_title_field.getText().toString();
                String price = price_field.getText().toString();
                String email = email_field.getText().toString();
                String manufacture = manufature_field.getText().toString();
                String des = des_field.getText().toString();


           if (TextUtils.isEmpty(cosmatic_name) && TextUtils.isEmpty(price) && TextUtils.isEmpty(email) && TextUtils.isEmpty(manufacture)) {

                // 2. Warning message
                TastyToast.makeText(
                        getApplicationContext(),
                        "required filed !",
                        TastyToast.LENGTH_LONG,
                        TastyToast.WARNING
                );
            } else {

                update(cosmatic_name, price, email, manufacture, des, image);
            }
        }
    });
}

    /**
     * for  event camera activity result.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     *  encode image to base64
     *
     * @param bitmap
     * @return
     */
    public String encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        return imageEncoded;
    }

    /**
     *  update pharmacy

     * @param cosmatic_name
     * @param email
     * @param des
     * @param image
     */
    private void update( String cosmatic_name, String price, String email,String manufature, String des, String image) {
        Cosmatic stock = (Cosmatic) getIntent().getSerializableExtra("cosmatic");
        Cosmatic cosmatic = new Cosmatic();
        cosmatic.setCosName(cosmatic_name);
        cosmatic.setCosPrice(price);
        cosmatic.setEmail(email);
        cosmatic.setManfacture(manufature);
        cosmatic.setDescription(des);
        cosmatic.setImage(image);
        cosmatic.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.collection("stock").document(stock.getId()).set(cosmatic).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // on successful completion of this process
                // we are displaying the toast message.
                TastyToast.makeText(
                        getApplicationContext(),
                        "Your Cosmetic has been Successfully update!",
                        TastyToast.LENGTH_LONG,
                        TastyToast.SUCCESS

                );
                startActivity(new Intent(getApplicationContext(), cosmaticStock.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            // inside on failure method we are
            // displaying a failure message.
            @Override
            public void onFailure(Exception e) {
                TastyToast.makeText(
                        getApplicationContext(),
                        "Fail to update Cosmatic" + e,
                        TastyToast.LENGTH_LONG,
                        TastyToast.ERROR
                );
            }
        });
    }
}








