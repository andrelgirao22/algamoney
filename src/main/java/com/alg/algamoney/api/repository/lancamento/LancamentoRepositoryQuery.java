package com.alg.algamoney.api.repository.lancamento;

import com.alg.algamoney.api.model.Lancamento;
import com.alg.algamoney.api.repository.filter.LancamentoFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LancamentoRepositoryQuery {

    public Page<Lancamento> filtrar(LancamentoFilter filter, Pageable pageable);
}
