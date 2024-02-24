package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.calback.BookCallBack;
import com.example.myapplication.data.Book;
import com.example.myapplication.data.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddComentActivity extends AppCompatActivity {

    private Button saveReviewButton;
    private EditText uploadReviewDesc;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coment);

        Intent intent = getIntent();
        Book book = (Book) intent.getSerializableExtra("BOOK");

        database = new Database();
        database.setBookCallBack(new BookCallBack() {
            @Override
            public void onFetchBooksComplete(ArrayList<Book> books) {

            }

            @Override
            public void onBookUploadComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(AddComentActivity.this, "comment saved successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    String err = task.getException().getMessage().toString();
                    Toast.makeText(AddComentActivity.this, err, Toast.LENGTH_SHORT).show();
                }
            }
        });


        uploadReviewDesc = findViewById(R.id.uploadDesc);
        saveReviewButton = findViewById(R.id.saveButton);

        saveReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadReviewDesc.getText().toString().isEmpty()){
                    Toast.makeText(AddComentActivity.this, "Your opinion is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Comment comment = new Comment();
                comment.setOpinion(uploadReviewDesc.getText().toString());
                comment.setUid(database.getCurrentUser().getUid());
                book.addComment(comment);
                database.saveBook(book);
            }
        });
    }




}
