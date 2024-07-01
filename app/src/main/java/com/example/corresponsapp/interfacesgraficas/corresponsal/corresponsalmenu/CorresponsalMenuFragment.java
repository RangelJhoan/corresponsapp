package com.example.corresponsapp.interfacesgraficas.corresponsal.corresponsalmenu;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.corresponsapp.entidades.Corresponsal;
import com.example.corresponsapp.entidades.Menu;
import com.example.corresponsapp.interfaces.IAbrirOpcCor;
import com.example.corresponsapp.interfaces.MenuCallback;
import com.example.corresponsapp.interfacesgraficas.adaptadores.MenuAdapter;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;


import java.util.ArrayList;

public class CorresponsalMenuFragment extends Fragment implements MenuCallback {

    private FragmentCorresponsalMenuBinding binding;
    private IAbrirOpcCor iAbrirOpcCor;
    private View view;
    private SharedPreferences preferences;

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

        inicializarPreference();

        String[] nombre = Sesion.corresponsalSesion.getNombreCompleto().split(" ");
        binding.tvSaludo.setText("Hola, " + nombre[0]);
        binding.tvSaldo.setText("$" + Sesion.corresponsalSesion.getSaldo());

        cargarMenu();

        binding.ibOpciones.setOnClickListener(view1 -> iAbrirOpcCor.abrirOpciones(view));
    }

    private void inicializarPreference() {
        Corresponsal sesionCorresponsal = new Corresponsal();

        sesionCorresponsal.setId(preferences.getInt(Sesion.LLAVE_ID, 0));
        sesionCorresponsal.setNombreCompleto(preferences.getString(Sesion.LLAVE_NOMBRE, null));
        sesionCorresponsal.setSaldo(preferences.getFloat(Sesion.LLAVE_SALDO, 0));

        Sesion.corresponsalSesion = sesionCorresponsal;
    }

    private void cargarMenu() {
        ArrayList<Menu> listaMenu = new ArrayList<>();
        listaMenu.add(new Menu(Constantes.PAGOTARJETA, R.drawable.pagotarjeta_128));
        listaMenu.add(new Menu(Constantes.RETIRAR, R.drawable.retirar_128));
        listaMenu.add(new Menu(Constantes.DEPOSITAR, R.drawable.depositar_128));
        listaMenu.add(new Menu(Constantes.TRANSFERIR, R.drawable.transferir_128));
        listaMenu.add(new Menu(Constantes.CONSULTAR_SALDO, R.drawable.consultar_saldo_128));
        listaMenu.add(new Menu(Constantes.CREAR_CUENTA, R.drawable.usuario_128));
        listaMenu.add(new Menu(Constantes.HISTORIAL_TRANSACCIONES, R.drawable.historial_128));

        binding.rvBotonesMenu.setLayoutManager(new GridLayoutManager(getContext(), 3));
        MenuAdapter adapter = new MenuAdapter(listaMenu, this);
        binding.rvBotonesMenu.setAdapter(adapter);
    }

    @Override
    public void navegarFragment(Menu menu) {
        final NavController navController = Navigation.findNavController(view);
        switch (menu.getNombre()) {
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
            case Constantes.CONSULTAR_SALDO:
                navController.navigate(R.id.consultarSaldoFragment);
                break;
            case Constantes.CREAR_CUENTA:
                navController.navigate(R.id.crearCuentaFragment);
                break;
            case Constantes.HISTORIAL_TRANSACCIONES:
                navController.navigate(R.id.historialTransaccionesFragment);
                break;
            default:
                Toast.makeText(getContext(), "Error en el menú.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        preferences = context.getSharedPreferences("sesion", Context.MODE_PRIVATE);
        if (context instanceof Activity) {
            Activity actividad = (Activity) context;
            iAbrirOpcCor = (IAbrirOpcCor) actividad;
        }
    }
}