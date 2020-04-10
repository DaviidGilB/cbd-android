package com.cbd.android

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun submit(view: View?) {
        val username: EditText = findViewById(R.id.username_field);
        val password: EditText = findViewById(R.id.password_field);
    }

    fun redirectRegister(view: View?) {
    }
}
