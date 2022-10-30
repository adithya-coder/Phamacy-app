package com.app.pharmacy.common;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Parcelable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pharmacy.AddStock;
import com.app.pharmacy.MedicineStock;
import com.app.pharmacy.Owner;
import com.app.pharmacy.R;
import com.app.pharmacy.UpdateCosmatic;
import com.app.pharmacy.UpdateMedicine;
import com.app.pharmacy.common.entity.Cosmatic;
import com.app.pharmacy.common.entity.Medicine;
import com.app.pharmacy.cosmaticStock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.text.BreakIterator;
import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Medicine> medicineArrayList;
    private boolean stock = true;

    public MedicineAdapter(Context context, ArrayList<Medicine> medicineArrayList) {
        this.context = context;
        this.medicineArrayList = medicineArrayList;
    }

    public void filterList(ArrayList<Medicine> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        medicineArrayList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }


    @Override
    public MedicineAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.review, parent, false);
        return new MedicineAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MedicineAdapter.MyViewHolder holder, int position) {
        Medicine medicine = medicineArrayList.get(position);
        holder.medicineName.setText(medicine.getMedicineName());
        holder.dec.setText(medicine.getDescription());
        holder.price.setText(medicine.getMediPrice());

        if (medicine.getImage() != null) {
            Bitmap bitmap = decodeBitmapAndSaveToFirebase(medicine.getImage());
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

    private Bitmap decodeBitmapAndSaveToFirebase(String image) {
        //decode base64 string to image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    @Override
    public int getItemCount() {
        return medicineArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private Button delete_button, update_button;
        private TextView medicineName, dec, price;
        private FirebaseFirestore db;
        private ImageView logo;

        public MyViewHolder(View itemView) {
            super(itemView);
            db = FirebaseFirestore.getInstance();
            medicineName = (TextView) itemView.findViewById(R.id.name);
            dec = (TextView) itemView.findViewById(R.id.des);
            price = (TextView) itemView.findViewById(R.id.price);
            delete_button = (Button) itemView.findViewById(R.id.delete_button);
            update_button = (Button) itemView.findViewById(R.id.update_button);
            logo = (ImageView) itemView.findViewById(R.id.logo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Medicine medicine = medicineArrayList.get(getAdapterPosition());
                    // below line is creating a new intent.
                    Intent i = new Intent(context, MedicineStock.class);

                    i.putExtra("stock", stock);
                    context.startActivity(i);
                }
            });
          delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  owner= new Owner().deleteCourse();
                    Medicine medicine = medicineArrayList.get(getAdapterPosition());
                    AlertDialog dialog = new MaterialAlertDialogBuilder(context,
                            R.style.MaterialAlertDialog_App_Title_Text)
                            .setMessage("Are You Sure?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteCourse(medicine);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setTextSize(18);
                    dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextSize(18);

                    TextView textView = dialog.findViewById(android.R.id.message);
                    if (textView != null) {
                        textView.setTextSize(18);
                    }
                    TextView textViewTitle = dialog.findViewById(android.R.id.title);
                    if (textViewTitle != null) {
                        textViewTitle.setTextSize(18);
                    }
                }
            });

            update_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Medicine medicine = medicineArrayList.get(getAdapterPosition());
                    // below line is creating a new intent.
                    Intent i = new Intent(context, UpdateMedicine.class);
                    i.putExtra("stock", stock);
                    context.startActivity(i);

                }
            });
        }

        public void deleteCourse(Medicine medicine) {
            // below line is for getting the collection
            // where we are storing our courses.
            String ids = (medicine.getId());
            db.collection("stock").
                    // after that we are getting the document
                    // which we have to delete.
                            document(ids).

                    // after passing the document id we are calling
                    // delete method to delete this document.
                            delete().
                    // after deleting call on complete listener
                    // method to delete this data.
                            addOnCompleteListener(new OnCompleteListener<Void>() {

                        @Override
                        public void onComplete(Task<Void> task) {
                            // inside on complete method we are checking
                            // if the task is success or not.
                            if (task.isSuccessful()) {
                                TastyToast.makeText(
                                        context,
                                        "Your Medicine details has been Successfully delete!",
                                        TastyToast.LENGTH_SHORT,
                                        TastyToast.SUCCESS

                                );

                                Intent i = new Intent(context, MedicineStock.class);
                                context.startActivity(i);
                            } else {
                                TastyToast.makeText(
                                        context,
                                        "Fail to delete Medicine details",
                                        TastyToast.LENGTH_LONG,
                                        TastyToast.ERROR
                                );
                            }
                        }
                    });

        }
    }
}









