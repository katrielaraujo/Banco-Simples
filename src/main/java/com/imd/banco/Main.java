package com.imd.banco;

import com.imd.banco.service.ContaService;
import com.imd.banco.model.ContaBonus;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ContaService contaService = new ContaService();
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while(executando){
            System.out.println("\n====== BANCO INTERATIVO ======");
            System.out.println("1 - Cadastrar Conta");
            System.out.println("2 - Consultar Saldo");
            System.out.println("3 - Credito");
            System.out.println("4 - Debito");
            System.out.println("5 - Transferencia");
            System.out.println("6 - Render Juros (apenas contas poupanca)");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opcao: ");

            int opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    System.out.print("Número da nova conta: ");
                    int numeroConta = scanner.nextInt();
                    scanner.nextLine();

                    double saldoInicial = 0.0;

                    System.out.print("Deseja criar qual tipo de conta: bonus, poupanca ou simples? ");
                    String tipo = scanner.nextLine();

                    if(tipo.equalsIgnoreCase("poupanca")){
                        System.out.print("Saldo inicial da conta poupanca: ");
                        saldoInicial = scanner.nextDouble();
                    }

                    if(contaService.cadastrarConta(numeroConta, tipo,saldoInicial)){
                        System.out.println("Conta "+ tipo + " cadastrada com sucesso!");
                    }else {
                        System.out.println("Erro ao cadastrar conta. Conta já existe.");
                    }

                    break;
                case 2:
                    System.out.print("Número da conta: ");
                    int numConsulta = scanner.nextInt();
                    Double saldo = contaService.consultarSaldo(numConsulta);
                    if(saldo != null){
                        System.out.printf("Saldo: R$ %.2f%n", saldo);
                    }else {
                        System.out.println("Conta não encontrada.");
                    }
                    break;
                case 3:
                    System.out.print("Número da conta: ");
                    int numCredito = scanner.nextInt();
                    System.out.print("Valor do crédito: ");
                    double valorCredito = scanner.nextDouble();
                    if(contaService.creditar(numCredito, valorCredito)){
                        System.out.println("Crédito realizado com sucesso!");
                    }else {
                        System.out.println("Erro ao realizar crédito.");
                    }
                    break;
                case 4:
                    System.out.print("Número da conta: ");
                    int numDebito = scanner.nextInt();
                    System.out.print("Valor do débito: ");
                    double valorDebito = scanner.nextDouble();
                    if(contaService.debitar(numDebito, valorDebito)){
                        System.out.println("Débito realizado com sucesso!");
                    }else {
                        System.out.println("Erro ao realizar débito.");
                    }
                    break;
                case 5:
                    System.out.print("Número da conta de origem: ");
                    int numOrigem = scanner.nextInt();
                    System.out.print("Número da conta de destino: ");
                    int numDestino = scanner.nextInt();
                    System.out.print("Valor da transferência: ");
                    double valorTransferencia = scanner.nextDouble();
                    if(contaService.transferir(numOrigem, numDestino, valorTransferencia)){
                        System.out.println("Transferência realizada com sucesso!");
                    }else {
                        System.out.println("Erro ao realizar transferência.");
                    }
                    break;
                case 6:
                    System.out.println("Informe a taxa de juros (%): ");
                    double taxa = scanner.nextDouble();
                    contaService.renderJurosPoupanca(taxa);
                    System.out.println("Juros aplicados às contas poupanca.");
                    break;
                case 0:
                    executando = false;
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }

        scanner.close();
    }
}