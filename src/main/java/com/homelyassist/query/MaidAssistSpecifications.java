package com.homelyassist.query;

import com.homelyassist.model.db.AgriculturalAssist;
import com.homelyassist.model.db.MaidAssist;
import com.homelyassist.model.enums.AgriculturalAssistType;
import com.homelyassist.model.enums.MaidAssistType;
import com.homelyassist.model.rest.request.SearchAssistRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MaidAssistSpecifications {

    public static Specification<MaidAssist> findBySearchParams(SearchAssistRequestDto<MaidAssistType> requestDto) {
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
            if (requestDto.getAssistTypes() != null && !requestDto.getAssistTypes().isEmpty()) {
                predicates.add(root.join("maidAssistTypes").in(requestDto.getAssistTypes()));
            }

            predicates.add(cb.isTrue(root.get("active")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

