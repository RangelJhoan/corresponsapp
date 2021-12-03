package com.example.corresponsapp.interfacesgraficas.corresponsal;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentCorresponsalMenuBinding;
import com.example.corresponsapp.entidades.Menu;
import com.example.corresponsapp.interfaces.MenuCallback;
import com.example.corresponsapp.interfacesgraficas.adaptadores.MenuAdapter;


import java.util.ArrayList;

public class CorresponsalMenuFragment extends Fragment implements MenuCallback {

    private FragmentCorresponsalMenuBinding binding;
    private ArrayList<Menu> listaMenu;
    private MenuAdapter adapter;
    private View view;


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
        this.view = view;

        listaMenu = new ArrayList<>();
        listaMenu.add(new Menu("Pagar con tarjeta", R.drawable.pagotarjeta_128));
        listaMenu.add(new Menu("Retirar", R.drawable.retirar_128));
        listaMenu.add(new Menu("Depositar", R.drawable.depositar_128));
        listaMenu.add(new Menu("Transferir", R.drawable.transferir_128));
        listaMenu.add(new Menu("Crear cuenta", R.drawable.anadir_128));

        binding.rvBotonesMenu.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapter = new MenuAdapter(listaMenu, this);
        binding.rvBotonesMenu.setAdapter(adapter);

    }

    @Override
    public void navegarFragment(Menu menu) {
        final NavController navController = Navigation.findNavController(view);
        switch (menu.getNombre()){
            case "Pagar con Tarjeta":
                navController.navigate(R.id.pagoTarjetaFragment);
                break;
            case "Retirar":
                navController.navigate(R.id.retirarFragment);
                break;
            case "Depositar":
                navController.navigate(R.id.depositarFragment);
                break;
            case "Transferir":
                navController.navigate(R.id.transferirFragment);
                break;
            case "Crear cuenta":
                navController.navigate(R.id.crearCuentaFragment);
                break;
            default:
                Toast.makeText(getContext(), "Error en el menú.", Toast.LENGTH_SHORT).show();
        }
    }
}