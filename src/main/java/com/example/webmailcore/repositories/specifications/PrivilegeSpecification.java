package com.example.webmailcore.repositories.specifications;

import com.example.webmailcore.models.Privilege;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class PrivilegeSpecification implements Specification<Privilege> {

    private List<SearchCriteria> list;

    public PrivilegeSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Privilege> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return new SpecificationHelper<Privilege>().resolvePredicate(list, root, query, builder);
    }
}