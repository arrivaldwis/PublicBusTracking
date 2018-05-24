package com.example.vikar.publicbustracking.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.vikar.publicbustracking.Constant;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button btnlogout;
    private String email;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pDialog = new ProgressDialog(this);
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        btnlogout = (Button) findViewById(R.id.btnlogout);
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, login.class));
                finish();
            }
        });

        pDialog.setMessage("Redirecting..");
        pDialog.show();
        checkUser();
    }

    String role = "";

    private void checkUser() {
        Constant.refUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserModel user = ds.getValue(UserModel.class);
                    if (user.getEmail().equals(email)) {
                        role = user.getRole();
                        SharedPreferences.Editor editor = getSharedPreferences("userSession", MODE_PRIVATE).edit();
                        editor.putString("email", email);
                        editor.putString("role", role);
                        editor.putString("id", user.getId_user() + "");
                        editor.putString("image", user.getImage() + "");
                        editor.putString("name", user.getName());
                        editor.putString("phone", user.getPhone() + "");
                        if (role.equals("driver"))
                            editor.putString("id_bus", user.getId_bus() + "");
                        editor.apply();
                    }
                }

                if (!role.isEmpty()) {
                    Log.d("roleOutput", role);
                    //Toast.makeText(MainActivity.this, role, Toast.LENGTH_SHORT).show();
                    loadUI(role);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadUI(String role) {
        if (role.equals("user"))
            startActivity(new Intent(MainActivity.this, Menu.class));
        if (role.equals("driver"))
            startActivity(new Intent(MainActivity.this, Driver.class));

        finish();
    }
}
