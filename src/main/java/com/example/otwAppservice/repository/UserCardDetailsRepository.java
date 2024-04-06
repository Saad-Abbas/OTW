package com.example.otwAppservice.repository;

import com.example.otwAppservice.entity.UserCardDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCardDetailsRepository extends JpaRepository<UserCardDetails, Long> {

    public UserCardDetails findUserCardDetailsByUserId(Long userId);
}
