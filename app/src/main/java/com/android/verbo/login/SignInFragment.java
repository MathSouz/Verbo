package com.android.verbo.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.verbo.R;

public class SignInFragment extends Fragment implements View.OnClickListener
{
    private LoginActivity loginActivity;
    private Button backToLogin;

    public SignInFragment(LoginActivity loginActivity)
    {
        this.loginActivity = loginActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        backToLogin = view.findViewById(R.id.to_register_button);
        this.backToLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    public static SignInFragment newInstance(LoginActivity loginActivity)
    {
        return new SignInFragment(loginActivity);
    }

    @Override
    public void onClick(View v)
    {
        if(v == backToLogin)
        {
            loginActivity.onBackPressed();
        }
    }
}
