package com.bignerdranch.android.smartlockdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Unlocked extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlocked);
    }

    public void onClick(View view){
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }
}
