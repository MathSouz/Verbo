package com.android.verbo.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.verbo.bible.BibleActivity;
import com.android.verbo.R;
import com.android.verbo.VerboApp;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignOnFragment extends Fragment implements View.OnClickListener
{
    private LoginActivity loginActivity;
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;
    private Button toSignInButton;

    public SignOnFragment(LoginActivity loginActivity)
    {
        this.loginActivity = loginActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sign_on, container, false);
        this.emailInput = view.findViewById(R.id.email_input);
        this.passwordInput = view.findViewById(R.id.password_input);
        this.loginButton = view.findViewById(R.id.login_button);
        this.toSignInButton = view.findViewById(R.id.back_to_login_button);

        this.loginButton.setOnClickListener(this);
        this.toSignInButton.setOnClickListener(this);
        return view;
    }

    public static SignOnFragment newInstance(LoginActivity loginActivity)
    {
        return new SignOnFragment(loginActivity);
    }

    @Override
    public void onClick(View v)
    {
        if(v == toSignInButton)
        {
            loginActivity.setScreen(SignInFragment.newInstance(loginActivity));
        }

        else if(v == loginButton)
        {
            emailInput.setError(null);
            passwordInput.setError(null);

            if(emailInput.getText().toString().trim().length() == 0) {
                emailInput.setError("Campo vazio");
                return ;
            }

            if(passwordInput.getText().toString().trim().length() == 0) {
                passwordInput.setError("Campo vazio");
                return ;
            }

            tryToLogin(emailInput.getText().toString(), passwordInput.getText().toString());
        }
    }

    private void tryToLogin(String email, String password)
    {
        loginButton.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(getContext());

        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.PUT, VerboApp.GET_TOKEN_URL, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    VerboApp verboApp = (VerboApp)getActivity().getApplicationContext();
                    String token = response.getString("token");
                    verboApp.setToken(token);
                    startActivity(new Intent(getActivity(), BibleActivity.class));
                    getActivity().finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                loginButton.setEnabled(true);
                passwordInput.getText().clear();
                final Snackbar snackbar = Snackbar.make(getView(), "Credenciais não encontradas!\nTente novamente.", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Entendi", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        });

        queue.add(objectRequest);
    }
}
