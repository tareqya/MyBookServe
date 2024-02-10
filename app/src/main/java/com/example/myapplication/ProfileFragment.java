package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myapplication.data.User;

public class ProfileFragment extends Fragment {
    private User currentUser;
    private ImageView userImageView;
    private TextView usernameTextView;
    private TextView nameTextView;
    private TextView scoreTextView;
    private Button updateProfileButton;
    private Button logoutButton;
    private Context context;
    private Database database;

    public ProfileFragment(Context context) {
        // Required empty public constructor
        this.context = context;
        database = new Database();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(root);
        initVars();
        return root;
    }

    private void findViews(View root) {
        userImageView = root.findViewById(R.id.userImageView);
        usernameTextView = root.findViewById(R.id.usernameTextView);
        nameTextView = root.findViewById(R.id.nameTextView);
        scoreTextView = root.findViewById(R.id.scoreTextView);
        updateProfileButton = root.findViewById(R.id.updateProfileButton);
        logoutButton = root.findViewById(R.id.logoutButton);


    }

    private void initVars() {
        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.logout();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                ((Activity)context).finish();
            }
        });
    }

    public void setUser(User user) {
        currentUser = user;
        displayUser();
    }

    private void displayUser() {
        if(currentUser.getImageUrl() != null){
            Glide.with(context).load(currentUser.getImageUrl()).into(userImageView);
        }

        nameTextView.setText(currentUser.getName());
        usernameTextView.setText(currentUser.getEmail());
        scoreTextView.setText(currentUser.getScore() + "");

    }
}