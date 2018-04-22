package com.example.vikar.publicbustracking.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vikar.publicbustracking.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by vikar on 10-Jan-18.
 */

public class login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button btnlogin;
    private EditText txtusername;
    private EditText txtpassword;
    private TextView btnsignup;
    private CheckBox check;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pDialog = new ProgressDialog(this);
        btnlogin=(Button) findViewById(R.id.btnlogin);
        txtusername=(EditText) findViewById(R.id.txtusername);
        txtpassword=(EditText) findViewById(R.id.txtpassword);
        btnsignup=(TextView) findViewById(R.id.btnsignup);
        check=(CheckBox) findViewById(R.id.showpasslogin);

        mAuth = FirebaseAuth.getInstance();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtusername.getText().toString().isEmpty()||txtpassword.getText().toString().isEmpty()){
                    Toast.makeText(login.this, "Complete Username & Password Corectly", Toast.LENGTH_SHORT).show();
                    return;
                }

                pDialog.setMessage("Please wait..");
                pDialog.show();
                String email = txtusername.getText().toString();
                String password = txtpassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("", "signInWithEmail:success");
                                    pDialog.dismiss();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startActivity(new Intent(login.this, MainActivity.class));
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(login.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this,signup.class));
            }
        });

        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(!ischecked)
                {
                    txtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                else
                {
                    txtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser CurrentUser = mAuth.getCurrentUser();
        if(CurrentUser!=null){
            startActivity(new Intent(login.this,MainActivity.class));
            return;
        }
    }
}
