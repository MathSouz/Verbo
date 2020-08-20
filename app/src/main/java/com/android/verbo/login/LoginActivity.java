package com.android.verbo.login;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;

import com.android.verbo.R;

public class LoginActivity extends FragmentActivity
{
    private FragmentManager fragmentManager;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.fragmentManager = getSupportFragmentManager();
        this.alertDialog = new AlertDialog.Builder(this)
                .setTitle("Atenção")
                .setMessage("O login é único!\n\nUma vez dentro não será mais necessário se autenticar quando entrar no aplicativo.")
                .setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.hide();
                    }
                })
                .create();

        setScreen(SignOnFragment.newInstance(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public void onBackPressed()
    {
        if(fragmentManager.getBackStackEntryCount() > 1)
            super.onBackPressed();
    }

    public void setScreen(Fragment screen)
    {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.setCustomAnimations(R.anim.appear, R.anim.desappear, R.anim.appear, R.anim.desappear);
        fragmentTransaction.replace(R.id.frame_layout, screen);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        if(getPreferences(MODE_PRIVATE).getBoolean("firstLogin", true))
        {
            alertDialog.show();
            getPreferences(MODE_PRIVATE).edit().putBoolean("firstLogin", false).apply();
        }
    }
}