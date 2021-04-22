package com.alg.algamoney.api.repository.lancamento;

import com.alg.algamoney.api.model.Lancamento;
import com.alg.algamoney.api.repository.filter.LancamentoFilter;

import java.util.List;

public interface LancamentoRepositoryQuery {

    public List<Lancamento> filtrar(LancamentoFilter filter);
}
