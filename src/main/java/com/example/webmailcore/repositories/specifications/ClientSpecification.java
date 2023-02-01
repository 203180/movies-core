package com.example.webmailcore.repositories.specifications;

import com.example.webmailcore.models.AirplaneCompany;
import com.example.webmailcore.models.Client;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class ClientSpecification implements Specification<Client> {

    private List<SearchCriteria> list;

    public ClientSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return new SpecificationHelper<Client>().resolvePredicate(list, root, query, builder);
    }

}
