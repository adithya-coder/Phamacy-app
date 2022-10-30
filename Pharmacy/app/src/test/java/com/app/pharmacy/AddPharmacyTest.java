package com.app.pharmacy;

import junit.framework.TestCase;

import org.junit.Test;

public class AddPharmacyTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testGetImage() {
    }

    public void testSetImage() {
    }

    public void testOnCreate() {
    }

    public void testOnActivityResult() {
    }

    public void testEncodeBitmapAndSaveToFirebase() {
    }

    /**
     * Should save the pharmacy when the name, phone and email are not empty
     */
    @Test
    public void addDataToFirestoreWhenNamePhoneAndEmailAreNotEmpty() {
        String name = "name";
        String phone = "phone";
        String email = "email";
        String location = "location";
        String des = "des";
        String image = "image";
        AddPharmacy addPharmacy = new AddPharmacy();
        try {
            addPharmacy.addDataToFirestore(name, phone, email, location, des, image);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("required filed !", e.getMessage());
        }
    }

    /**
     * Should throw an exception when the name, phone and email are empty
     */
    @Test
    public void addDataToFirestoreWhenNamePhoneAndEmailAreEmptyThenThrowException() {
        String name = "";
        String phone = "";
        String email = "";
        String location = "";
        String des = "";
        String image = "";
        AddPharmacy addPharmacy = new AddPharmacy();
        try {
            addPharmacy.addDataToFirestore(name, phone, email, location, des, image);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("required filed !", e.getMessage());
        }
    }
}