package com.firebase.androidchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class Registration extends Activity {
    String myUsername;
    EditText et;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        et=(EditText)findViewById(R.id.editText);
        b=(Button)findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }
    void register(){
        SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs", 0);
        myUsername = prefs.getString("myusername", null);
        if (myUsername == null) {
            if (et.getText().toString()!=""){
                myUsername=et.getText().toString();
                prefs.edit().putString("myusername", myUsername).commit();
                Toast.makeText(this,"Registeration Success",Toast.LENGTH_LONG).show();
                Intent i=new Intent(this,MainActivity.class);
                startActivity(i);
            }

        }
    }

}
