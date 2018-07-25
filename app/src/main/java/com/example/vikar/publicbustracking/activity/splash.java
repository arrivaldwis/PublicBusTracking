package com.example.vikar.publicbustracking.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.vikar.publicbustracking.R;
import com.crashlytics.android.Crashlytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import io.fabric.sdk.android.Fabric;

public class splash extends AppCompatActivity {

    ImageView imgLogo;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        imgLogo = (ImageView) findViewById(R.id.splashid);

        StartAnimation();
        Thread splash = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(splash.this, login.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }
            }
        };
        splash.start();
    }

    private void StartAnimation() {
        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.showin);
        anim1.setStartOffset(800);

        imgLogo.startAnimation(anim1);
    }

}

