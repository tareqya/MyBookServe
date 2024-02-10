package com.example.myapplication.calback;

import com.example.myapplication.data.Book;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public interface BookCallBack {
    void onFetchBooksComplete(ArrayList<Book> books);
    void onBookUploadComplete(Task<Void> task);
}
