package com.idk.feetinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private View card;
    private View backCard;
    private Button logOut;
    private TextView cardText;

    private TextView greeting;
    private ImageButton profile;

    private int cardNum = 1;
    private boolean homeTaken = false;
    private float xDown = 0;
    private float xHomeCard;
    private float xHomeText;
    private final int SWIPE_THRESHOLD = 350;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int screenSize = getScreenWidth(MainActivity.this);

        card = findViewById(R.id.card_box);
        backCard = findViewById(R.id.card_behind);
        cardText = findViewById(R.id.card_text);
        logOut = findViewById(R.id.log_out_placeholder);
        greeting = findViewById(R.id.greeting);
        profile = findViewById(R.id.profile);

        auth = FirebaseAuth.getInstance();

        String userId = auth.getCurrentUser().getUid();
        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference();

        currentUserDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = (String) snapshot.child("Users").child(userId).child("Name").getValue();

                greeting.setText("Hello " + name + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "read failed: " + error.getCode(), Toast.LENGTH_SHORT);
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(MainActivity.this, LoginRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetUserBioActivity.class);
                startActivity(intent);
            }
        });

        ObjectAnimator animationCard = ObjectAnimator.ofFloat(card, "translationX", screenSize);
        animationCard.setDuration(250);
        ObjectAnimator animationText = ObjectAnimator.ofFloat(cardText, "translationX", screenSize);
        animationText.setDuration(250);


        card.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_UP:
                        if(card.getX() >= (xHomeCard +SWIPE_THRESHOLD)){ // swipe right
                            animationCard.setFloatValues(screenSize);
                            animationText.setFloatValues(screenSize);
                            animationCard.start();
                            animationText.start();
                            cardNum++;
                            cardText.setText("CARD " + cardNum);
                        } else if((card.getX() + SWIPE_THRESHOLD) <= xHomeCard){ // swipe left
                            animationCard.setFloatValues(screenSize*-1);
                            animationText.setFloatValues(screenSize*-1);
                            animationCard.start();
                            animationText.start();
                            cardNum--;
                            cardText.setText("CARD " + cardNum);
                        }

                        if(animationCard.isRunning()){
                            Utils.delay(500, new Utils.DelayCallback() {
                                @Override
                                public void afterDelay() {
                                    animationCard.end();
                                    animationText.end();
                                    card.setX(xHomeCard);
                                    cardText.setX(xHomeText);
                                }
                            });
                        }

                        card.setX(xHomeCard);
                        cardText.setX(xHomeText);
                        break;

                    case MotionEvent.ACTION_DOWN:
                        if(!homeTaken){
                            xHomeCard = card.getX();
                            xHomeText = cardText.getX();
                            homeTaken = true;
                        }

                        xDown = motionEvent.getX();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float xMoved = motionEvent.getX();
                        float xDistance = xMoved- xDown;

                        card.setX(card.getX() + xDistance);
                        cardText.setX(cardText.getX() + xDistance);

                        if(card.getX() >= xHomeCard){ // lean right
                            backCard.setBackgroundColor(Color.parseColor("#2fad39"));
                        } else if(card.getX() <= xHomeCard){ // lean left
                            backCard.setBackgroundColor(Color.parseColor("#ad2f2f"));
                        } else {
                            backCard.setBackgroundColor(Color.parseColor("#e8dab3"));
                        }

                        break;

                }
                return super.onTouch(view, motionEvent);
            }

            @Override
            public void onClick() {
                ProfileDialog profileDialog = new ProfileDialog(MainActivity.this);
                profileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                profileDialog.showDialog();
            }
        });
    }

    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        return metrics.widthPixels;
    }
}