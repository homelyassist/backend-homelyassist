package com.homelyassist.repository.db;

import com.homelyassist.model.db.MaidAssist;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaidAssistRepository extends JpaRepository<MaidAssist, String> {

    boolean existsByPhoneNumber(String phoneNumber);

    List<MaidAssist> findAll(Specification<MaidAssist> specification);
}
