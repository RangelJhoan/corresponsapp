package com.example.corresponsapp.interfacesgraficas.adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.corresponsapp.R;
import com.example.corresponsapp.entidades.Menu;
import com.example.corresponsapp.interfaces.MenuCallback;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolderDatos> {

    ArrayList<Menu> listaMenu;
    MenuCallback callback;

    public MenuAdapter(ArrayList<Menu> listaMenu, MenuCallback callback) {
        this.listaMenu = listaMenu;
        this.callback = callback;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_botonesmenu, null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        Menu menu = listaMenu.get(position);
        holder.ibBoton.setImageResource(listaMenu.get(position).getIdentificadorRecurso());
        holder.tvBoton.setText(listaMenu.get(position).getNombre());
        holder.ibBoton.setOnClickListener(view -> {
            callback.navegarFragment(menu);
        });
        holder.llItemList.setOnClickListener(view -> {
            callback.navegarFragment(menu);
        });
    }

    @Override
    public int getItemCount() {
        return listaMenu.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        ImageButton ibBoton;
        TextView tvBoton;
        CardView llItemList;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);

            ibBoton = itemView.findViewById(R.id.ibBoton);
            tvBoton = itemView.findViewById(R.id.tvBoton);
            llItemList = itemView.findViewById(R.id.llItemList);

        }
    }
}
