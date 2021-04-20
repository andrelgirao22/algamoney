package com.alg.algamoney.api.service;

import com.alg.algamoney.api.exceptions.PessoaNotFoundException;
import com.alg.algamoney.api.model.Pessoa;
import com.alg.algamoney.api.repository.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository repository;

    public Pessoa atualizar(Long codigo, Pessoa pessoa) {
        Pessoa pessoaSalva = this.buscarPeloCodigo(codigo);
        BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
        return this.repository.save(pessoaSalva);
    }

    public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
        Pessoa pessoaSalva = this.buscarPeloCodigo(codigo);
        pessoaSalva.setAtivo(ativo);
        this.repository.save(pessoaSalva);
    }

    public Pessoa buscarPeloCodigo(Long codigo) {
        return this.repository.findById(codigo)
                .orElseThrow(() -> new PessoaNotFoundException());
    }
}
