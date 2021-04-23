package com.alg.algamoney.api.resource;

import com.alg.algamoney.api.event.RecursoCriadoEvent;
import com.alg.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler;
import com.alg.algamoney.api.exceptions.PessoaInexistenteOuInativaException;
import com.alg.algamoney.api.model.Lancamento;
import com.alg.algamoney.api.repository.filter.LancamentoFilter;
import com.alg.algamoney.api.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/lancamento")
public class LancamentoResource {

    @Autowired
    private LancamentoService service;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private MessageSource messageSource;

    @GetMapping
    public ResponseEntity<?> pesquisar(LancamentoFilter filter, Pageable pageable) {
        return ResponseEntity.ok(this.service.pesquisar(filter, pageable));
    }

    @PostMapping
    public ResponseEntity<?> salvar(@Valid  @RequestBody Lancamento lancamento, HttpServletResponse response) {
        lancamento = this.service.salvar(lancamento);
        this.publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamento.getCodigo()));
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamento);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Lancamento> buscarPelaCodigo(@PathVariable Long codigo) {
        Lancamento lancamento = this.service.buscarPeloCodigo(codigo);
        return ResponseEntity.ok(lancamento);
    }


    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> deletar(@PathVariable Long codigo) {
        this.service.deletar(codigo);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex, WebRequest request) {
        String mensagemDesenvolvedor = ex.toString();
        String mensagem = messageSource.getMessage("pessoa.inexistente-inativa", null, LocaleContextHolder.getLocale());
        List<AlgamoneyExceptionHandler.Erro> lista = Arrays.asList(new AlgamoneyExceptionHandler.Erro(mensagem, mensagemDesenvolvedor));
        return  ResponseEntity.badRequest().body(lista);
    }

}
