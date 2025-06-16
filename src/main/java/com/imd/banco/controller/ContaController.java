package com.imd.banco.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.imd.banco.dto.ContaDTO;
import com.imd.banco.dto.ContaDetalhadaDTO;
import com.imd.banco.dto.TransferenciaDTO;
import com.imd.banco.model.Conta;
import com.imd.banco.model.ContaBonus;
import com.imd.banco.service.ContaService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/banco/conta")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<String> cadastrarConta(@RequestBody ContaDTO dto){
        boolean criada = contaService.cadastrarConta(dto.numero(), dto.tipo(), dto.saldoInicial());
        if(criada){
            return ResponseEntity.status(HttpStatus.CREATED).body("Conta criada com sucesso.");
        }else{
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conta já existe.");
        }
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<Double> consultarSaldo(@PathVariable int id){
        Double saldo = contaService.consultarSaldo(id);
        return saldo != null ? ResponseEntity.ok(saldo) : ResponseEntity.notFound().build();
    }

    
    @PutMapping("/{id}/credito")
    public ResponseEntity<String> creditar(@PathVariable int id, @RequestParam double valor){
       return contaService.creditar(id, valor)
        ? ResponseEntity.ok("Crédito realizado.")
        : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao creditar.");
    }

    @PutMapping("/{id}/debito")
    public ResponseEntity<String> debitar(@PathVariable int id, @RequestParam double valor){
        return contaService.debitar(id, valor)
            ? ResponseEntity.ok("Débito realizado.")
            : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao debitar.");
    
    }

    @PutMapping("/transferencia")
    public ResponseEntity<String> transferir(@RequestBody TransferenciaDTO dto){
        return contaService.transferir(dto.numeroContaOrigem(), dto.numeroContaDestino(), dto.valor())
            ? ResponseEntity.ok("Transferência realizada")
            : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao transferir.");
    }

    @PutMapping("/rendimento")
    public ResponseEntity<String> renderJuros(@RequestParam double taxa){
        contaService.renderJurosPoupanca(taxa);
        return ResponseEntity.ok("Rendimento aplicado.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaDetalhadaDTO> consultarConta(@PathVariable int id){
        Conta conta = contaService.buscarConta(id);
        if(conta == null) return ResponseEntity.notFound().build();

        String tipo = conta.getClass().getSimpleName();
        Integer bonus = (conta instanceof ContaBonus cb) ? cb.getPontos() : null;
        ContaDetalhadaDTO dto = new ContaDetalhadaDTO(tipo, conta.getNumero(),conta.getSaldo(), bonus);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<ContaDetalhadaDTO>> listarTodasAsContas(){
        List<ContaDetalhadaDTO> contasDTO = contaService.listarTodasAsContas().stream()
            .map(conta -> {
                String tipo = conta.getClass().getSimpleName();
                Integer bonus = (conta instanceof ContaBonus cb) ? cb.getPontos() : null;
                return new ContaDetalhadaDTO(tipo, conta.getNumero(), conta.getSaldo(), bonus);
            }).collect(Collectors.toList());
        return ResponseEntity.ok(contasDTO);
    }
}
