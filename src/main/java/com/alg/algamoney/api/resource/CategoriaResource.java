package com.alg.algamoney.api.resource;

import com.alg.algamoney.api.model.Categoria;
import com.alg.algamoney.api.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/categoria")
public class CategoriaResource {

    @Autowired
    private CategoriaRepository repository;

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(this.repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@RequestBody Categoria categoria, HttpServletResponse response) {
        categoria = this.repository.save(categoria);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(categoria.getCodigo()).toUri();
        response.setHeader("Location", uri.toASCIIString());
        return ResponseEntity.created(uri).body(categoria);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<Categoria> buscarPelaCodigo(@PathVariable Long codigo) {

        Optional<Categoria> categoria = this.repository.findById(codigo);
        if(categoria.isPresent()) {
            return ResponseEntity.ok(categoria.get());
        }
        return ResponseEntity.notFound().build();
    }

}
