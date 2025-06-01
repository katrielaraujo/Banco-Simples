package com.imd.banco.model;

public class Conta {
    private int numero;
    private double saldo;

    public Conta(int numero){
        this.numero = numero;
        this.saldo = 0.0;
    }

    public int getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public void creditar(double valor){
        this.saldo += valor;
    }

    public void debitar(double valor){
        this.saldo -= valor;
    }
}
