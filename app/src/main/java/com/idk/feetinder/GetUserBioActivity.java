package com.idk.feetinder;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class GetUserBioActivity extends AppCompatActivity {

    private Button finishButton;
    private EditText enterName;
    private EditText enterBio;
    private Button back;
    private ImageView profilePic;
    private Uri imageUri;
    private FirebaseAuth auth;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_bio);

        finishButton = findViewById(R.id.finish);
        enterName = findViewById(R.id.enter_name);
        enterBio = findViewById(R.id.enter_bio);
        back = findViewById(R.id.back);
        profilePic = findViewById(R.id.profile_picture);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        String userId = auth.getCurrentUser().getUid();

        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference();

        currentUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String existingName = (String) snapshot.child("Users").child(userId).child("Name").getValue();
                String existingBio = (String) snapshot.child("Users").child(userId).child("Bio").getValue();

                if(existingName != null){
                    enterName.setText(existingName);
                } else {
                    back.setVisibility(View.INVISIBLE);
                }

                if(existingBio != null){
                    enterBio.setText(existingBio);
                }

                /* Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");

                if(intent.resolveActivity(getPackageManager()) != null){
                    Uri existingPfp = Uri.parse((String) snapshot.child("Users").child(userId).child("ProfilePicture").getValue());
                    if(existingPfp != null){
                        profilePic.setImageURI(existingPfp);
                        Toast.makeText(GetUserBioActivity.this, "retrieving pic", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(GetUserBioActivity.this, "uh oh", Toast.LENGTH_SHORT).show();
                } */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GetUserBioActivity.this, "read failed: " + error.getCode(), Toast.LENGTH_SHORT);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = enterName.getText().toString();
                String bio = enterBio.getText().toString();

                if(isValidNameBio(name, bio)){
                    DatabaseReference currentUserName = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(userId).child("Name");

                    currentUserName.setValue(name);

                    DatabaseReference currentUserBio = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(userId).child("Bio");

                    //DatabaseReference currentUserPfp = FirebaseDatabase.getInstance().getReference()
                            //.child("Users").child(userId).child("ProfilePicture");

                    currentUserBio.setValue(bio);
                    //currentUserPfp.setValue(imageUri.toString());

                    Intent intent = new Intent(GetUserBioActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPicture();
            }
        });
    }

    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randKey = UUID.randomUUID().toString();
        StorageReference ref = storageReference.child("images/" + randKey);
        ref.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(GetUserBioActivity.this, "Image Upload Failed", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double percent = (100.00*snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage((int) percent + "%");
            }
        });
    }

    private boolean isValidNameBio(String name, String bio) {
        if(name.matches(("^.*[^a-zA-Z0-9 ].*$"))){
            Toast.makeText(GetUserBioActivity.this, "invalid characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(name.length() < 1){
            Toast.makeText(GetUserBioActivity.this, "enter a name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(name.length() > 100){
            Toast.makeText(GetUserBioActivity.this, "name too long!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(bio.length() > 500){
            Toast.makeText(GetUserBioActivity.this, "bio too long!", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }

    private void selectPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                        imageUri = result.getData().getData();
                        profilePic.setImageURI(imageUri);
                        uploadPicture();
                    }
                }
            });

}