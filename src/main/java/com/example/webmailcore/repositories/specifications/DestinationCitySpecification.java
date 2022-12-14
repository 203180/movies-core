package com.example.webmailcore.repositories.specifications;

import com.example.webmailcore.models.DestinationCity;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DestinationCitySpecification implements Specification<DestinationCity> {
    private List<SearchCriteria> list;

    public DestinationCitySpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<DestinationCity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return new SpecificationHelper<DestinationCity>().resolvePredicate(list, root, query, builder);
    }
}