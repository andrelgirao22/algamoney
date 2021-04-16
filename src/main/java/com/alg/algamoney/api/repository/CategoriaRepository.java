package com.alg.algamoney.api.repository;

import com.alg.algamoney.api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
