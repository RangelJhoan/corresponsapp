package com.example.corresponsapp.interfacesgraficas.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corresponsapp.R;
import com.example.corresponsapp.entidades.ConsultaSaldo;
import com.example.corresponsapp.entidades.CuentaCreada;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.entidades.Transferencia;

import java.util.ArrayList;

public class AdaptadorInfoCorta extends RecyclerView.Adapter<AdaptadorInfoCorta.ViewHolderDatos> {

    private final ArrayList<Retiro> listaRetiros;
    private final ArrayList<Deposito> listaDepositos;
    private final ArrayList<Transferencia> listaTransferencias;
    private final ArrayList<PagoTarjeta> listaPagosTarjeta;
    private final ArrayList<CuentaCreada> listaCuentasCreada;
    private final ArrayList<ConsultaSaldo> listaConsultasSaldo;

    /**
     * Recibe la lista a mostrar en los distintos recyclers views
     *
     * @param listaRetiros        Recibe la lista de retiros. Si la lista es nula es porque se inicializó con otra lista
     * @param listaDepositos      Recibe la lista de depósitos. Si la lista es nula es porque se inicializó con otra lista
     * @param listaPagosTarjeta   Recibe la lista de pagos con tarjeta. Si la lista es nula es porque se inicializó con otra lista
     * @param listaTransferencias Recibe la lista de transferencias. Si la lista es nula es porque se inicializó con otra lista
     */
    public AdaptadorInfoCorta(ArrayList<Retiro> listaRetiros, ArrayList<Deposito> listaDepositos, ArrayList<PagoTarjeta> listaPagosTarjeta, ArrayList<Transferencia> listaTransferencias, ArrayList<ConsultaSaldo> listaConsultasSaldo, ArrayList<CuentaCreada> listaCuentasCreada) {
        this.listaRetiros = listaRetiros;
        this.listaDepositos = listaDepositos;
        this.listaPagosTarjeta = listaPagosTarjeta;
        this.listaTransferencias = listaTransferencias;
        this.listaConsultasSaldo = listaConsultasSaldo;
        this.listaCuentasCreada = listaCuentasCreada;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_transaccioninfocorta, parent, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        //Si la lista de retiros no es nula, llena el recycler view con esta lista
        if (listaRetiros != null) {
            holder.accion.setImageResource(R.drawable.retirar_128);

            holder.fecha.setText("Fecha: " + listaRetiros.get(position).getFecha());
            holder.monto.setText("Monto: " + listaRetiros.get(position).getMonto());
            holder.documento1.setText("Documento: " + listaRetiros.get(position).getCuentaBancaria().getCliente().getDocumento());

            holder.documento2.setVisibility(View.GONE);
            holder.numeroTarjeta.setVisibility(View.GONE);
        } else if (listaDepositos != null) {
            holder.accion.setImageResource(R.drawable.depositar_128);

            holder.fecha.setText("Fecha: " + listaDepositos.get(position).getFecha());
            holder.monto.setText("Monto: " + listaDepositos.get(position).getMonto());
            holder.documento1.setText("Documento que deposita: " + listaDepositos.get(position).getDocumento());
            holder.documento2.setText("Documento que recibe: " + listaDepositos.get(position).getCuentaBancaria().getCliente().getDocumento());

            holder.numeroTarjeta.setVisibility(View.GONE);
        } else if (listaPagosTarjeta != null) {
            holder.accion.setImageResource(R.drawable.pagotarjeta_128);

            holder.fecha.setText("Fecha: " + listaPagosTarjeta.get(position).getFecha());
            holder.monto.setText("Monto: " + listaPagosTarjeta.get(position).getValor());
            String numeroTarjeta = listaPagosTarjeta.get(position).getCuentaBancaria().getNumeroCuenta();
            holder.numeroTarjeta.setText("Número tarjeta: ************" + numeroTarjeta.substring(12, 16));
            holder.documento1.setText("Documento: " + listaPagosTarjeta.get(position).getCuentaBancaria().getCliente().getDocumento());

            holder.documento2.setVisibility(View.GONE);
        } else if (listaTransferencias != null) {
            holder.accion.setImageResource(R.drawable.transferir_128);

            holder.fecha.setText("Fecha: " + listaTransferencias.get(position).getFecha());
            holder.monto.setText("Monto: " + listaTransferencias.get(position).getMonto());
            holder.documento1.setText("Documento que transfiere: " + listaTransferencias.get(position).getCuentaTransfiere().getCliente().getDocumento());
            holder.documento2.setText("Documento que recibe: " + listaTransferencias.get(position).getCuentaRecibe().getCliente().getDocumento());

            holder.numeroTarjeta.setVisibility(View.GONE);
        } else if (listaCuentasCreada != null) {
            holder.accion.setImageResource(R.drawable.anadir_128);

            holder.fecha.setText("Fecha: " + listaCuentasCreada.get(position).getFecha());
            holder.monto.setText("Monto: " + listaCuentasCreada.get(position).getMontoInicial());
            holder.documento1.setText("Documento del cliente: " + listaCuentasCreada.get(position).getCliente().getDocumento());

            holder.numeroTarjeta.setVisibility(View.GONE);
            holder.documento2.setVisibility(View.GONE);
        } else if (listaConsultasSaldo != null) {
            holder.accion.setImageResource(R.drawable.consultar_saldo_128);

            holder.fecha.setText("Fecha: " + listaConsultasSaldo.get(position).getFecha());
            holder.monto.setText("Monto: " + listaConsultasSaldo.get(position).getSaldo());
            holder.documento1.setText("Documento del cliente: " + listaConsultasSaldo.get(position).getCliente().getDocumento());

            holder.numeroTarjeta.setVisibility(View.GONE);
            holder.documento2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (listaRetiros != null) {
            return listaRetiros.size();
        } else if (listaDepositos != null) {
            return listaDepositos.size();
        } else if (listaPagosTarjeta != null) {
            return listaPagosTarjeta.size();
        } else if (listaTransferencias != null) {
            return listaTransferencias.size();
        } else if (listaConsultasSaldo != null) {
            return listaConsultasSaldo.size();
        } else if (listaCuentasCreada != null) {
            return listaCuentasCreada.size();
        } else {
            return 0;
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        final TextView fecha, monto, documento1, documento2, numeroTarjeta;
        final ImageView accion;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            accion = itemView.findViewById(R.id.ivAccion);

            documento1 = itemView.findViewById(R.id.tvDocumento1);
            documento2 = itemView.findViewById(R.id.tvDocumento2);
            numeroTarjeta = itemView.findViewById(R.id.tvNumeroTarjeta);
            fecha = itemView.findViewById(R.id.tvFecha);
            monto = itemView.findViewById(R.id.tvMonto);

        }
    }
}
