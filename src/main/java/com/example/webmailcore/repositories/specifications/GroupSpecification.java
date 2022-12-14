package com.example.webmailcore.repositories.specifications;

import com.example.webmailcore.models.Group;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class GroupSpecification implements Specification<Group> {
    private List<SearchCriteria> list;

    public GroupSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Group> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return new SpecificationHelper<Group>().resolvePredicate(list, root, query, builder);
    }
}
