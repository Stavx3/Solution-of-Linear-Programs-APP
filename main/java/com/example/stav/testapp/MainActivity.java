package com.example.stav.testapp;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    public static final String VAR_KEY="com.example.stav.testapp.VER_KEY";
    public static final String CON_KEY="com.example.stav.testapp.CON_KEY";
    public static final String MINMAX_KEY="com.example.stav.testapp.MINMAX_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ButtonNext(View view){
        EditText VarNum=(EditText) findViewById(R.id.num_var_EditText);
        String Var= VarNum.getText().toString();

        EditText ConNum=(EditText) findViewById(R.id.num_con_EditText);
        String Con= ConNum.getText().toString();

        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup2);
        int selectedId = rg.getCheckedRadioButtonId();

        String minmax="";
        if(selectedId== android.R.id.button1)minmax=minmax+"Minimum";
        else minmax=minmax+"Maximum";


        Intent intent= new Intent(this,InputDataActivity.class);
        intent.putExtra(VAR_KEY,Var);
        intent.putExtra(CON_KEY,Con);
        intent.putExtra(MINMAX_KEY,minmax);
        startActivity(intent);
    }

}
