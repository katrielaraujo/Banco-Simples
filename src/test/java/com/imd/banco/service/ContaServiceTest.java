package com.imd.banco.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import com.imd.banco.model.Conta;
import com.imd.banco.model.ContaBonus;
import com.imd.banco.model.ContaPoupanca;


public class ContaServiceTest {

    private ContaService contaService;

    @BeforeEach
    void setup(){
        contaService = new ContaService(new com.imd.banco.repository.ContaRepository());
    }

    @Test
    @DisplayName("Deve ser possível cadastrar uma conta do tipo simples com sucesso")
    void deveCadastrarContaSimples(){
        boolean resultado = contaService.cadastrarConta(1,"simples", 0.0);
        assertTrue(resultado);
        assertInstanceOf(Conta.class, contaService.buscarConta(1));
    }

    @Test
    @DisplayName("Deve ser possível consultar o saldo de uma conta")
    void deveConsultarSaldoConta(){
        contaService.cadastrarConta(1,"simples", 0.0);
        assertEquals(0.0,contaService.consultarSaldo(1));
    }

    @Test
    @DisplayName("Deve ser possível cadastrar uma conta do tipo Bônus com sucesso")
    void deveCadastrarContaBonus(){
        boolean resultado = contaService.cadastrarConta(2,"bonus",0.0);
        assertTrue(resultado);
        assertInstanceOf(ContaBonus.class, contaService.buscarConta(2));
    }

    @Test
    @DisplayName("Conta bônus deve ter 10 pontos de bonus inicial")
    void contaBonusDeveTer10PontosBonusInicial(){
        contaService.cadastrarConta(2,"bonus",0.0);
        ContaBonus contaBonus = (ContaBonus) contaService.buscarConta(2);
        double bonus = contaBonus.getPontos();
        assertEquals(10.0, bonus);
    }

    @Test
    @DisplayName("Deve ser possível cadastrar uma conta do tipo Poupança com sucesso")
    void deveCadastrarContaPoupanca(){
        boolean resultado = contaService.cadastrarConta(3,"poupanca",200.0);
        assertTrue(resultado);
        assertInstanceOf(ContaPoupanca.class, contaService.buscarConta(3));
    }

    @Test
    @DisplayName("Não deve ser possível cadastrar conta duplicada")
    void naoDeveCadastrarContaDuplicada(){
        contaService.cadastrarConta(0, "simples",0.0);
        boolean resultado = contaService.cadastrarConta(0, "simples",0.0);
        assertFalse(resultado);
    }

    //Creditar testes

    @Test
    @DisplayName("Deve ser possível creditar valor em uma conta")
    void deveCreditarValor(){
        contaService.cadastrarConta(0, "simples",0.0);
        contaService.creditar(0, 100.0);
        assertEquals(100.0, contaService.consultarSaldo(0));
    }

    @Test
    @DisplayName("Não deve ser possível creditar valor negativo em uma conta")
    void naoDeveCreditarValorNegativo(){
        contaService.cadastrarConta(0, "simples",0.0);
        boolean resultado = contaService.creditar(0, -100.0);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve adicionar pontos na conta bonus ao creditar")
    void deveAdicionarPontosNaContaBonus(){
        contaService.cadastrarConta(0, "bonus",0.0);
        double valor = 100.0;
        int pontos = (int) (valor / 100);
        contaService.creditar(0, valor);
        ContaBonus contaBonus = (ContaBonus) contaService.buscarConta(0);
        assertEquals(pontos + 10, contaBonus.getPontos());
    }

    // Débitar testes

    @Test
    @DisplayName("Deve ser possível debitar valor em uma conta")
    void deveDebitarValor(){
        contaService.cadastrarConta(0, "simples",0.0);
        contaService.creditar(0, 20);
        contaService.debitar(0, 10);
        assertEquals(10.0, contaService.consultarSaldo(0));
    }

    @Test
    @DisplayName("Não deve ser possível debitar valor negativo em uma conta")
    void naoDeveDebitarValorNegativo(){
        contaService.cadastrarConta(0, "simples",0.0);
        boolean resultado = contaService.debitar(0, -100.0);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Não deve ser possível debitar valor maior que o saldo em uma conta")
    void naoDevePermitirSaldoNegativo(){
        contaService.cadastrarConta(0,"simples",0.0);
        boolean resultado = contaService.debitar(0, 1001);
        assertFalse(resultado);
    }

    //Transferencia testes

    @Test
    @DisplayName("Não deve ser possível transferir valor Negativo")
    void naoDevePermitirTransferenciaValorNegativo(){
        contaService.cadastrarConta(0,"poupanca",1000);
        contaService.cadastrarConta(1,"simples",0.0);
        boolean resultado = contaService.transferir(0, 1, -100.0);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Não deve ser possível transferir valor maior que o saldo")
    void naoDevePermitirTransferenciaSaldoNegativo(){
        contaService.cadastrarConta(0, "simples", 0);
        contaService.cadastrarConta(1, "simples", 0);
        boolean resultado = contaService.transferir(0, 1, 1001);
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve adicionar pontos na conta bonus realiza")
    void deveAdicionarPontosTransferenciaContaBonus(){
        contaService.cadastrarConta(0, "bonus",0.0);
        contaService.cadastrarConta(1, "simples",0.0);
        double valor = 100.0;
        int pontos = (int) (valor / 150);
        contaService.transferir(0, 1, valor);
        ContaBonus contaBonus = (ContaBonus) contaService.buscarConta(0);
        assertEquals(10 + pontos, contaBonus.getPontos());
    }

    //Render Juros
    @Test
    @DisplayName("Deve render juros na conta poupança")
    void deveRenderJurosPoupanca(){
        contaService.cadastrarConta(0, "poupanca", 1000);
        contaService.cadastrarConta(1, "poupanca", 500);
        contaService.cadastrarConta(2, "poupanca", 1500);
        contaService.renderJurosPoupanca(10.0);
        assertEquals(1100.0, contaService.consultarSaldo(0));
        assertEquals(550.0, contaService.consultarSaldo(1));
        assertEquals(1650.0, contaService.consultarSaldo(2));
    }
}
