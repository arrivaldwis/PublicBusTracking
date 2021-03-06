package com.example.vikar.publicbustracking.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.activity.login;
import com.example.vikar.publicbustracking.activity.signup;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class ProfileUser extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button btnLogout;
    private EditText tv_id;
    private EditText tv_name;
    private EditText tv_email;
    private EditText tv_phone;
    public static ImageView img_user;

    public ProfileUser() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfileUser newInstance() {
        ProfileUser fragment = new ProfileUser();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_user, container, false);
        btnLogout = (Button) v.findViewById(R.id.btnlogoutdriver);
        tv_id = (EditText) v.findViewById(R.id.tv_id);
        tv_name = (EditText) v.findViewById(R.id.tv_name);
        tv_email = (EditText) v.findViewById(R.id.tv_email);
        tv_phone = (EditText) v.findViewById(R.id.tv_phone);
        img_user = (ImageView) v.findViewById(R.id.img_user);

        btnLogout.setOnClickListener(this);
        img_user.setOnClickListener(this);
        loadProfile();
        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnlogoutdriver:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), login.class));
                getActivity().finish();
                break;
            case R.id.img_user:
                ImagePicker.create(getActivity())
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
                break;
        }
    }

    private void loadProfile() {
        SharedPreferences prefs = getActivity().getSharedPreferences("userSession", MODE_PRIVATE);
        String role = prefs.getString("role", "");
        String email = prefs.getString("email", "");
        String id = prefs.getString("id", "");
        String name = prefs.getString("name", "");
        String phone = prefs.getString("phone", "");
        String image = prefs.getString("image", "");

        tv_id.setText(id);
        tv_name.setText(name);
        tv_phone.setText(phone);
        tv_email.setText(email);
        Picasso.get().load(image).into(img_user);
    }
}
