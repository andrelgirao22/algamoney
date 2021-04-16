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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String mensagemDesenvolvedor = ex.getMessage().toString();
        String mensagem = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
        return  handleExceptionInternal(ex,new Erro(mensagem, mensagemDesenvolvedor), headers, HttpStatus.BAD_REQUEST, request);
    }


    @Data
    @AllArgsConstructor
    public static class Erro {
        private String mensagemUsuario;
        private String mensagemDesenvolvedor;
    }
}
