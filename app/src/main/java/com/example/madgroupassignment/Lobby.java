package com.example.madgroupassignment;

import static com.example.madgroupassignment.SignupPage.Preqcode;
import static com.example.madgroupassignment.SignupPage.RequestCode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.madgroupassignment.Adapters.PostAdapter;
import com.example.madgroupassignment.Models.Post;
import com.example.madgroupassignment.databinding.ActivityLobbyBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;


public class Lobby extends DrawerBaseAcitivity {
ActivityLobbyBinding activityLobbyBinding;

FirebaseAuth auth;
FirebaseUser currentuser;
Dialog popAddPost;
ImageView popuppostimage;
ShapeableImageView popupuserimage;
EditText popuptitle,popupdescription;
ProgressBar popupprogress;
ImageButton createpost;
private Uri imagepath;


RecyclerView postrecyclerview;
ArrayList<Post> list;
PostAdapter postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   /*  *//*   Button testingbutton;*//*
        ImageButton lobbypostbutton;


        activityLobbyBinding = ActivityLobbyBinding.inflate(getLayoutInflater());
        allocateActivityTitle("LOBBY");*/
        super.setContentView(R.layout.activity_lobby);
/*        lobbypostbutton = findViewById(R.id.lobbypostbutton);
        *//* testingbutton =(Button) findViewById(R.id.testbutton);*//*
        postrecyclerview = findViewById(R.id.recyclerview_posts);

        auth = FirebaseAuth.getInstance();
        currentuser = auth.getCurrentUser();

        //ini popup

        inpopup();
     *//*   initrecyclerview();*//*
*//*
        testingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseDatabase_location);
                DatabaseReference myRef = database.getReference("testing");
                myRef.setValue("is firebase working");
                testingbutton.setText("CHanged");
            }
        });

*//*

        lobbypostbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.show();
            }
        });

        initrecyclerview();


    }

    private void initrecyclerview() {
       FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(firebaseDatabase_location   );
       DatabaseReference databaseReference = firebaseDatabase.getReference("Posts");

       list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        postrecyclerview.setLayoutManager(manager);
        postrecyclerview.setHasFixedSize(true);
       postAdapter = new PostAdapter(this,list);
       postrecyclerview.setAdapter(postAdapter);
       databaseReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot dataSnapshot : snapshot.getChildren())
               {
                   Post post = dataSnapshot.getValue(Post.class);
                    list.add(post);

               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
            postAdapter.notifyDataSetChanged();
    }


    private void inpopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;
        //popop widgets
        popupuserimage = popAddPost.findViewById(R.id.profilepost);
        popuppostimage = popAddPost.findViewById(R.id.postlayout);
        popupprogress = popAddPost.findViewById(R.id.progresspost);
        popuptitle = popAddPost.findViewById(R.id.titlepost);
        popupdescription = popAddPost.findViewById(R.id.descpost);
        createpost = popAddPost.findViewById(R.id.createpost);

        //load user profile photo
        Glide.with(this).load(currentuser.getPhotoUrl()).into(popupuserimage);

        //Add post click listener
        createpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progresssuccess();

                String title,description;
                title = popuptitle.getText().toString();
                description = popupdescription.getText().toString();

                if(!title.equals("") && !description.equals("") && imagepath != null)
                {
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                   final StorageReference imagefilepath = storageReference.child(imagepath.getLastPathSegment());

                    imagefilepath.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagefilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageDownloadURL = uri.toString();
                                    //create post Object
                                    Post post = new Post(
                                            title,
                                            description,
                                            imageDownloadURL,
                                            currentuser.getUid(),
                                            currentuser.getPhotoUrl().toString());
                                    //Add [post] to firebase database
                                   // addPost(post);




                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // upload post error
                                    showMessage(e.getMessage());
                                    progressfailure();
                                }
                            });
                        }
                    });
                }
                else
                {
                    showMessage("Please enter all field and choose an image");
                    progressfailure();
                }

            }
        });
        //choose image to be posted
        popuppostimage.setOnClickListener(new View.OnClickListener() {
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

   *//* private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance(firebaseDatabase_location);
        DatabaseReference myRef = database.getReference("Posts").push();


        //get post id and update post key
        String key = myRef.getKey();
        post.setPostkey(key);

        //add post data to firebase database
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showMessage("Post Created ");
                progressfailure();
                popAddPost.dismiss();
            }
        });
    }*//*


    private void opengallery() {


        Intent galleryintent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, RequestCode);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == RequestCode &&data!= null&& data.getData()!=null)
        {
            imagepath = data.getData();
            popuppostimage.setImageURI(imagepath);
        }

    }
    private void checkrequestPermission() {
        if (ContextCompat.checkSelfPermission(Lobby.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Lobby.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Lobby.this, "Allow app to access permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(Lobby.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        Preqcode);
            }

        } else {
            opengallery();
        }

    }
    private void showMessage(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void progresssuccess()
    {
        popupprogress.setVisibility(View.VISIBLE);
        createpost.setVisibility(View.INVISIBLE);
    }
    public void progressfailure()
    {
        popupprogress.setVisibility(View.INVISIBLE);
        createpost.setVisibility(View.VISIBLE);
    }*/
}
}