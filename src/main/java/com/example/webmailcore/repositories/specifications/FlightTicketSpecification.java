package com.example.webmailcore.repositories.specifications;

import com.example.webmailcore.models.Client;
import com.example.webmailcore.models.FlightTicket;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class FlightTicketSpecification implements Specification<FlightTicket> {
    private List<SearchCriteria> list;

    public FlightTicketSpecification() {
        this.list = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        list.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<FlightTicket> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        return new SpecificationHelper<FlightTicket>().resolvePredicate(list, root, query, builder);
    }



}
