package com.alg.algamoney.api.repository.lancamento;

import com.alg.algamoney.api.model.Lancamento;
import com.alg.algamoney.api.repository.filter.LancamentoFilter;
import org.apache.commons.lang3.StringUtils;

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

    public List<Lancamento> filtrar(LancamentoFilter filter) {

        CriteriaBuilder criteriaBuilder = this.manager.getCriteriaBuilder();
        CriteriaQuery<Lancamento> criteriaQuery = criteriaBuilder.createQuery(Lancamento.class);
        Root<Lancamento> root = criteriaQuery.from(Lancamento.class);

        //Restricoes
        Predicate [] predicates = criarRestricoes(filter, criteriaBuilder, root);
        criteriaQuery.where(predicates);

        TypedQuery<Lancamento> typedQuery = manager.createQuery(criteriaQuery);
        return typedQuery.getResultList();
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
}
