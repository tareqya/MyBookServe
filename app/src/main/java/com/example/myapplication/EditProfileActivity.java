package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.myapplication.calback.UserCallBack;
import com.example.myapplication.data.User;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editName, editPhone;
    private CircleImageView profileEdit_image;
    private Button saveButton;
    private FloatingActionButton profileEdit_FBTN_uploadImage;
    private Uri selectedImageUri;
    private Database database;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent = getIntent();
        currentUser = (User) intent.getSerializableExtra("USER");
        findViews();
        initVars();

        showData();

    }

    private void showData() {
        editName.setText(currentUser.getName());
        editPhone.setText(currentUser.getPhone());
        if(currentUser.getImageUrl() != null){
            Glide.with(this).load(currentUser.getImageUrl()).into(profileEdit_image);
        }
    }

    private void initVars() {
        database = new Database();
        database.setUserCallBack(new UserCallBack() {
            @Override
            public void onUserSaveDataComplete(Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(EditProfileActivity.this, "User data updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    String err = task.getException().getMessage().toString();
                    Toast.makeText(EditProfileActivity.this, err, Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onUserFetchDataComplete(User user) {

            }
        });

        profileEdit_FBTN_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceDialog();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editName.getText().toString().isEmpty()){
                    Toast.makeText(EditProfileActivity.this, "name is requried!", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentUser.setName(editName.getText().toString());
                currentUser.setPhone(editPhone.getText().toString());

                if(selectedImageUri != null){
                    String ext = Generic.getFileExtension(EditProfileActivity.this, selectedImageUri);
                    String imagePath = "Users/"+database.getCurrentUser().getUid() + "." + ext;
                    if(database.uploadImage(selectedImageUri, imagePath)){
                        currentUser.setImagePath(imagePath);
                    }else{
                        return;
                    }
                }

                database.saveUserData(currentUser);
            }
        });
    }

    private void findViews() {
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        saveButton = findViewById(R.id.saveButton);
        profileEdit_image = findViewById(R.id.profileEdit_image);
        profileEdit_FBTN_uploadImage = findViewById(R.id.profileEdit_FBTN_uploadImage);

    }

    private void showImageSourceDialog() {
        CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileActivity.this);
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
                                final InputStream imageStream = EditProfileActivity.this.getContentResolver().openInputStream(selectedImageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                profileEdit_image.setImageBitmap(selectedImage);
                            }
                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Toast.makeText(EditProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(EditProfileActivity.this, "Gallery canceled", Toast.LENGTH_SHORT).show();
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
                            profileEdit_image.setImageBitmap(bitmap);
                            selectedImageUri = Generic.getImageUri(EditProfileActivity.this, bitmap);
                            break;
                        case Activity.RESULT_CANCELED:
                            Toast.makeText(EditProfileActivity.this, "Camera canceled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });
}


