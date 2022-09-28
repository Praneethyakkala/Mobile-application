package com.bmlmunjal.mad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class profile_page extends AppCompatActivity {

    ImageView backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        backbutton = findViewById(R.id.backButtonProfilePage);
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
    }
}