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
import com.app.pharmacy.RequestApprove;
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

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ProductRequest> requestArrayList;
    private String productId;
    private String pharmacyId;

    public RequestAdapter(Context context, ArrayList<ProductRequest> approveArrayList) {
        this.context = context;
        this.requestArrayList = approveArrayList;
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

//    public void filterList(ArrayList<ProductRequest> filterlist) {
//        // below line is to add our filtered
//        // list in our course array list.
//        requestArrayList = filterlist;
//        // below line is to notify our adapter
//        // as change in recycler view data.
//        notifyDataSetChanged();
//    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.request_header, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ProductRequest approve = requestArrayList.get(position);
//        holder.name.setText(pharmacy.getName());
//        holder.dec.setText(pharmacy.getDescription());
//        holder.mobile.setText(pharmacy.getMobile());
//        setPharmacyId(pharmacy.getId());
//        setProductId(pharmacy.getUserId());
//
//        if (pharmacy.getImage() != null) {
//            Bitmap bitmap = decodeBitmapAndSaveToFirebase(pharmacy.getImage());
//            int maxHeight = 600;
//            int maxWidth = 300;
//            float scale = Math.min(((float) maxHeight / bitmap.getWidth()), ((float) maxWidth / bitmap.getHeight()));
//
//            Matrix matrix = new Matrix();
//            matrix.postScale(scale, scale);
//
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//            if (bitmap != null) {
//                Log.d("Image", bitmap.toString());
//                holder.logo.setImageBitmap(bitmap);
//            }
//        }


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
        return requestArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private Button request_button;
        private TextView name, dec, mobile;
        private FirebaseFirestore db;
        private ImageView logo;

        public MyViewHolder(View itemView) {
            super(itemView);
            db = FirebaseFirestore.getInstance();
//            name = (TextView) itemView.findViewById(R.id.name);
//            dec = (TextView) itemView.findViewById(R.id.des);
//            mobile = (TextView) itemView.findViewById(R.id.mobile);
            //request_button = (Button) itemView.findViewById(R.id.request_button);
            //  update_button = (Button) itemView.findViewById(R.id.update_button);
           // logo = (ImageView) itemView.findViewById(R.id.logo);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductRequest approve = requestArrayList.get(getAdapterPosition());
                    // below line is creating a new intent.
//                    Intent i = new Intent(context, AddStock.class);
//                    i.putExtra("pharmacy", approve);
                   // context.startActivity(i);
                }
            });

        }

    }

}

