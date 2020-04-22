package com.cbd.android.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.cbd.android.R;
import com.cbd.android.common.Constants;
import com.cbd.android.common.Utils;
import com.cbd.android.ui.home.PostListFragment;
import com.cbd.android.ui.newPost.NewPostDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BottomNavigationView navView;
    private FloatingActionButton fab;
    private ImageView appExitImage;
    private TextView appExitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        Objects.requireNonNull(getSupportActionBar()).hide();

        fab = findViewById(R.id.fab);
        appExitImage = findViewById(R.id.app_exit);
        appExitText = findViewById(R.id.app_exit_text);

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
}
