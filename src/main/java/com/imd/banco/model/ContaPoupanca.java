package com.imd.banco.model;

public class ContaPoupanca extends Conta{

    public ContaPoupanca(int numero) {
        super(numero);
    }

    public ContaPoupanca(int numero, double saldoInicial){
        super(numero);
        this.creditar(saldoInicial);
    }
  
    public void renderJuros(double taxaPercentual){
        double saldo = getSaldo();
        double juros = saldo * (taxaPercentual/ 100.0);
        creditar(juros);
    }
}
