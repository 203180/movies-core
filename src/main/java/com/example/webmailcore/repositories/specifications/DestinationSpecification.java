package com.example.webmailcore.repositories.specifications;

import com.example.webmailcore.models.Destination;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DestinationSpecification implements Specification<Destination> {
    private List<SearchCriteria> list;

    public DestinationSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Destination> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return new SpecificationHelper<Destination>().resolvePredicate(list, root, query, builder);
    }
}
