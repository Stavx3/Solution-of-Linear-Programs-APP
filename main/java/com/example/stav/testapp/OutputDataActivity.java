package com.example.stav.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class OutputDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_data);
        Intent intent=getIntent();
        String ans=intent.getStringExtra(InputDataActivity.ANS_KEY);

        TextView tv=(TextView) findViewById(R.id.outputtv);
        tv.setText(ans);

    }
}
