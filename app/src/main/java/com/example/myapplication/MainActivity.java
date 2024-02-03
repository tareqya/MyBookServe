package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView home_BN;
    private FrameLayout home_frame_category, home_frame_home, home_frame_profile;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private CategoryFragment categoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        initVars();
    }

    private void initVars() {
        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        categoryFragment = new CategoryFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.home_frame_home, homeFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.home_frame_profile, profileFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.home_frame_category, categoryFragment).commit();

        home_frame_category.setVisibility(View.INVISIBLE);
        home_frame_profile.setVisibility(View.INVISIBLE);
        home_frame_home.setVisibility(View.VISIBLE);

        home_BN.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.home){
                    home_frame_category.setVisibility(View.INVISIBLE);
                    home_frame_profile.setVisibility(View.INVISIBLE);
                    home_frame_home.setVisibility(View.VISIBLE);
                }else if(item.getItemId() == R.id.profile){
                    home_frame_category.setVisibility(View.INVISIBLE);
                    home_frame_profile.setVisibility(View.VISIBLE);
                    home_frame_home.setVisibility(View.INVISIBLE);
                }else if(item.getItemId() == R.id.category){
                    home_frame_category.setVisibility(View.VISIBLE);
                    home_frame_profile.setVisibility(View.INVISIBLE);
                    home_frame_home.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }

    private void findViews() {
        home_frame_category = findViewById(R.id.home_frame_category);
        home_frame_home = findViewById(R.id.home_frame_home);
        home_frame_profile = findViewById(R.id.home_frame_profile);
        home_BN = findViewById(R.id.home_BN);

    }

}
