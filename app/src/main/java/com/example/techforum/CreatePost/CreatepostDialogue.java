package com.example.techforum.CreatePost;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.techforum.R;
import com.example.techforum.posts;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class CreatepostDialogue extends AppCompatDialogFragment {
    Button post;
    EditText description;
    private String profileimagefromfirebase;
    TextView photos;
    ImageView picked,profile;
    FirebaseUser muser;
    TextView username;
    String time;
    int x=0;
    String uid;
    Uri imageuri,uploadedurl;
    String postid;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseStorage storage;
    StorageReference storageReference;

    ActivityResultLauncher<String> activityResultLauncher;
    private static final int PICK_IMAGE = 100;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog builder= new Dialog(getContext(),android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth);
        LayoutInflater layoutInflater=getActivity().getLayoutInflater();
        View view= layoutInflater.inflate(R.layout.createpostdialogue,null);
        post=view.findViewById(R.id.create_post_post);
        description=view.findViewById(R.id.create_post_description);
        picked= view.findViewById(R.id.create_post_pic);
        profile=view.findViewById(R.id.create_post_profilePic);
        username=view.findViewById(R.id.create_post_username);
        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("postDetails");
        muser= FirebaseAuth.getInstance().getCurrentUser();
         uid=muser.getPhoneNumber();
//        Toast.makeText(getContext(),uid, Toast.LENGTH_SHORT).show();
        DatabaseReference dref= FirebaseDatabase.getInstance().getReference().child("userDetails");

        DatabaseReference postref=FirebaseDatabase.getInstance().getReference().child("posts");
        getuserinfo(dref);

//        builder.setView(view).setTitle("POST").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
        post.setOnClickListener(new View. OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                time= Calendar.getInstance().getTime().toString();
                time=time.substring(0,20);
                postid=uid+time;
//                dref.child(uid).child("postid").setValue(postid);
                    posts postDetails= new posts();
                String s=description.getText().toString();
                if(s.isEmpty()&&x==0)
                    Toast.makeText(getContext(), "Type something", Toast.LENGTH_SHORT).show();
                else
                {
                    postDetails.setCaption(s);
                    postDetails.setTime(Calendar.getInstance().getTime().toString().substring(0,20));
                    postDetails.setProfilePic("");
                    postDetails.setUserName(username.getText().toString());
                    if(imageuri!=null)
                    uploadImage(imageuri);
//                    postDetails.setPostImage("def");
                    setpostinfo(postref,postDetails);
//                    Toast.makeText(getContext(), "sent Info to Firebase", Toast.LENGTH_SHORT).show();
                    x=0;
                }


            }

            private void uploadImage(Uri imageuri) {

                ProgressDialog progressDialog
                        = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                StorageReference ref= storageReference.child("images/");
                StorageReference filename=ref.child("file"+imageuri.getLastPathSegment());
//                StorageReference refernce= storage.getReference().child("images/");
                filename.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Posted Successfully",Toast.LENGTH_SHORT).show();
                        filename.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
//                                Toast.makeText(getContext(), uploadedurl+"here", Toast.LENGTH_SHORT).show();
                                postref.child(postid).child("postImage").setValue(uri.toString());

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to upload!!",Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress
                                = (100.0
                                * taskSnapshot.getBytesTransferred()
                                / taskSnapshot.getTotalByteCount());
                        if((int)progress==100)
                        {builder.dismiss();
                        progressDialog.dismiss();}
                        progressDialog.setMessage("Uploaded "+ (int)progress + "%");
                    }
                });

            }

            public void setpostinfo(DatabaseReference postref,posts fp) {

//                dref.child(uid).child("postid").setValue(postid);
                reference.child(postid).child("likes").setValue("0");
                postref.child(postid).setValue(fp);

            }
        });
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.GetContent(),new ActivityResultCallback<Uri>(){
            @Override
            public void onActivityResult(Uri result) {
                imageuri=result;
                picked.setImageURI(result);
                x=1;
            }
        });
        picked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityResultLauncher.launch("image/*");

            }
        });
        builder.setContentView(view);
        builder.show();
return builder;
    }

    private void getuserinfo(DatabaseReference dref) {
        dref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name=snapshot.child(uid).child("name").getValue(String.class)+"";
                String uri=snapshot.child(uid).child("profilePic").getValue(String.class)+"";
                username.setText(name);
                profileimagefromfirebase=uri;


                Picasso.get().load(uri).into(profile);

//                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

