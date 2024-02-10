package com.example.myapplication.calback;

import com.example.myapplication.data.User;
import com.google.android.gms.tasks.Task;

public interface UserCallBack {
    void onUserSaveDataComplete(Task<Void> task);
    void onUserFetchDataComplete(User user);
}
