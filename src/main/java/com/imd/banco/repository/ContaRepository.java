package com.imd.banco.repository;

import com.imd.banco.model.Conta;

import java.util.HashMap;
import java.util.Map;

public class ContaRepository {
    private final Map<Integer, Conta> contas = new HashMap<>();

    public void salvar(Conta conta){
        contas.put(conta.getNumero(),conta);
    }

    public Conta buscar(int numero){
        return contas.get(numero);
    }

    public boolean existe(int numero){
        return contas.containsKey(numero);
    }

    public Map<Integer, Conta> listarTodas(){
        return contas;
    }
}
