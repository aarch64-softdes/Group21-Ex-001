package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Citizenship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CitizenshipRepository extends JpaRepository<Citizenship, String> {
    Optional<Citizenship> findCitizenshipByCountryNameEqualsIgnoreCase(String normalizedName);
}
