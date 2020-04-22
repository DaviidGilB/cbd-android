package com.cbd.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbd.android.R;
import com.cbd.android.common.Constants;
import com.cbd.android.common.Responses;
import com.cbd.android.common.Utils;
import com.cbd.android.models.RequestLogin;
import com.cbd.android.models.ResponseGeneric;
import com.cbd.android.retrofit.CBDisposalClient;
import com.cbd.android.retrofit.CBDisposalService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    // Campos
    EditText usernameField, passwordField;

    // Servicios
    CBDisposalService cbdisposalService;
    CBDisposalClient cbdisposalClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Utils.hasToken()) {
            setContentView(R.layout.activity_login);

            retrofitInit();
            fieldsInit();
        } else {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void retrofitInit() {
        cbdisposalClient = CBDisposalClient.getInstance();
        cbdisposalService = cbdisposalClient.getCBDisposalService();
    }

    private void fieldsInit() {
        usernameField = findViewById(R.id.username_field);
        passwordField = findViewById(R.id.password_field);
    }

    public void submit(View view) {
        String username = usernameField.getText().toString();;
        String password = passwordField.getText().toString();

        if (username.isEmpty()) {
            usernameField.setError(Constants.ERROR_USUARIO_VACIO);
        }
        if (password.isEmpty()) {
            passwordField.setError(Constants.ERROR_CONTRASENA_VACIA);
        }
        if (!username.isEmpty() && !password.isEmpty()) {
            RequestLogin requestLogin = new RequestLogin(username, password);
            Call<ResponseGeneric> call = this.cbdisposalService.login(requestLogin);
            call.enqueue(new Callback<ResponseGeneric>() {
                @Override
                public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                    try {
                        Toast.makeText(LoginActivity.this, response.body().getInfo().getMessage(), Toast.LENGTH_LONG).show();
                        if (response.body().getInfo().getCode() == Responses.OK_SESION_INICIADA_CORRECTAMENTE) {
                            Utils.saveToken(response.body().getToken());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, Constants.ERROR_INESPERADO, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseGeneric> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, Constants.ERROR_COMUNICACION, Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    public void redirectRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
