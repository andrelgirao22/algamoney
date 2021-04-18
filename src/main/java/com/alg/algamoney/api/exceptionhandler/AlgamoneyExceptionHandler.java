package com.alg.algamoney.api.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String mensagemDesenvolvedor = ex.getMessage().toString();
        String mensagem = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
        List<Erro> lista = Arrays.asList(new Erro(mensagem, mensagemDesenvolvedor));
        return  handleExceptionInternal(ex,lista, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<Erro> lista = this.criarListaErros(ex.getBindingResult());

        return  handleExceptionInternal(ex,lista, headers, HttpStatus.BAD_REQUEST, request);
    }

    private List<Erro> criarListaErros(BindingResult bindingResult) {
        List<Erro> lista = new ArrayList<>();

        for (FieldError error: bindingResult.getFieldErrors()) {
            String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
            String mensagemDesenvolvedor = error.toString();
            lista.add(new Erro(mensagem,mensagemDesenvolvedor));
        }


        return lista;
    }

    @Data
    @AllArgsConstructor
    public static class Erro {
        private String mensagemUsuario;
        private String mensagemDesenvolvedor;
    }
}
