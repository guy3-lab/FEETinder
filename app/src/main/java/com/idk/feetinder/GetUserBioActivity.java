package com.idk.feetinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetUserBioActivity extends AppCompatActivity {

    private Button finishButton;
    private EditText enterName;
    private EditText enterBio;
    private Button back;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_bio);

        finishButton = findViewById(R.id.finish);
        enterName = findViewById(R.id.enter_name);
        enterBio = findViewById(R.id.enter_bio);
        back = findViewById(R.id.back);

        auth = FirebaseAuth.getInstance();

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

                if(name.matches(("^.*[^a-zA-Z0-9 ].*$"))){
                    Toast.makeText(GetUserBioActivity.this, "invalid characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(name.length() < 1){
                    Toast.makeText(GetUserBioActivity.this, "enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(name.length() > 100){
                    Toast.makeText(GetUserBioActivity.this, "name too long!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(bio.length() > 500){
                    Toast.makeText(GetUserBioActivity.this, "bio too long!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference currentUserName = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(userId).child("Name");

                currentUserName.setValue(name);

                DatabaseReference currentUserBio = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(userId).child("Bio");

                currentUserBio.setValue(bio);

                Intent intent = new Intent(GetUserBioActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}