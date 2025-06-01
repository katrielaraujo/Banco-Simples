package com.imd.banco.service;

import com.imd.banco.model.Conta;
import com.imd.banco.model.ContaBonus;
import com.imd.banco.model.ContaPoupanca;
import com.imd.banco.repository.ContaRepository;

public class ContaService {
    private final ContaRepository repository = new ContaRepository();

    public boolean cadastrarConta(int numero){
        return cadastrarConta(numero);
    }

    public boolean cadastrarConta(int numero, String tipo,double saldoInicial){
        if(repository.buscar(numero) != null){
            return false;
        }

        Conta conta;

        switch(tipo.toLowerCase()){
            case "bonus":
                conta = new ContaBonus(numero);
                break;
            case "poupanca":
                conta = new ContaPoupanca(numero);
                break;
            case "simples":
                conta = new Conta(numero);
                conta.creditar(0.0);
                break;
            default:
                return false; // Tipo de conta inválido
        }

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
        if(valor < 0) return false;

        Conta conta = repository.buscar(numero);
        if(conta == null)  return false;

        if(conta instanceof ContaBonus){
            ((ContaBonus) conta).creditar(valor);
        }

        conta.creditar(valor);
        return true;
    }

    public boolean debitar(int numero, double valor){
        if (valor < 0) return false;

        Conta conta = repository.buscar(numero);
        if (conta == null || conta.getSaldo() < valor) return false;

        conta.debitar(valor);
        return true;
    }

    public boolean transferir(int origem, int destino, double valor){
        if (valor < 0) return false;
        
        Conta contaOrigem = repository.buscar(origem);
        Conta contaDestino = repository.buscar(destino);

        if(contaOrigem == null || contaDestino == null) return false;

        if(contaOrigem.getSaldo() < valor) return false;

        contaOrigem.debitar(valor);

        if(contaDestino instanceof ContaBonus){
            ((ContaBonus) contaDestino).receberTransferencia(valor);
        } else {
            contaDestino.creditar(valor);
        }
        
        return true;
    }

    public void renderJurosPoupanca(double taxa){
        for (Conta conta : repository.listarTodas().values()) {
            if (conta instanceof ContaPoupanca) {
                ((ContaPoupanca) conta).renderJuros(taxa);
            }
        }
    }
}
