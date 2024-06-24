package com.homelyassist.repository.db;

import com.homelyassist.model.db.ConstructionAssist;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstructionAssistRepository extends JpaRepository<ConstructionAssist, String> {

    boolean existsByPhoneNumber(String phoneNumber);

    List<ConstructionAssist> findAll(Specification<ConstructionAssist> specification);
}
