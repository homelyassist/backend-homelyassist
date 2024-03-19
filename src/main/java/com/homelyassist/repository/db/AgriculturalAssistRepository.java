package com.homelyassist.repository.db;

import com.homelyassist.model.db.AgriculturalAssist;
import com.homelyassist.model.enums.AgriculturalAssistType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgriculturalAssistRepository extends JpaRepository<AgriculturalAssist, String> {

    boolean existsByPhoneNumber(String phoneNumber);

    List<AgriculturalAssist> findTop50ByPinCodeAndAgriculturalAssistTypesInAndCityAreaIgnoreCaseStartingWithAndActiveIsTrue(String pinCode, List<AgriculturalAssistType> assistTypes, String cityArea);
}
