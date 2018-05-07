package com.example.vikar.publicbustracking.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.example.vikar.publicbustracking.Constant;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.fragment.BusTracking;
import com.example.vikar.publicbustracking.fragment.Maps;
import com.example.vikar.publicbustracking.fragment.ProfileUser;
import com.example.vikar.publicbustracking.fragment.Route;
import com.example.vikar.publicbustracking.model.UserModel;
import com.example.vikar.publicbustracking.ui.BottomNavigationViewHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static com.example.vikar.publicbustracking.activity.signup.refPhotoProfile;
import static com.example.vikar.publicbustracking.fragment.ProfileUser.img_user;

public class Menu extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_route:
                    selectedFragment = Route.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, selectedFragment).commit();
                    return true;
                case R.id.navigation_bustracking:
                    selectedFragment = BusTracking.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, selectedFragment).commit();
                    return true;
                case R.id.navigation_maps:
                    selectedFragment = Maps.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, selectedFragment).commit();
                    return true;
                case R.id.navigation_profile:
                    selectedFragment = ProfileUser.newInstance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, selectedFragment).commit();
                    return true;
            }
            return true;
        }
    };

    FirebaseUser CurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, Route.newInstance());
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            com.esafirm.imagepicker.model.Image image = ImagePicker.getFirstImageOrNull(data);
            File imgFile = new File(image.getPath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img_user.setImageBitmap(myBitmap);
                updateFoto();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    ProgressDialog pDialog;
    private void updateFoto() {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait..");
        pDialog.show();

        refPhotoProfile = Constant.storageRef.child("user/" + System.currentTimeMillis() + ".jpg");
        StorageReference photoImagesRef = Constant.storageRef.child("user/" + System.currentTimeMillis() + ".jpg");
        refPhotoProfile.getName().equals(photoImagesRef.getName());
        refPhotoProfile.getPath().equals(photoImagesRef.getPath());

        img_user.setDrawingCacheEnabled(true);
        img_user.buildDrawingCache();
        Bitmap bitmap = img_user.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = refPhotoProfile.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() c
                // ontains file metadata such as size, content-type, and download URL.
                final Uri photoUrl = taskSnapshot.getDownloadUrl();

                Constant.refUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren()) {
                            UserModel model = ds.getValue(UserModel.class);
                            if(model.getEmail().equals(CurrentUser.getEmail())) {
                                Constant.refUser.child(ds.getKey()).child("image").setValue(photoUrl.toString());
                            }
                        }
                        pDialog.dismiss();
                        Toast.makeText(Menu.this, "Photo has been updated!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
