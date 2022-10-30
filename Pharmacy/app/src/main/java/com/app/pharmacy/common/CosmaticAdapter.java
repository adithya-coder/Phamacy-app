package com.app.pharmacy.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.app.pharmacy.AddStock;
import com.app.pharmacy.MedicineStock;
import com.app.pharmacy.Owner;
import com.app.pharmacy.R;
import com.app.pharmacy.UpdateCosmatic;
import com.app.pharmacy.UpdatePharmacy;
import com.app.pharmacy.common.entity.Cosmatic;
import com.app.pharmacy.common.entity.Medicine;
import com.app.pharmacy.common.entity.Pharmacy;
import com.app.pharmacy.cosmaticStock;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.text.CollationElementIterator;
import java.util.ArrayList;

public class CosmaticAdapter extends RecyclerView.Adapter<CosmaticAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Cosmatic> cosmaticArrayList;

    public CosmaticAdapter(Context context, ArrayList<Cosmatic> cosmaticArrayList) {
        this.context = context;
        this.cosmaticArrayList = cosmaticArrayList;
    }


    public void filterList(ArrayList<Cosmatic> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        cosmaticArrayList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @Override
    public CosmaticAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.review, parent, false);
        return new CosmaticAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CosmaticAdapter.MyViewHolder holder, int position) {
        Cosmatic cosmatic = cosmaticArrayList.get(position);
        holder.cosName.setText(cosmatic.getCosName());
        holder.dec.setText(cosmatic.getDescription());
        holder.price.setText(cosmatic.getCosPrice());

        if(cosmatic.getImage() !=null){
            Bitmap bitmap= decodeBitmapAndSaveToFirebase(cosmatic.getImage());
            int maxHeight = 600;
            int maxWidth = 300;
            float scale = Math.min(((float)maxHeight / bitmap.getWidth()), ((float)maxWidth / bitmap.getHeight()));

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if(bitmap != null) {
                Log.d("Image", bitmap.toString());
                holder.logo.setImageBitmap(bitmap);
            }
        }


    }

    public Bitmap  decodeBitmapAndSaveToFirebase(String image) {
        //decode base64 string to image
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        return decodedImage;
    }

    @Override
    public int getItemCount() {
        return cosmaticArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private Button delete_button, update_button;
        private TextView cosName, dec, price;
        private FirebaseFirestore db;
        private ImageView logo;

        public MyViewHolder(View itemView) {
            super(itemView);
            db = FirebaseFirestore.getInstance();
            cosName = (TextView) itemView.findViewById(R.id.name);
            dec = (TextView) itemView.findViewById(R.id.des);
            price = (TextView) itemView.findViewById(R.id.price);
            delete_button = (Button) itemView.findViewById(R.id.delete_button);
            update_button = (Button) itemView.findViewById(R.id.update_button);
            logo = (ImageView) itemView.findViewById(R.id.logo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Cosmatic cosmatic = cosmaticArrayList.get(getAdapterPosition());
                    // below line is creating a new intent.
                    Intent i = new Intent(context, cosmaticStock.class);
                    i.putExtra("cosmatic", cosmatic);
                    context.startActivity(i);
                }
            });

            delete_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  owner= new Owner().deleteCourse();
                    Cosmatic cosmatic = cosmaticArrayList.get(getAdapterPosition());
                    AlertDialog dialog = new MaterialAlertDialogBuilder(context,
                            R.style.MaterialAlertDialog_App_Title_Text)
                            .setMessage("Are You Sure?")
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteCourse(cosmatic);
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
                    Cosmatic cosmatic = cosmaticArrayList.get(getAdapterPosition());
                    // below line is creating a new intent.
                    Intent i = new Intent(context, UpdateCosmatic.class);
                    i.putExtra("cosmatic", cosmatic);
                    context.startActivity(i);

                }
            });
        }

        public void deleteCourse(Cosmatic cosmatic) {
            // below line is for getting the collection
            // where we are storing our courses.
            String ids = (cosmatic.getId());
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
                                        "Your Cosmatic details has been Successfully delete!",
                                        TastyToast.LENGTH_SHORT,
                                        TastyToast.SUCCESS

                                );

                                Intent i = new Intent(context, cosmaticStock.class);
                                context.startActivity(i);
                            } else {
                                TastyToast.makeText(
                                        context,
                                        "Fail to delete Cosmatic details",
                                        TastyToast.LENGTH_LONG,
                                        TastyToast.ERROR
                                );
                            }
                        }
                    });

        }
    }

}
