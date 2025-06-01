package com.imd.banco.model;

import com.imd.banco.model.Conta;

public class ContaPoupanca extends Conta{

    public ContaPoupanca(int numero) {
        super(numero);
    }

    public void renderJuros(double taxaPercentual){
        double saldo = getSaldo();
        double juros = saldo * (taxaPercentual/ 100.0);
        creditar(juros);
    }
}
