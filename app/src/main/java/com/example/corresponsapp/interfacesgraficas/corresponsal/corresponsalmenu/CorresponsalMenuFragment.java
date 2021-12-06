package com.example.corresponsapp.interfacesgraficas.corresponsal.corresponsalmenu;

import android.annotation.SuppressLint;
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
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;


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

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        String[] nombre = Sesion.corresponsalSesion.getNombre_completo().split(" ");
        binding.tvSaludo.setText("Hola, " + nombre[0]);
        binding.tvSaldo.setText("$"+Sesion.corresponsalSesion.getSaldo());

        listaMenu = new ArrayList<>();
        listaMenu.add(new Menu(Constantes.PAGOTARJETA, R.drawable.pagotarjeta_128));
        listaMenu.add(new Menu(Constantes.RETIRAR, R.drawable.retirar_128));
        listaMenu.add(new Menu(Constantes.DEPOSITAR, R.drawable.depositar_128));
        listaMenu.add(new Menu(Constantes.TRANSFERIR, R.drawable.transferir_128));
        listaMenu.add(new Menu(Constantes.CREAR_CUENTA, R.drawable.anadir_128));

        binding.rvBotonesMenu.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapter = new MenuAdapter(listaMenu, this);
        binding.rvBotonesMenu.setAdapter(adapter);

    }

    @Override
    public void navegarFragment(Menu menu) {
        final NavController navController = Navigation.findNavController(view);
        switch (menu.getNombre()){
            case Constantes.PAGOTARJETA:
                navController.navigate(R.id.pagoTarjetaFragment);
                break;
            case Constantes.RETIRAR:
                navController.navigate(R.id.retirarFragment);
                break;
            case Constantes.DEPOSITAR:
                navController.navigate(R.id.depositarFragment);
                break;
            case Constantes.TRANSFERIR:
                navController.navigate(R.id.transferirFragment);
                break;
            case Constantes.CREAR_CUENTA:
                navController.navigate(R.id.crearCuentaFragment);
                break;
            default:
                Toast.makeText(getContext(), "Error en el menú.", Toast.LENGTH_SHORT).show();
        }
    }
}