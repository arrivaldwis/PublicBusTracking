package com.example.vikar.publicbustracking.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.example.vikar.publicbustracking.Constant;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.model.TrackModel;
import com.example.vikar.publicbustracking.model.UserModel;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by vikar on 10-Jan-18.
 */

public class Driver extends AppCompatActivity {
    private Button btnlogoutdriver;
    private MyLocationListener mLocationListener;
    private LocationManager mLocationManager;
    private Switch swOn;
    private boolean status = false;
    private EditText tv_id;
    private EditText tv_bus_no;
    private EditText tv_name;
    private EditText tv_email;
    private ImageView img_user;
    public static StorageReference refPhotoProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        btnlogoutdriver = (Button) findViewById(R.id.btnlogoutdriver);
        tv_id = (EditText) findViewById(R.id.tv_id);
        tv_bus_no = (EditText) findViewById(R.id.tv_bus_no);
        tv_name = (EditText) findViewById(R.id.tv_name);
        tv_email = (EditText) findViewById(R.id.tv_email);
        img_user = (ImageView) findViewById(R.id.img_user);
        swOn = (Switch) findViewById(R.id.switch1);
        btnlogoutdriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Driver.this, login.class));
                finish();
            }
        });

        swOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                status = b;
            }
        });

        loadProfile();

        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(Driver.this)
                        .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                        .folderMode(true) // folder mode (false by default)
                        .toolbarFolderTitle("Folder") // folder selection title
                        .toolbarImageTitle("Tap to select") // image selection title
                        .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                        .single() // single mode
                        .limit(1) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                        .enableLog(false) // disabling log
                        .start(); // start image picker activity with request code\
            }
        });

        mLocationListener = new MyLocationListener();
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mLocationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
    }

    boolean isPic = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            com.esafirm.imagepicker.model.Image image = ImagePicker.getFirstImageOrNull(data);
            File imgFile = new File(image.getPath());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img_user.setImageBitmap(myBitmap);
                isPic = true;

                if (isPic) {
                    refPhotoProfile = Constant.storageRef.child("user/" + System.currentTimeMillis() + ".jpg");
                    StorageReference photoImagesRef = Constant.storageRef.child("user/" + System.currentTimeMillis() + ".jpg");
                    refPhotoProfile.getName().equals(photoImagesRef.getName());
                    refPhotoProfile.getPath().equals(photoImagesRef.getPath());

                    img_user.setDrawingCacheEnabled(true);
                    img_user.buildDrawingCache();
                    Bitmap bitmap = img_user.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] datas = baos.toByteArray();

                    UploadTask uploadTask = refPhotoProfile.putBytes(datas);
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
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        UserModel model = ds.getValue(UserModel.class);
                                        if (model.getEmail().equals(email)) {
                                            Constant.refUser.child(ds.getKey()).child("image").setValue(photoUrl.toString());
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(final Location loc) {
            if (status) {
                Toast.makeText(getBaseContext(),
                        "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                                + loc.getLongitude(), Toast.LENGTH_SHORT).show();

                Constant.refTrack.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            TrackModel model = ds.getValue(TrackModel.class);
                            if (String.valueOf(model.getId_user()).equals(id)) {
                                Constant.refTrack.child(ds.getKey()).setValue(new TrackModel(
                                        model.getId_bus(), model.getId_track(), model.getId_user(),
                                        loc.getLatitude(), loc.getLongitude()
                                ));
                            } else {
                                Constant.refTrack.push().setValue(new TrackModel(model.getId_bus(),
                                        1, Integer.parseInt(id), loc.getLatitude(), loc.getLongitude()));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            //handlePostion(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    String id_bus = "";
    String id = "";
    String email = "";


    private void loadProfile() {
        SharedPreferences prefs = getSharedPreferences("userSession", MODE_PRIVATE);
        String role = prefs.getString("role", "");
        email = prefs.getString("email", "");
        String name = prefs.getString("name", "");
        String phone = prefs.getString("phone", "");
        String image = prefs.getString("image", "");
        id = prefs.getString("id", "");
        id_bus = prefs.getString("id_bus", "");

        tv_id.setText(id);
        tv_name.setText(name);
        tv_email.setText(email);
        tv_bus_no.setText(id_bus);
        Picasso.get().load(image).into(img_user);
    }
}