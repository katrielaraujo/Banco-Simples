package com.imd.banco.service;

import com.imd.banco.model.Conta;
import com.imd.banco.repository.ContaRepository;

public class ContaService {
    private final ContaRepository repository = new ContaRepository();

    public boolean cadastrarConta(int numero){
        if(repository.existe(numero)){
            return false;
        }
        Conta conta = new Conta(numero);
        repository.salvar(conta);
        return true;
    }

    public Conta buscarConta(int numero){
        return repository.buscar(numero);
    }

    public Double consultarSaldo(int numero){
        Conta conta = repository.buscar(numero);
        if(conta == null){
            return null;
        }
        return conta.getSaldo();
    }

    public boolean creditar(int numero, double valor){
        Conta conta = repository.buscar(numero);
        if(conta == null){
            return false;
        }
        conta.creditar(valor);
        return true;
    }

    public boolean debitar(int numero, double valor){
        Conta conta = repository.buscar(numero);
        if (conta == null){
            return false;
        }

        if(conta.getSaldo() < valor){
            return false; // Saldo insuficiente
        }
        conta.debitar(valor);
        return true;
    }

    public boolean transferir(int origem, int destino, double valor){
        Conta contaOrigem = repository.buscar(origem);
        Conta contaDestino = repository.buscar(destino);

        if(contaOrigem == null || contaDestino == null){
            return false;
        }

        if(contaOrigem.getSaldo() < valor){
            return false; // Saldo insuficiente
        }

        contaOrigem.debitar(valor);
        contaDestino.creditar(valor);
        return true;
    }
}
