package com.cbd.android.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.cbd.android.R;
import com.cbd.android.common.Constants;
import com.cbd.android.common.MyApp;
import com.cbd.android.common.Responses;
import com.cbd.android.common.Utils;
import com.cbd.android.models.ResponseUser;
import com.cbd.android.retrofit.CBDAuthDisposalClient;
import com.cbd.android.retrofit.CBDAuthDisposalService;
import com.cbd.android.ui.home.PostListFragment;
import com.cbd.android.ui.newPost.NewPostDialogFragment;
import com.cbd.android.viewModels.PostViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, PermissionListener {
    private BottomNavigationView navView;
    private FloatingActionButton fab;
    private ImageView appExitImage, actionBarImage;
    private TextView appExitText, userNameText;
    private PostViewModel postViewModel;

    // Gestión imagen
    private Uri imagenSeleccionada;

    // Servicios
    private CBDAuthDisposalClient cbdAuthDisposalClient;
    private CBDAuthDisposalService cbdAuthDisposalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        Objects.requireNonNull(getSupportActionBar()).hide();

        postViewModel = ViewModelProviders.of(this).get(PostViewModel.class);

        fab = findViewById(R.id.fab);
        appExitImage = findViewById(R.id.app_exit);
        appExitText = findViewById(R.id.app_exit_text);
        actionBarImage = findViewById(R.id.action_bar_image);
        userNameText = findViewById(R.id.user_name_text);

        // Carga del fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, PostListFragment.newInstance(Constants.POST_LIST_ALL)).commit();

        // Listener navegación
        navView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        Fragment f = null;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                f = PostListFragment.newInstance(Constants.POST_LIST_ALL);
                                break;
                            case R.id.navigation_my_profile:
                                break;
                        }

                        if (f != null) {
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainer, f)
                                    .commit();
                        }

                        return false;
                    }
                });

        // Listener boton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPostDialogFragment dialogFragment = new NewPostDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "NewPostDialogFragment");
            }
        });

        // Listener exit
        appExitText.setOnClickListener(this);
        appExitImage.setOnClickListener(this);

        retrofitInit();

    }

    private void retrofitInit() {
        cbdAuthDisposalClient = CBDAuthDisposalClient.getInstance();
        cbdAuthDisposalService = cbdAuthDisposalClient.getCBDAuthDisposalService();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void exit() {
        Utils.deleteToken();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Integer id = v.getId();
        if (id == R.id.app_exit_text || id == R.id.app_exit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea realmente salir de la aplicación?").setTitle("Cerrar sesión");
            builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    exit();
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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

    public Uri getImagenSeleccionada() {
        return imagenSeleccionada;
    }

    public void setImagenSeleccionada(Uri imagenSeleccionada) {
        this.imagenSeleccionada = imagenSeleccionada;
    }

    public void loadUser() {
        Call<ResponseUser> call = this.cbdAuthDisposalService.getUserLogged();
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if (response.body().getInfo().getCode() == Responses.OK_USUARIO_RECUPERADO_CORRECTAMENTE) {
                    userNameText.setText(response.body().getUser().getName());
                    if (!response.body().getUser().getAvatar().isEmpty()) {
                        Glide.with(MyApp.getContext())
                                .load(response.body().getUser().getAvatar())
                                .centerCrop()
                                .into(actionBarImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });
    }
}
