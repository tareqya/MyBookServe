package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myapplication.calback.UserCallBack;
import com.example.myapplication.data.User;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView home_BN;
    private FrameLayout home_frame_category, home_frame_home, home_frame_profile;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private AddBookFragment addBookFragment;
    private Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initVars();
        db = new Database();
        fetchCurrentUserData();
        if(!checkPermissions()){
            requestPermissions();
        }
    }

    private void fetchCurrentUserData() {
        db.setUserCallBack(new UserCallBack() {
            @Override
            public void onUserSaveDataComplete(Task<Void> task) {

            }

            @Override
            public void onUserFetchDataComplete(User user) {
                profileFragment.setUser(user);
            }
        });

        String uid = db.getCurrentUser().getUid();
        db.fetchUserByUid(uid);
    }

    private void initVars() {
        homeFragment = new HomeFragment(this);
        profileFragment = new ProfileFragment(this);
        addBookFragment = new AddBookFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.home_frame_home, homeFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.home_frame_profile, profileFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.home_frame_category, addBookFragment).commit();

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
                }else if(item.getItemId() == R.id.addBook){
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

    public  boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        android.Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                },
                100
        );
    }
}
