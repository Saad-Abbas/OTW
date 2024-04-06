package com.example.otwAppservice.repository;

import com.example.otwAppservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByPhoneNumber(String phoneNumber);

    User findUserById(Long userId);
}
