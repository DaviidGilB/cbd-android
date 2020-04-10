package com.cbd.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbd.android.R;
import com.cbd.android.models.RequestLogin;
import com.cbd.android.models.ResponseAuth;
import com.cbd.android.retrofit.CBDisposalClient;
import com.cbd.android.retrofit.CBDisposalService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    CBDisposalService cbdisposalService;
    CBDisposalClient cbdisposalClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofitInit();
    }

    private void retrofitInit() {
        cbdisposalClient = CBDisposalClient.getInstance();
        cbdisposalService = cbdisposalClient.getCBDisposalService();
    }

    public void submit(View view) {
        EditText usernameField = findViewById(R.id.username_field);
        String username = usernameField.getText().toString();

        EditText passwordField = findViewById(R.id.password_field);
        String password = passwordField.getText().toString();

        if (username.isEmpty()) {
            usernameField.setError("Debe indicar un usuario");
        }
        if (password.isEmpty()) {
            passwordField.setError("Debe indicar una contraseña");
        }
        if (!username.isEmpty() && !password.isEmpty()) {
            RequestLogin requestLogin = new RequestLogin(password, username);
            Call<ResponseAuth> call = this.cbdisposalService.login(requestLogin);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, response.body().getInfo().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
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
