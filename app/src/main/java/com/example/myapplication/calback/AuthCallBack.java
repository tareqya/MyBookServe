package com.example.myapplication.calback;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface AuthCallBack {
    void onLoginComplete(Task<AuthResult> task);
    void onCreateAccountComplete(Task<AuthResult> task);
}
