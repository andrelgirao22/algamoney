package com.alg.algamoney.api.resource;

import com.alg.algamoney.api.event.RecursoCriadoEvent;
import com.alg.algamoney.api.exceptions.PessoaNotFoundException;
import com.alg.algamoney.api.model.Pessoa;
import com.alg.algamoney.api.repository.PessoaRepository;
import com.alg.algamoney.api.service.PessoaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/pessoa")
public class PessoaResource {

    @Autowired
    private PessoaRepository repository;

    @Autowired
    private PessoaService service;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(this.repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
        pessoa = this.repository.save(pessoa);
        this.publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoa.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(pessoa);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Pessoa> buscarPelaCodigo(@PathVariable Long codigo) {
        Pessoa pessoa = this.service.buscarPeloCodigo(codigo);
        return ResponseEntity.ok(pessoa);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<Pessoa> atualizar(@PathVariable long codigo, @Valid @RequestBody Pessoa pessoa) {
        Pessoa pessoaSalva = this.service.atualizar(codigo, pessoa);
        return ResponseEntity.ok(pessoaSalva);
    }

    @PutMapping("/{codigo}/ativo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
        this.service.atualizarPropriedadeAtivo(codigo, ativo);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> remover(@PathVariable Long codigo) {
        this.repository.deleteById(codigo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
