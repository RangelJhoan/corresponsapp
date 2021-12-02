package com.example.corresponsapp.interfaces.corresponsal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentCorresponsalMenuBinding;

public class CorresponsalMenuFragment extends Fragment {

    private FragmentCorresponsalMenuBinding binding;

    public CorresponsalMenuFragment() {

    }

    public static CorresponsalMenuFragment newInstance(String param1, String param2) {
        CorresponsalMenuFragment fragment = new CorresponsalMenuFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCorresponsalMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}