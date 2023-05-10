package com.example.chat_gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ModelClass> userList;
    Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        initData();
        initRecyclerView(); 



    }

    private void initData() {
        userList = new ArrayList<>();
        userList.add(new ModelClass(R.drawable.squirtle, "Squirtle", "3:25AM", "Are you cheating on me?", "----------------------------------------------------------------------------------------------"));
        userList.add(new ModelClass(R.drawable.cubone, "Cubone", "9:13PM", "Who is she?", "----------------------------------------------------------------------------------------------"));
        userList.add(new ModelClass(R.drawable.bulbasaur, "Bulbasaur", "2:07PM", "I'm on keto...", "----------------------------------------------------------------------------------------------"));
        userList.add(new ModelClass(R.drawable.charmander, "Charmander", "8:00AM", "Hey babe~", "----------------------------------------------------------------------------------------------"));
        userList.add(new ModelClass(R.drawable.eevee, "Eevee", "7:03AM", "What's up?", "----------------------------------------------------------------------------------------------"));
        userList.add(new ModelClass(R.drawable.metapod, "Metapod", "10:00PM", "How's it shaking", "----------------------------------------------------------------------------------------------"));
        userList.add(new ModelClass(R.drawable.mew, "MewTwo", "9:57AM", "Nice toes. What's your cuticle care routine?", "----------------------------------------------------------------------------------------------"));
        userList.add(new ModelClass(R.drawable.pikachu, "Pikachu", "2:15AM", "Can I collect your toenails?", "----------------------------------------------------------------------------------------------"));

    }



    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(userList);
        recyclerView.setAdapter((adapter));
        adapter.notifyDataSetChanged();
    }
}