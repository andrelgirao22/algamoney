package com.alg.algamoney.api.repository;

import com.alg.algamoney.api.model.Lancamento;
import com.alg.algamoney.api.repository.lancamento.LancamentoRepositoryQuery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {
}
