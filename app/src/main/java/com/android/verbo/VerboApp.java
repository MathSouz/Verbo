package com.android.verbo;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.IOException;

public class VerboApp extends Application
{
    public static final String GET_TOKEN_URL = "https://bibleapi.co/api/users/token";
    private SharedPreferences preferences;
    private RequestQueue requestQueue;

    @Override
    public void onCreate()
    {
        super.onCreate();
        this.requestQueue = Volley.newRequestQueue(this);
        this.preferences = getSharedPreferences("userProps", MODE_PRIVATE);
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setToken(String token)
    {
        this.preferences.edit().putString("token", token).apply();
    }

    public String getToken() {
        return this.preferences.getString("token", null);
    }

    public void createFile(String path)
    {
        File file = new File(getFilesDir(), path);

        try {
            if(file.createNewFile()) {
                Toast.makeText(this, "File " + path + " created!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "File not created :(", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
