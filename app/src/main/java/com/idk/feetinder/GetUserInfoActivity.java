// Catherine
package com.idk.feetinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetUserInfoActivity extends AppCompatActivity {

    private RadioGroup genderSelect;
    private Button continueButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info);

        genderSelect = findViewById(R.id.gender_select);
        continueButton = findViewById(R.id.continueButton);
        auth = FirebaseAuth.getInstance();

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selected = genderSelect.getCheckedRadioButtonId();
                RadioButton checkedButton = findViewById(selected);

                if(checkedButton == null){
                    Toast.makeText(GetUserInfoActivity.this, "must select gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                String userId = auth.getCurrentUser().getUid();
                DatabaseReference currentUserGender = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(userId).child("Gender");

                currentUserGender.setValue(checkedButton.getText());

                Intent intent = new Intent(GetUserInfoActivity.this, UserQuestionnaireActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}