package com.example.corresponsapp.interfaces;

import java.util.Hashtable;

public interface IAbrirDialogo {
    void abrirDialogo(Hashtable<String,String> informacion, ConfirmacionCallback callback);
}
