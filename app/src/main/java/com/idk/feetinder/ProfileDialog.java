// Catherine
package com.idk.feetinder;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import android.widget.TextView;

import androidx.annotation.NonNull;

public class ProfileDialog extends Dialog {
    public ProfileDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.profile_dialog);
    }

    public void showDialog(){
        TextView name = findViewById(R.id.profile_name);
        TextView bio = findViewById(R.id.profile_bio);

        show();
    }
}
