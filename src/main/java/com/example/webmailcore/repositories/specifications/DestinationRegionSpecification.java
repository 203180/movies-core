package com.example.webmailcore.repositories.specifications;

import com.example.webmailcore.models.DestinationRegion;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DestinationRegionSpecification implements Specification<DestinationRegion> {
    private List<SearchCriteria> list;

    public DestinationRegionSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<DestinationRegion> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return new SpecificationHelper<DestinationRegion>().resolvePredicate(list, root, query, builder);
    }
}