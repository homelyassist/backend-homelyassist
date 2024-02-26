package com.homelyassist.repository.db;

import com.homelyassist.model.db.UserMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMappingRepository extends JpaRepository<UserMapping, String> {

    UserMapping findByPhoneNumber(String phoneNumber);
}
