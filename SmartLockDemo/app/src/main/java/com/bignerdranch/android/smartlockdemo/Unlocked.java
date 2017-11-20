package com.bignerdranch.android.smartlockdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Unlocked extends AppCompatActivity {

    TextView output;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlocked);
        //dbHandler = new MyDBHandler(this,null,null,1);
        output = (TextView) findViewById(R.id.output);
        //printDatabase();
        Bundle logx = getIntent().getExtras();
        if(logx == null){
            return;
        }
        String logData = logx.getString("log");
        output.setText(logData);
    }

    //Print the log
    public void printDatabase(){
        String dbString = dbHandler.databaseToString();
        output.setText(dbString);
    }
}
