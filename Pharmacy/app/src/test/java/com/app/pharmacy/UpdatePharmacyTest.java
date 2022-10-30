package com.app.pharmacy;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;

import com.app.pharmacy.common.entity.Pharmacy;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class UpdatePharmacyTest {
@Mock
    private UpdatePharmacy updatePharmacy;

    private Intent intent;

    private FirebaseFirestore db;

    @Before
    public void setUp() {
        updatePharmacy = new UpdatePharmacy();

        intent = mock(Intent.class);

        db = mock(FirebaseFirestore.class);

        updatePharmacy.setIntent(intent);

      //  updatePharmacy.setDb(db);
    }

    /**
     * Should throw an exception when the name, phone and email are empty
     */
    @Test
    public void updateWhenNamePhoneAndEmailAreEmptyThenThrowException() {
        String name = "";
        String phone = "";
        String email = "";
        String location = "";
        String des = "";
        String image = "";

        updatePharmacy.update(name, phone, email, location, des, image);

        verify(intent).getSerializableExtra("pharmacy");

        verify(db).collection("pharmacy");

        verify(db).document(anyString());


    }

    /**
     * Should update the pharmacy when the name, phone and email are not empty
     */
    @Test
    public void updateWhenNamePhoneAndEmailAreNotEmpty() {
        String name = "name";

        String phone = "phone";

        String email = "email";

        String location = "location";

        String des = "des";

        String image = "image";

        when(intent.getSerializableExtra("pharmacy")).thenReturn(new Pharmacy());

        updatePharmacy.update(name, phone, email, location, des, image);

        verify(db).collection("pharmacy").document(anyString()).set(any(Pharmacy.class));
    }
}