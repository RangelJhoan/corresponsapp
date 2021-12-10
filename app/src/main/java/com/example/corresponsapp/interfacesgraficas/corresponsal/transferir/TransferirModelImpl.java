package com.example.corresponsapp.interfacesgraficas.corresponsal.transferir;

import android.content.Context;

import com.example.corresponsapp.basedatos.BaseDatos;
import com.example.corresponsapp.entidades.Transferencia;
import com.example.corresponsapp.utilidades.Constantes;
import com.example.corresponsapp.utilidades.Sesion;

public class TransferirModelImpl implements TransferirMVP.Model {

    private TransferirMVP.Presenter presenter;
    private BaseDatos baseDatos;

    public TransferirModelImpl(TransferirMVP.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void transferirDinero(Context context, Transferencia transferencia) {
        baseDatos = BaseDatos.getInstance(context);
        //Verificamos que el cliente a transferir exista
        if (baseDatos.consultarIdCliente(transferencia.getCuentaTransfiere().getCliente().getDocumento()) > 0) {
            //Verificamos que el cliente que recibe exista
            if (baseDatos.consultarIdCliente(transferencia.getCuentaRecibe().getCliente().getDocumento()) > 0) {
                //Verificamos que el PIN ingresado sea el mismo de la cuenta
                if (baseDatos.consultarPINCuenta(transferencia.getCuentaTransfiere().getCliente().getDocumento()).equals(transferencia.getCuentaTransfiere().getPIN())) {
                    //Verificamos que el cliente a transferir tenga cuenta bancaria
                    long idCuentaTransfiere = baseDatos.consultarIdCuentaDocumento(transferencia.getCuentaTransfiere().getCliente().getDocumento());
                    if ( idCuentaTransfiere > 0) {
                        //Verificamos que el cliente a receibir tenga cuenta bancaria
                        long idCuentaRecibe = baseDatos.consultarIdCuentaDocumento(transferencia.getCuentaRecibe().getCliente().getDocumento());
                        if (idCuentaRecibe > 0) {
                            //Verificamos que el cliente tenga dinero para la transferencia y si tiene, retiramos el dinero de su cuenta
                            if (baseDatos.retirarDinero((int) idCuentaTransfiere, transferencia.getMonto(), Constantes.COMISION_TRANSFERIR) > 0) {
                                //Verificamos que el corresponsal haya obtenido la comisión
                                if(baseDatos.registrarComision(Sesion.corresponsalSesion.getId(), Constantes.COMISION_TRANSFERIR) > 0){
                                    //Verificamos que el cliente haya recibido el dinero
                                    if(baseDatos.depositarDinero((int) idCuentaRecibe, transferencia.getMonto(), 0) > 0){
                                        //Verificamos que se registre la transferencia
                                        transferencia.getCuentaTransfiere().setId((int) idCuentaTransfiere);
                                        transferencia.getCuentaRecibe().setId((int) idCuentaRecibe);
                                        if(baseDatos.crearTransferencia(transferencia) > 0){
                                            presenter.mostrarResultado("Transferencia realizada correctamente");
                                        }else{
                                            presenter.mostrarError("No se pudo realizar el registro de la transferencia");
                                        }
                                    }else{
                                        presenter.mostrarError("No se pudo realizar la transferencia");
                                    }
                                }else{
                                    presenter.mostrarError("No se le pudo consignar la comisión al corresponsal");
                                }
                            } else {
                                presenter.mostrarError("Saldo insuficiente para la transferencia");
                            }
                        }else{
                            presenter.mostrarError("Cliente que recibe no tiene una cuenta asociada");
                        }
                    } else {
                        presenter.mostrarError("Cliente que transfiere no tiene una cuenta asociada");
                    }
                } else {
                    presenter.mostrarError("Código PIN incorrecto");
                }
            } else {
                presenter.mostrarError("Documento del cliente que recibe incorrecto");
            }
        } else {
            presenter.mostrarError("Documento del cliente que transfiere incorrecto");
        }
    }
}
