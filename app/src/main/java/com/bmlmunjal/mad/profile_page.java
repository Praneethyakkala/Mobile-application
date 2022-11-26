package com.bmlmunjal.mad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bmlmunjal.mad.databinding.ActivityProfilePageBinding;
import com.bmlmunjal.mad.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class profile_page extends AppCompatActivity implements View.OnClickListener{

    ActivityProfilePageBinding binding;
    ImageView backbutton;
    EditText editTextUserName;
    EditText editTextEmailId;
    public static String fName,lName,phoneNo,emailId;
    ImageView imageViewProfilePicture;
    private final static int galleryRequestCode =69;

    private FirebaseAuth mAuth;
    private DatabaseReference fDatabase;
    FirebaseDatabase database;
    FirebaseStorage storage;

    private StorageReference storageReference;
    private StorageReference profileReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityProfilePageBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        fDatabase = FirebaseDatabase.getInstance().getReference("Users");
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        backbutton = findViewById(R.id.backButtonProfilePage);
        editTextUserName = findViewById(R.id.editTextUserName);
        editTextEmailId = findViewById(R.id.editTextEmailId);
        profileReference = storageReference.child("Users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");




        emailId = mAuth.getCurrentUser().getEmail().toString().trim();
        editTextEmailId.setText(emailId);
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("panee");
        Log.v("USERID",userRef.getKey());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyId: snapshot.getChildren()) {
                    if (keyId.child("email").getValue().equals(emailId)) {
                        fName = keyId.child("name").getValue(String.class);
//                        lName = keyId.child("lName").getValue(String.class);
//                        phoneNo = keyId.child("phoneNumber").getValue(String.class);
                        Log.v("USERID","Hello!");
                        break;
                    }
                }
                editTextUserName.setText(fName);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Failed to read values
                Log.w("UserRef", "Failed to read User Values ",error.toException() );
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case (R.id.backButtonProfilePage):
                        Intent intent=new Intent(profile_page.this,navbar_Activity.class);
                        startActivity(intent);
                }
            }
        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,33);

            }
        });

        binding.saveButtonProfilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String about = binding.status.getText().toString();
                String username = binding.editTextUserName.getText().toString();
                String email = binding.editTextEmailId.getText().toString();

                HashMap<String,Object> obj = new HashMap<>();
                obj.put("userName", username);
                obj.put("about", about);
                obj.put("mail", email);

                database.getReference().child("User")
                        .child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(obj);

                Toast.makeText(profile_page.this,"Updated Successfully",Toast.LENGTH_SHORT).show();
            }
        });


        database.getReference().child("User").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfilepic())
                                .placeholder(R.drawable.img)
                                .into(binding.imageViewProfilePic);

                        binding.editTextEmailId.setText(user.getMail());
                        binding.editTextUserName.setText(user.getUserName());
                        binding.status.setText(user.getAbout());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    public static void saveToDatabase(){

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.saveButtonProfilePage):
                saveToDatabase();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData() != null){

            Uri sFile = data.getData();
            binding.imageViewProfilePic.setImageURI(sFile);
            final StorageReference reference = storage.getReference()
                    .child("profile pictures")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("User")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child("profilepic")
                                    .setValue(uri.toString());
                            Toast.makeText(profile_page.this,"Profile is Updated",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}