package com.cbd.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cbd.android.R;
import com.cbd.android.common.Utils;
import com.cbd.android.ui.home.PostListFragment;
import com.cbd.android.ui.newPost.NewPostDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);
        Objects.requireNonNull(getSupportActionBar()).hide();

        fab = findViewById(R.id.fab);

        // Carga del fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new PostListFragment()).commit();

        // Listener navegación
        navView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                return true;
                            case R.id.navigation_my_posts:
                                return true;
                            case R.id.navigation_my_profile:
                                return true;
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.deleteToken();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
