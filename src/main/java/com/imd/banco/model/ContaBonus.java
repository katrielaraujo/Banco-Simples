package com.imd.banco.model;

public class ContaBonus extends Conta{
    private int pontos;

    public ContaBonus(int numero){
        super(numero);
        this.pontos = 10;
    }

    public int getPontos() {
        return pontos;
    }

    public void adicionarPontos(int pontos) {
        this.pontos += pontos;
    }

    @Override
    public void creditar(double valor) {
        super.creditar(valor);
        int pontosGanhos = (int) (valor / 100);
        adicionarPontos(pontosGanhos);
    }

    public void receberTransferencia(double valor){
        super.creditar(valor);

        int pontosGanhos = (int) (valor / 150);
        adicionarPontos(pontosGanhos);
    }
}
