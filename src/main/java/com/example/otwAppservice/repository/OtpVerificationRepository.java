package com.example.otwAppservice.repository;

import com.example.otwAppservice.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    public OtpVerification findByPhoneNumberAndIsActiveOrderByIdDesc(String phoneNumber, int isActive);
}
