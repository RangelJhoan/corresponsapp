package com.example.corresponsapp.interfacesgraficas.corresponsal.depositar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentDepositarBinding;
import com.example.corresponsapp.entidades.Cliente;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.validaciones.Validaciones;

public class DepositarFragment extends Fragment implements DepositarMVP.View {
    private FragmentDepositarBinding binding;
    private DepositarMVP.Presenter presenter;
    private NavController navController;

    public DepositarFragment() {

    }

    public static DepositarFragment newInstance(String param1, String param2) {
        DepositarFragment fragment = new DepositarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDepositarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new DepositarPresenterImpl(this);
        navController = Navigation.findNavController(view);

                binding.menuToolbar.ivPantalla.setImageResource(R.drawable.banco_128);
        binding.menuToolbar.tvTitulo.setText("Depositar Dinero");

        binding.btnDepositar.setOnClickListener(v -> {
            depositarDinero();
        });

    }

    private void depositarDinero() {
        EditText[] editTexts = {binding.etDocumentoEnvia, binding.etDocumentoRecibe, binding.etMonto};

        if (Validaciones.validarCampos(editTexts)) {
            Deposito deposito = new Deposito();
            Cliente cliente = new Cliente();

            deposito.setDocumento(binding.etDocumentoEnvia.getText().toString());
            cliente.setDocumento(binding.etDocumentoRecibe.getText().toString());
            deposito.setCliente(cliente);
            deposito.setMonto(Double.parseDouble(binding.etMonto.getText().toString()));

            presenter.depositarDinero(getContext(), deposito);

        } else {
            Toast.makeText(getContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void mostrarRespueta(String respuesta) {
        Toast.makeText(getContext(), respuesta, Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.corresponsalMenuFragment);
    }

    @Override
    public void mostrarError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }
}