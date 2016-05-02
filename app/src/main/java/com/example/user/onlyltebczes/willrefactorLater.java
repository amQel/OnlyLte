package com.example.user.onlyltebczes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class willrefactorLater extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_willrefactor_later);
    }


    public void buttonOnClick(View v) {
        Button switchActivity = (Button) v;

        startActivity(new Intent(willrefactorLater.this, MainLTEStatus.class ));

    }
}
