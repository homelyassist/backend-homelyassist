package com.homelyassist.query;

import com.homelyassist.model.db.ElectricalAssist;
import com.homelyassist.model.rest.request.SearchAssistRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ElectricalAssistSpecifications {

    public static Specification<ElectricalAssist> findBySearchParams(SearchAssistRequestDto<ElectricalAssist> requestDto) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add predicates based on the presence of state, district, block, village, and assistTypes
            if (requestDto.getState() != null) {
                predicates.add(cb.equal(root.get("state"), requestDto.getState()));
            }
            if (requestDto.getDistrict() != null && !requestDto.getDistrict().isBlank()) {
                predicates.add(cb.equal(root.get("district"), requestDto.getDistrict()));
            }
            if (requestDto.getBlock() != null && !requestDto.getBlock().isBlank()) {
                predicates.add(cb.equal(root.get("block"), requestDto.getBlock()));
            }
            if (requestDto.getVillage() != null && !requestDto.getVillage().isBlank()) {
                predicates.add(cb.equal(root.get("village"), requestDto.getVillage()));
            }

            predicates.add(cb.isTrue(root.get("active")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

