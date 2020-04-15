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
import com.cbd.android.models.RequestRegister;
import com.cbd.android.models.ResponseAuth;
import com.cbd.android.retrofit.CBDisposalClient;
import com.cbd.android.retrofit.CBDisposalService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    // Campos
    EditText usernameField, emailField, passwordField, passwordConfirmField;

    // Otros
    boolean submit;

    // Servicios
    CBDisposalService cbdisposalService;
    CBDisposalClient cbdisposalClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        retrofitInit();
        fieldsInit();
    }

    private void retrofitInit() {
        cbdisposalClient = CBDisposalClient.getInstance();
        cbdisposalService = cbdisposalClient.getCBDisposalService();
    }

    private void fieldsInit() {
        usernameField = findViewById(R.id.username_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        passwordConfirmField = findViewById(R.id.password_confirm_field);
    }

    public void submit(View view) {
        String username = usernameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String passwordConfirm = passwordConfirmField.getText().toString();

        submit = true;
        if (username.isEmpty()) {
            usernameField.setError(Constants.ERROR_USUARIO_VACIO);
            submit = false;
        }
        if (email.isEmpty()) {
            emailField.setError(Constants.ERROR_EMAIL_VACIO);
            submit = false;
        }
        if (password.isEmpty() || passwordConfirm.isEmpty()) {
            passwordField.setError(Constants.ERROR_CONTRASENAS_VACIAS);
            passwordConfirmField.setError(null);
            submit = false;
        } else if (!password.contentEquals(passwordConfirm)) {
            passwordField.setError(null);
            passwordConfirmField.setError(Constants.ERROR_VALIDACION_CONTRASENAS);
            submit = false;
        }

        if (submit) {
            RequestRegister requestRegister = new RequestRegister(username, email, password);
            Call<ResponseAuth> call = this.cbdisposalService.register(requestRegister);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    Toast.makeText(RegisterActivity.this, response.body().getInfo().getMessage(), Toast.LENGTH_LONG).show();
                    if (response.body().getInfo().getCode() == Responses.OK_USUARIO_CREADO_CORRECTAMENTE) {
                        Utils.saveToken(response.body().getObject());
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, Constants.ERROR_COMUNICACION, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
