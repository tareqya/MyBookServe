package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.calback.BookCallBack;
import com.example.myapplication.data.Book;
import com.google.android.gms.tasks.Task;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class AddBookFragment extends Fragment {
    public final int SCORE_PER_BOOK = 100;
    public static String[] categories = {
            "Romance",
            "Horror",
            "Adventure",
            "Travel"};
    private ImageView uploadImage;
    private EditText bookName;
    private EditText bookDesc;
    private EditText bookLang;
    private Spinner bookCategory;
    private Button saveButton;
    private Context context;
    private Database database;
    private Uri selectedImageUri;


    public AddBookFragment(Context context) {
       this.context = context;
       this.database = new Database();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_book, container, false);
        findViews(root);
        initVars();
        return root;
    }

    private void findViews(View root) {
        uploadImage = root.findViewById(R.id.uploadImage);
        bookName = root.findViewById(R.id.bookName);
        bookDesc = root.findViewById(R.id.bookDesc);
        bookLang = root.findViewById(R.id.bookLang);
        saveButton = root.findViewById(R.id.saveButton);
        bookCategory = root.findViewById(R.id.bookCategory);
    }

    private void initVars() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categories);
        bookCategory.setAdapter(adapter);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceDialog();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkInput()){
                    Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveBook();
            }
        });


        database.setBookCallBack(new BookCallBack() {
            @Override
            public void onFetchBooksComplete(ArrayList<Book> books) {

            }

            @Override
            public void onBookUploadComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Book uploaded successfully", Toast.LENGTH_SHORT).show();
                    String uid = database.getCurrentUser().getUid();
                    database.updateUserScore(uid, SCORE_PER_BOOK);
                    cleanUI();
                }else{
                    String err = task.getException().getMessage().toString();
                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cleanUI() {
        bookLang.setText("");
        bookDesc.setText("");
        bookName.setText("");
        selectedImageUri = null;
        uploadImage.setImageResource(R.drawable.upload__2_);
    }

    private void saveBook() {
        String category = bookCategory.getSelectedItem().toString();
        Random random = new Random();
        int number = random.nextInt(100000) + 10000;
        String uid = database.getCurrentUser().getUid();
        String ext = Generic.getFileExtension(((Activity) context), selectedImageUri);
        String imagePath = "Books/"+uid+"_"+number+"."+ext;
        database.uploadImage(selectedImageUri, imagePath);
        Random rnd = new Random();
        int rate = rnd.nextInt(4-1)+1;
        Book book = new Book()
                .setRate(rate)
                .setCategory(category)
                .setImagePath(imagePath)
                .setDescription(bookDesc.getText().toString())
                .setName(bookName.getText().toString())
                .setLanguage(bookLang.getText().toString());
        database.saveBook(book);
    }

    private boolean checkInput() {
        if(bookName.getText().length() == 0){
            return false;
        }
        if(bookDesc.getText().length() == 0){
            return false;
        }

        if(bookLang.getText().length() == 0){
            return false;
        }
        if(selectedImageUri == null){
            return false;
        }
        return true;
    }
    private void showImageSourceDialog() {
        CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Image Source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                switch (which) {
                    case 0:
                        openCamera();
                        break;
                    case 1:
                        openGallery();
                        break;
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraResults.launch(intent);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallery_results.launch(intent);
    }
    private final ActivityResultLauncher<Intent> gallery_results = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            try {
                                Intent intent = result.getData();
                                selectedImageUri = intent.getData();
                                final InputStream imageStream = context.getContentResolver().openInputStream(selectedImageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                uploadImage.setImageBitmap(selectedImage);
                            }
                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(context, "Gallery canceled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });

    private final ActivityResultLauncher<Intent> cameraResults = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    switch (result.getResultCode()) {
                        case Activity.RESULT_OK:
                            Intent intent = result.getData();
                            Bitmap bitmap = (Bitmap)  intent.getExtras().get("data");
                            uploadImage.setImageBitmap(bitmap);
                            selectedImageUri = Generic.getImageUri(((Activity)context), bitmap);
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(context, "Camera canceled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
}