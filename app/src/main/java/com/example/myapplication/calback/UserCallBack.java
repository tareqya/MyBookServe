package com.example.myapplication.calback;

import com.google.android.gms.tasks.Task;

public interface UserCallBack {
    void onUserSaveDataComplete(Task<Void> task);
}
