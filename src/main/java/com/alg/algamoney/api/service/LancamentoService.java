package com.alg.algamoney.api.service;

import com.alg.algamoney.api.exceptions.LancamentoNotFoundException;
import com.alg.algamoney.api.exceptions.PessoaInexistenteOuInativaException;
import com.alg.algamoney.api.model.Lancamento;
import com.alg.algamoney.api.model.Pessoa;
import com.alg.algamoney.api.repository.LancamentoRepository;
import com.alg.algamoney.api.repository.PessoaRepository;
import com.alg.algamoney.api.repository.filter.LancamentoFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LancamentoService {

    @Autowired
    private LancamentoRepository repository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public List<Lancamento> pesquisar(LancamentoFilter filter) {
        return this.repository.filtrar(filter);
    }

    public Lancamento salvar(Lancamento lancamento) {

        Optional<Pessoa> pessoaOptional = this.pessoaRepository.findById(lancamento.getPessoa().getCodigo());
        if (!pessoaOptional.isPresent() || pessoaOptional.get().isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }

        return this.repository.save(lancamento);
    }

    public Lancamento buscarPeloCodigo(Long codigo) {
        return this.repository.findById(codigo)
                .orElseThrow(() -> new LancamentoNotFoundException());
    }

    public void deletar(Long codigo) {
        Lancamento lancamento = this.buscarPeloCodigo(codigo);
        this.repository.delete(lancamento);
    }
}
