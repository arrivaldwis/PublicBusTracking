package com.example.vikar.publicbustracking.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.example.vikar.publicbustracking.Constant;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class signup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnlogin;
    private EditText txtusername;
    private EditText txtemail;
    private EditText txtphone;
    private EditText txtpassword;
    private CheckBox check;
    private ProgressDialog pDialog;
    private ImageView img_user;
    private StorageReference refPhotoProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        pDialog = new ProgressDialog(this);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        txtusername = (EditText) findViewById(R.id.txtusername);
        txtemail = (EditText) findViewById(R.id.txtemail);
        txtphone = (EditText) findViewById(R.id.txtphone);
        txtpassword = (EditText) findViewById(R.id.txtpassword);
        img_user = (ImageView) findViewById(R.id.img_user);
        check = (CheckBox) findViewById(R.id.showpass);

        mAuth = FirebaseAuth.getInstance();
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if (!ischecked) {
                    txtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    txtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

            }
        });

        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.create(signup.this)
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
                        .start(); // start image picker activity with request code
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtusername.getText().toString().isEmpty() || txtpassword.getText().toString().isEmpty()) {
                    Toast.makeText(signup.this, "Complete Username & Password Corectly", Toast.LENGTH_SHORT).show();
                    return;
                }

                pDialog.setMessage("Please wait..");
                pDialog.show();
                final String email = txtemail.getText().toString();
                final String password = txtpassword.getText().toString();
                final String name = txtusername.getText().toString();
                final String phone = txtphone.getText().toString();

                if(isPic) {
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

                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d("", "createUserWithEmail:success");
                                                pDialog.dismiss();
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                Long id = System.currentTimeMillis();
                                                Constant.refUser.push().setValue(new UserModel(id, email, name, "user", photoUrl.toString(), 0, phone));
                                                Toast.makeText(signup.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                                                finish();

                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w("", "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(signup.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();

                                            }

                                            // ...
                                        }
                                    });
                        }
                    });
                }
            }
        });
    }

    boolean isPic = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            com.esafirm.imagepicker.model.Image image = ImagePicker.getFirstImageOrNull(data);
            File imgFile = new File(image.getPath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                img_user.setImageBitmap(myBitmap);
                isPic = true;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
