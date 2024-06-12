package com.homelyassist.repository.db;

import com.homelyassist.model.db.ElectricalAssist;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectricalAssistRepository extends JpaRepository<ElectricalAssist, String> {

    boolean existsByPhoneNumber(String phoneNumber);

    List<ElectricalAssist> findAll(Specification<ElectricalAssist> specification);
}
