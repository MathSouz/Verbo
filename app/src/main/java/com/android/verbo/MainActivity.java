package com.android.verbo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.verbo.bible.BibleActivity;
import com.android.verbo.login.LoginActivity;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VerboApp verboApp = (VerboApp)getApplicationContext();

        String token = verboApp.getToken();

        if(token != null && token.trim().length() > 0) {
            startBibleActivity();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
    }

    public void startBibleActivity()
    {
        Intent intent = new Intent(this, BibleActivity.class);
        startActivity(intent);
    }
}