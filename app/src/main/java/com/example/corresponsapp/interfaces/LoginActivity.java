package com.example.corresponsapp.interfaces;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}