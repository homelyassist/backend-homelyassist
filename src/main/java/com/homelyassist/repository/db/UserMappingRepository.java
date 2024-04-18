package com.homelyassist.repository.db;

import com.homelyassist.model.db.UserMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMappingRepository extends JpaRepository<UserMapping, String> {

    UserMapping findByPhoneNumber(String phoneNumber);

    // projection to retrieve only the password field
    interface PasswordOnlyProjection {
        String getPassword();
    }

    Optional<PasswordOnlyProjection> findPasswordByPhoneNumber(String phoneNumber);
}
