package com.example.corresponsapp.interfacesgraficas.corresponsal.corresponsalcambiarclave;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.corresponsapp.R;
import com.example.corresponsapp.databinding.FragmentCorresponsalCambiarClaveBinding;
import com.example.corresponsapp.entidades.Corresponsal;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;
import com.example.corresponsapp.utilidades.Utilidades;

public class CorresponsalCambiarClaveFragment extends Fragment implements CorresponsalCambiarClaveMVP.View {

    private FragmentCorresponsalCambiarClaveBinding binding;
    private CorresponsalCambiarClaveMVP.Presenter presenter;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCorresponsalCambiarClaveBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new CorresponsalCambiarClavePresenterImpl(this);
        binding.menuToolbar.tvTitulo.setText(Constantes.CORRESPONSAL_CAMBIAR_CLAVE);
        binding.menuToolbar.ivPantalla.setImageResource(R.drawable.clave_128);
        navController = Navigation.findNavController(view);

        binding.btnCambiarClave.setOnClickListener(view1 -> validarCampos());
    }

    /**
     * Se realizan las distintas validaciones para realizar el cambio de clave en el corresponsal
     */
    private void validarCampos() {
        EditText[] editTexts = {binding.etCorreo, binding.etClave, binding.etConfirmarClave};
        //Se valida que los campos estén llenos
        if (Utilidades.validarCampos(editTexts)) {
            //Se valida la confirmación de la nueva clave
            if (binding.etClave.getText().toString().equals(binding.etConfirmarClave.getText().toString())) {
                Corresponsal corresponsal = new Corresponsal();
                corresponsal.setId(Sesion.corresponsalSesion.getId());
                corresponsal.setCorreo(binding.etCorreo.getText().toString());
                corresponsal.setClave(binding.etClave.getText().toString());
                presenter.cambiarClave(getContext(), corresponsal);
            } else {
                Toast.makeText(getContext(), "Las claves no coinciden", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "Por favor llene todos los campos", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Muestra un mensaje en pantalla con el resultado obtenido en el modelo
     *
     * @param resultado Resultado obtenido en el modelo luego de realizar las distintas validaciones y operaciones
     */
    @Override
    public void mostrarResultado(String resultado) {
        Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
        navController.navigate(R.id.corresponsalMenuFragment);
    }

    /**
     * Muestra un mensaje en pantalla si el Modelo indica que hubo algún error
     *
     * @param error Descripción del error ocurrido al realizarse el proceso
     */
    @Override
    public void mostrarError(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
    }
}