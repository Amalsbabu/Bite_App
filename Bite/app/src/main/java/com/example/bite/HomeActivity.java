package com.example.bite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    Button btnLogout,contactus;
    FirebaseAuth bitefirebase;
    private FirebaseAuth.AuthStateListener biteAuthStateListener;
    RecyclerView recyclerView;
    RecyclerView.Adapter programAdapter;
    RecyclerView.LayoutManager layoutManager;
    String[] hotelNameList = {"Dominoz","Pizza Hut","Chicking"
    };
    String[] hotelRating = {"4.6","4.5","4.3"};
    int[] hotelImages = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView =findViewById(R.id.rcv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        programAdapter = new ProgramAdapter(this,hotelNameList,hotelRating,hotelImages);
        recyclerView.setAdapter(programAdapter);
        contactus = findViewById(R.id.contact);

        btnLogout =findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent InToMain = new Intent(view.getContext(),MainActivity.class);
                startActivity(InToMain);
                finishAffinity();
            }

        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this,ContactUs.class);
                startActivity(i);

            }

        });
    }

}