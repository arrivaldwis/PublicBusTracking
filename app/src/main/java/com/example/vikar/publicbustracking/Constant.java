package com.example.vikar.publicbustracking;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Constant {
    public static DatabaseReference refUser = FirebaseDatabase.getInstance().getReference("User");
    public static DatabaseReference refRute = FirebaseDatabase.getInstance().getReference("Rute");
    public static DatabaseReference refStation = FirebaseDatabase.getInstance().getReference("Station");
    public static DatabaseReference refBus = FirebaseDatabase.getInstance().getReference("Bus");
    public static DatabaseReference refTrack = FirebaseDatabase.getInstance().getReference("track");

    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageRef = storage.getReference();
}
