package com.example.vikar.publicbustracking.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.vikar.publicbustracking.R;
import com.example.vikar.publicbustracking.fragment.BusTracking;
import com.example.vikar.publicbustracking.fragment.Maps;
import com.example.vikar.publicbustracking.fragment.ProfileUser;
import com.example.vikar.publicbustracking.fragment.Route;
import com.example.vikar.publicbustracking.ui.BottomNavigationViewHelper;

public class Menu extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, Route.newInstance());
        transaction.commit();
    }

}
