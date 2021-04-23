package com.alg.algamoney.api.repository.lancamento;

import com.alg.algamoney.api.model.Lancamento;
import com.alg.algamoney.api.repository.filter.LancamentoFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class LancamentoRepositoryQueryImpl implements LancamentoRepositoryQuery {

    @PersistenceContext
    private EntityManager manager;

    public Page<Lancamento> filtrar(LancamentoFilter filter, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = this.manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteriaQuery = criteriaBuilder.createQuery(Lancamento.class);
        Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

        //Restricoes
        Predicate [] predicates = criarRestricoes(filter, criteriaBuilder, root);
        criteriaQuery.where(predicates);

        TypedQuery<Lancamento> typedQuery = manager.createQuery(criteriaQuery);
        adicionarRestricoesDePaginacao(typedQuery, pageable);


        return new PageImpl(typedQuery.getResultList(), pageable, total(filter));
    }

    private Predicate[] criarRestricoes(LancamentoFilter filter, CriteriaBuilder criteriaBuilder, Root<Lancamento> root) {

        List<Predicate> predicates = new ArrayList<>();

        if(!StringUtils.isEmpty(filter.getDescricao())) {
            predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("descricao")),
                    "%" + filter.getDescricao() + "%"));
        }

        if(filter.getDataVencimentoDe() != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dataVencimento"), filter.getDataVencimentoDe()));
        }

        if(filter.getDataVencimentoAte() != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dataVencimento"), filter.getDataVencimentoAte()));
        }

        return predicates.toArray(new Predicate[predicates.size()]);
    }

    private long total(LancamentoFilter filter) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
        Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

        Predicate [] predicates = this.criarRestricoes(filter, builder, root);
        criteriaQuery.where(predicates);
        criteriaQuery.select(builder.count(root));

        return manager.createQuery(criteriaQuery).getSingleResult();
    }

    private void adicionarRestricoesDePaginacao(TypedQuery<Lancamento> typedQuery, Pageable pageable) {
        int paginaAtual = pageable.getPageNumber();
        int totalRegistrosPorPagina = pageable.getPageSize();
        int primeiroRegistro = paginaAtual * totalRegistrosPorPagina;

        typedQuery.setFirstResult(primeiroRegistro);
        typedQuery.setMaxResults(totalRegistrosPorPagina);
    }
}
