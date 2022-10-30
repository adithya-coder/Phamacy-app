package com.app.pharmacy.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.app.pharmacy.AddStock;
import com.app.pharmacy.R;
import com.app.pharmacy.common.entity.Cosmatic;
import com.app.pharmacy.common.entity.Pharmacy;
import com.app.pharmacy.common.entity.ProductRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import lombok.NonNull;

public class CustomerAdapter extends RecyclerView.Adapter<com.app.pharmacy.common.CustomerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Cosmatic> pharmacyArrayList;
    private String productId;
    private String pharmacyId;

    public CustomerAdapter(Context context, ArrayList<Cosmatic> pharmacyArrayList) {
        this.context = context;
        this.pharmacyArrayList = pharmacyArrayList;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(String pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public void filterList(ArrayList<Cosmatic> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        pharmacyArrayList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.customerview, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Cosmatic pharmacy = pharmacyArrayList.get(position);
        holder.name.setText(pharmacy.getCosName());
        holder.dec.setText(pharmacy.getDescription()+" Powerd By : "+pharmacy.getManfacture()+" Contact : "+pharmacy.getEmail());
        holder.mobile.setText("Rs "+pharmacy.getCosPrice()+". 00");
        setPharmacyId(pharmacy.getId());
        setProductId(pharmacy.getUserId());

        if (pharmacy.getImage() != null) {
            Bitmap bitmap = decodeBitmapAndSaveToFirebase(pharmacy.getImage());
            int maxHeight = 600;
            int maxWidth = 300;
            float scale = Math.min(((float) maxHeight / bitmap.getWidth()), ((float) maxWidth / bitmap.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap != null) {
                Log.d("Image", bitmap.toString());
                holder.logo.setImageBitmap(bitmap);
            }
        }
    }

    public Bitmap decodeBitmapAndSaveToFirebase(String image) {
        //decode base64 string to image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    @Override
    public int getItemCount() {
        return pharmacyArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private Button request_button;
        private TextView name, dec, mobile;
        private FirebaseFirestore db;
        private ImageView logo;

        public MyViewHolder(View itemView) {
            super(itemView);
            db = FirebaseFirestore.getInstance();
            name = (TextView) itemView.findViewById(R.id.name);
            dec = (TextView) itemView.findViewById(R.id.des);
            mobile = (TextView) itemView.findViewById(R.id.mobile);
            request_button = (Button) itemView.findViewById(R.id.request_button);
            //  update_button = (Button) itemView.findViewById(R.id.update_button);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            ;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cosmatic pharmacy = pharmacyArrayList.get(getAdapterPosition());
                    // below line is creating a new intent.
                    Intent i = new Intent(context, AddStock.class);
                    i.putExtra("pharmacy", pharmacy);
                    context.startActivity(i);
                }
            });
//                delete_button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //  owner= new Owner().deleteCourse();
//                        Pharmacy pharmacy = pharmacyArrayList.get(getAdapterPosition());
//                        AlertDialog dialog = new MaterialAlertDialogBuilder(context,
//                                R.style.MaterialAlertDialog_App_Title_Text)
//                                .setMessage("Are You Sure?")
//                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        deleteCourse(pharmacy);
//                                    }
//                                })
//                                .setNegativeButton("No", null)
//                                .show();
//                        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextSize(18);
//                        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextSize(18);
//
//                        TextView textView = dialog.findViewById(android.R.id.message);
//                        if (textView != null) {
//                            textView.setTextSize(18);
//                        }
//                        TextView textViewTitle = dialog.findViewById(android.R.id.title);
//                        if (textViewTitle != null) {
//                            textViewTitle.setTextSize(18);
//                        }
//                    }
//                });
            request_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addDataToFirestore(getPharmacyId(),getProductId(),false);

//                        Pharmacy pharmacy = pharmacyArrayList.get(getAdapterPosition());
                    // below line is creating a new intent.
//                        Intent i = new Intent(context, UpdatePharmacy.class);
//                        i.putExtra("pharmacy", pharmacy);
//                        context.startActivity(i);

                }
            });
        }


        private void addDataToFirestore(String pharmacyId, String productId, Boolean isClame) {

            // creating a collection reference
            // for our Firebase Firetore database.
            CollectionReference reference = db.collection("request");
            // adding our data to our courses object class.
            ProductRequest request = new ProductRequest();
            request.setPharmacyId(pharmacyId);
            request.setClame(isClame);
            request.setProductId(productId);
            request.setRequestBy(FirebaseAuth.getInstance().getCurrentUser().getUid());

            // below method is use to add data to Firebase Firestore.
            reference.add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    // after the data addition is successful
                    // we are displaying a success toast message.
                    // 1. Success message
                    TastyToast.makeText(
                            context.getApplicationContext(),
                            " Successfully Requested !",
                            TastyToast.LENGTH_LONG,
                            TastyToast.SUCCESS
                    );
                    // startActivity(new Intent(getApplicationContext(), Owner.class));
                    // finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // this method is called when the data addition process is failed.
                    // displaying a toast message when data addition is failed.
                    TastyToast.makeText(
                            context.getApplicationContext(),
                            "Fail to Request !" + e,
                            TastyToast.LENGTH_LONG,
                            TastyToast.ERROR
                    );
                }
            });
        }
    }


}
