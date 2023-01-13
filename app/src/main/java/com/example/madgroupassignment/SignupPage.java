package com.example.madgroupassignment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class SignupPage extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imagepath;
    private Bitmap imageToStore;

    private ProgressBar loadingProgress;
    private FirebaseAuth auth;
    static int Preqcode = 1;
    static final int RequestCode = 1;
    EditText signupuser, signupemail, signupphone, signuppassword, signuprepeatpassword;
    TextView haveaccount;
    Button register;
    ShapeableImageView profilephoto;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup_page);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        signupuser = findViewById(R.id.signup_username);
        signupemail = findViewById(R.id.signup_email);
        signupphone = findViewById(R.id.signup_phone);
        signuppassword = findViewById(R.id.signup_password);
        signuprepeatpassword = findViewById(R.id.signup_rpassword);
        haveaccount = findViewById(R.id.haveaccount);
        register = findViewById(R.id.registerbutton);
        profilephoto = findViewById(R.id.signup_photo);

        loadingProgress = findViewById(R.id.loadingprogress);
        loadingProgress.setVisibility(View.INVISIBLE);


        auth = FirebaseAuth.getInstance();


        haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginPage.class));
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);


                String username, email, password, repeatpassword, phone;
                username = signupuser.getText().toString();
                email = signupemail.getText().toString();
                password = signuppassword.getText().toString();
                repeatpassword = signuprepeatpassword.getText().toString();
                phone = signupphone.getText().toString();
                if (username.isEmpty()) {
                    showError(signupuser, "Please enter username");
                    register.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else if (username.length() < 7) {
                    showError(signupuser, "Username must be > 7 characters");
                    register.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else if (email.isEmpty()) {
                    showError(signupemail, "Please enter email");
                    register.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else if (!email.contains("@")) {
                    showError(signupemail, "Email is invalid (@)");
                    register.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else if (phone.isEmpty()) {
                    showError(signupphone, "Please enter phone");
                    register.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else if (password.isEmpty()) {
                    showError(signuppassword, "Please enter password");
                    register.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else if (password.length() < 7) {
                    showError(signuppassword, "Username must be > 7 characters");
                    register.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else if (repeatpassword.isEmpty()) {
                    showError(signuprepeatpassword, "Please repeat Password");
                    register.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else if (!password.equals(repeatpassword)) {
                    showError(signuprepeatpassword, "Password doesn't match");
                    register.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                } else if (password.equals(repeatpassword)
                        && profilephoto.getDrawable() != null && imagepath != null) {
                    createAccount(email, username, password,phone);
                    register.setVisibility(View.INVISIBLE);
                    loadingProgress.setVisibility(View.VISIBLE);

                } else
                    Toast.makeText(getApplicationContext(), "Please fill all the fields or Select Image", Toast.LENGTH_SHORT).show();
                register.setVisibility(View.VISIBLE);
                loadingProgress.setVisibility(View.INVISIBLE);

            }

        });
        profilephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkrequestPermission();
                } else {
                    opengallery();
                }
            }

        });


    }

    private void createAccount(final String email, String username, String password,String phone) {
        register.setVisibility(View.INVISIBLE);
        loadingProgress.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //user account created successfully
                            //     showMessage("Account Created Successfully");
                            //update user info
                            updateuserinfo(username,phone, imagepath, auth.getCurrentUser());
                            updateUI();

                        } else {
                            showMessage("Account Failed to Create" + task.getException().getMessage());
                            register.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);

                        }
                    }
                });

    }

    //update user profilephoto and username
         private void updateuserinfo(String username,String phone, Uri imagepickedUri, FirebaseUser currentUser) {
                StorageReference mstorage = FirebaseStorage.getInstance().getReference().child("users_photos");
                StorageReference imagefilepath = mstorage.child(imagepickedUri.getLastPathSegment() );
                imagefilepath.putFile(imagepickedUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagefilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override public void onSuccess(Uri uri) {
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .setPhotoUri(uri)
                                        .build();
                                currentUser.updateProfile(profileUpdate)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful())
                                           {
                                               showMessage("Registered Successfully");
                                               updateUI();
                                           }
                                            }
                                        });


                            }
                        });

                }

                });


    }



    private void checkrequestPermission() {
        if (ContextCompat.checkSelfPermission(SignupPage.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignupPage.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(SignupPage.this, "Allow app to access permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(SignupPage.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Preqcode);
            }

        } else {
            opengallery();
        }

    }

    private void opengallery() {


        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, RequestCode);


    }


    // Override onActivityResult method


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RequestCode &&data!= null&& data.getData()!=null)
        {
            imagepath = data.getData();
            profilephoto.setImageURI(imagepath);
        }

    }

    private void updateUI() {

        Intent intent = new Intent(SignupPage.this, LoginPage.class);
        startActivity(intent);
        finish();


    }


    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
    private void showMessage(String Msg)
    {
        Toast.makeText(getApplicationContext(), Msg, Toast.LENGTH_SHORT).show();
    }
}