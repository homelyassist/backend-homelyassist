package com.homelyassist.repository.db;

import com.homelyassist.model.db.AgriculturalAssist;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgriculturalAssistRepository extends JpaRepository<AgriculturalAssist, String> {

    boolean existsByPhoneNumber(String phoneNumber);

    List<AgriculturalAssist> findAll(Specification<AgriculturalAssist> specification);
}
