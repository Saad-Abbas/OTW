package com.example.otwAppservice.repository;

import com.example.otwAppservice.entity.DeletedUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedUserRepository extends JpaRepository<DeletedUsers, Long> {


}
