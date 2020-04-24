package com.cbd.android.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbd.android.R;
import com.cbd.android.common.Constants;
import com.cbd.android.common.Responses;
import com.cbd.android.common.Utils;
import com.cbd.android.models.RequestRegister;
import com.cbd.android.models.ResponseGeneric;
import com.cbd.android.retrofit.CBDisposalClient;
import com.cbd.android.retrofit.CBDisposalService;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, PermissionListener {
    // Campos
    EditText usernameField, emailField, passwordField, passwordConfirmField, nameField;
    Button userUploadPhoto;

    // Otros
    boolean submit;

    // Servicios
    CBDisposalService cbdisposalService;
    CBDisposalClient cbdisposalClient;

    // Gestión imagen
    private Uri imagenSeleccionada;
    private PermissionListener allPermissionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        retrofitInit();
        fieldsInit();

        userUploadPhoto.setOnClickListener(this);
    }

    private void retrofitInit() {
        cbdisposalClient = CBDisposalClient.getInstance();
        cbdisposalService = cbdisposalClient.getCBDisposalService();
    }

    private void fieldsInit() {
        usernameField = findViewById(R.id.username_field);
        nameField = findViewById(R.id.name_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        passwordConfirmField = findViewById(R.id.password_confirm_field);
        userUploadPhoto = findViewById(R.id.user_upload_photo);
    }

    public void submit(View view) {
        String username = usernameField.getText().toString();
        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String passwordConfirm = passwordConfirmField.getText().toString();

        submit = true;
        if (username.isEmpty()) {
            usernameField.setError(Constants.ERROR_USUARIO_VACIO);
            submit = false;
        }
        if (name.isEmpty()) {
            nameField.setError(Constants.ERROR_NOMBRE_VACIO);
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
            Bitmap bmp = null;
            if (imagenSeleccionada != null) {
                try {
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imagenSeleccionada);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String avatar = Utils.getBase64FromBitmap(bmp, 200, 80);
            RequestRegister requestRegister = new RequestRegister(username, email, password, name, avatar);
            Call<ResponseGeneric> call = this.cbdisposalService.register(requestRegister);
            call.enqueue(new Callback<ResponseGeneric>() {
                @Override
                public void onResponse(Call<ResponseGeneric> call, Response<ResponseGeneric> response) {
                    Toast.makeText(RegisterActivity.this, response.body().getInfo().getMessage(), Toast.LENGTH_LONG).show();
                    if (response.body().getInfo().getCode() == Responses.OK_USUARIO_CREADO_CORRECTAMENTE) {
                        Utils.saveToken(response.body().getToken());
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseGeneric> call, Throwable t) {
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

    @Override
    public void onClick(View v) {
        Integer id = v.getId();
        if (id == R.id.user_upload_photo) {
            checkPermissions();
        }
    }

    private void checkPermissions() {
        PermissionListener dialogOnDeniedPermissionListener = DialogOnDeniedPermissionListener.Builder.withContext(this)
                .withTitle("Permisos")
                .withMessage("Los permisos solicitados son necesarios para seleccionar una imagen")
                .withButtonText("Aceptar")
                .withIcon(R.mipmap.ic_launcher)
                .build();

        allPermissionsListener = new CompositePermissionListener((PermissionListener) this, dialogOnDeniedPermissionListener);
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissionsListener).check();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constants.SELECT_PHOTO_GALLERY) {
                if (data != null) {
                    imagenSeleccionada = data.getData();
                }
            }
        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
        Intent seleccionarFoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(seleccionarFoto, Constants.SELECT_PHOTO_GALLERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

    }
}
