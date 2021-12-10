package com.example.corresponsapp.interfacesgraficas.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corresponsapp.R;
import com.example.corresponsapp.entidades.Deposito;
import com.example.corresponsapp.entidades.PagoTarjeta;
import com.example.corresponsapp.entidades.Retiro;
import com.example.corresponsapp.entidades.Transferencia;

import java.util.ArrayList;

public class AdaptadorInfoCorta extends RecyclerView.Adapter<AdaptadorInfoCorta.ViewHolderDatos> {

    private ArrayList<Retiro> listaRetiros;
    private ArrayList<Deposito> listaDepositos;
    private ArrayList<Transferencia> listaTransferencias;
    private ArrayList<PagoTarjeta> listaPagosTarjeta;

    public AdaptadorInfoCorta(ArrayList<Retiro> listaRetiros, ArrayList<Deposito> listaDepositos, ArrayList<PagoTarjeta> listaPagosTarjeta, ArrayList<Transferencia> listaTransferencias) {
        this.listaRetiros = listaRetiros;
        this.listaDepositos = listaDepositos;
        this.listaPagosTarjeta = listaPagosTarjeta;
        this.listaTransferencias = listaTransferencias;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_transaccioninfocorta, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        if (listaRetiros != null) {
            holder.accion.setImageResource(R.drawable.retirar_128);

            holder.fecha.setText("Fecha: " + listaRetiros.get(position).getFecha());
            holder.monto.setText("Monto: " + String.valueOf(listaRetiros.get(position).getMonto()));
            holder.documento1.setText("Documento: " + listaRetiros.get(position).getCuentaBancaria().getCliente().getDocumento());

            holder.documento2.setVisibility(View.GONE);
            holder.numeroTarjeta.setVisibility(View.GONE);
        } else if (listaDepositos != null) {
            holder.accion.setImageResource(R.drawable.depositar_128);

            holder.fecha.setText("Fecha: " + listaDepositos.get(position).getFecha());
            holder.monto.setText("Monto: " + String.valueOf(listaDepositos.get(position).getMonto()));
            holder.documento1.setText("Documento que deposita: " + listaDepositos.get(position).getDocumento());
            holder.documento2.setText("Documento que recibe: " + listaDepositos.get(position).getCuentaBancaria().getCliente().getDocumento());

            holder.numeroTarjeta.setVisibility(View.GONE);
        } else if (listaPagosTarjeta != null) {
            holder.accion.setImageResource(R.drawable.pagotarjeta_128);

            holder.fecha.setText("Fecha: " + listaPagosTarjeta.get(position).getFecha());
            holder.monto.setText("Monto: " + listaPagosTarjeta.get(position).getValor());
            holder.numeroTarjeta.setText("Número tarjeta: " + listaPagosTarjeta.get(position).getCuentaBancaria().getNumero_cuenta());
            holder.documento1.setText("Documento: " + listaPagosTarjeta.get(position).getCuentaBancaria().getCliente().getDocumento());

            holder.documento2.setVisibility(View.GONE);
        } else if (listaTransferencias != null) {
            holder.accion.setImageResource(R.drawable.transferir_128);

            holder.fecha.setText("Fecha: " + listaTransferencias.get(position).getFecha());
            holder.monto.setText("Monto: " + listaTransferencias.get(position).getMonto());
            holder.documento1.setText("Documento que transfiere: " + listaTransferencias.get(position).getCuentaTransfiere().getCliente().getDocumento());
            holder.documento2.setText("Documento que recibe: " + listaTransferencias.get(position).getCuentaRecibe().getCliente().getDocumento());

            holder.numeroTarjeta.setVisibility(View.GONE);
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
        } else {
            return 0;
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView fecha, monto, documento1, documento2, numeroTarjeta;
        ImageView accion;

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
