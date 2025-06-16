package com.imd.banco.service;

import org.springframework.stereotype.Service;

import com.imd.banco.model.Conta;
import com.imd.banco.model.ContaBonus;
import com.imd.banco.model.ContaPoupanca;
import com.imd.banco.repository.ContaRepository;
import java.util.Collection;

@Service
public class ContaService {
    private final ContaRepository repository;

    public ContaService(ContaRepository repository){
        this.repository = repository;
    }

    public boolean cadastrarConta(int numero, String tipo,double saldoInicial){

        if(repository.buscar(numero) != null) return false;

        Conta conta;

        switch(tipo.toLowerCase()){
            case "bonus":
                conta = new ContaBonus(numero);
                break;
            case "poupanca":
                conta = new ContaPoupanca(numero, saldoInicial);
                break;
            case "simples":
                conta = new Conta(numero);
                conta.creditar(saldoInicial);
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

    public Collection<Conta> listarTodasAsContas(){
        return repository.listarTodas();
    }

    public Double consultarSaldo(int numero){
        Conta conta = repository.buscar(numero);
        return (conta != null) ? conta.getSaldo() : null;
    }

    public boolean creditar(int numero, double valor){
        if(valor < 0) return false;

        Conta conta = repository.buscar(numero);
        if(conta == null)  return false;

        if(conta instanceof ContaBonus cb){
            cb.creditar(valor);
        } else {
            conta.creditar(valor);
        }
        
        return true;
    }

    public boolean debitar(int numero, double valor){
        if (valor < 0) return false;

        Conta conta = repository.buscar(numero);

        if (conta == null) return false;

        if((conta instanceof Conta || conta instanceof ContaBonus) && (conta.getSaldo() - valor < -1000)){
            return false;
        }

        conta.debitar(valor);
        return true;
    }

    public boolean transferir(int origem, int destino, double valor){
        if (valor < 0) return false;
        
        Conta contaOrigem = repository.buscar(origem);
        Conta contaDestino = repository.buscar(destino);

        if(contaOrigem == null || contaDestino == null) return false;

        if((contaOrigem instanceof Conta || contaOrigem instanceof ContaBonus) && (contaOrigem.getSaldo() - valor < -1000)){
            return false;
        }

        contaOrigem.debitar(valor);

        if(contaDestino instanceof ContaBonus cb){
            cb.receberTransferencia(valor);
        } else {
            contaDestino.creditar(valor);
        }
        
        return true;
    }

    public void renderJurosPoupanca(double taxa){
        for (Conta conta : repository.listarTodas()) {
            if (conta instanceof ContaPoupanca cp) {
                cp.renderJuros(taxa);
            }
        }
    }
}
