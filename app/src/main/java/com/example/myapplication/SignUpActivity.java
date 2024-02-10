package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.calback.AuthCallBack;
import com.example.myapplication.calback.UserCallBack;
import com.example.myapplication.data.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText signupName, signupEmail, signupPhone, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupPhone = findViewById(R.id.signup_phone);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);

        database = new Database();

        database.setUserCallBack(new UserCallBack() {
            @Override
            public void onUserSaveDataComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    String err = task.getException().getMessage().toString();
                    Toast.makeText(SignUpActivity.this, err, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onUserFetchDataComplete(User user) {

            }
        });

        database.setAuthCallBack(new AuthCallBack() {
            @Override
            public void onLoginComplete(Task<AuthResult> task) {

            }

            @Override
            public void onCreateAccountComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String name = signupName.getText().toString();
                    String email = signupEmail.getText().toString();
                    String phone = signupPhone.getText().toString();
                    User user = new User()
                            .setEmail(email)
                            .setPhone(phone)
                            .setName(name);
                    String uid = database.getCurrentUser().getUid();
                    user.setKey(uid);
                    database.saveUserData(user);
                }else{
                    String err = task.getException().getMessage().toString();
                    Toast.makeText(SignUpActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = signupName.getText().toString();
                String email = signupEmail.getText().toString();
                String phone = signupPhone.getText().toString();
                String password = signupPassword.getText().toString();

                database.createNewAccount(email, password);
            }
        });

        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}