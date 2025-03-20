package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, String> {
    boolean existsIdentityByNumberAndType(String number, String type);

    boolean existsIdentityByNumberAndTypeAndIdNot(String number, String type, String id);
}
