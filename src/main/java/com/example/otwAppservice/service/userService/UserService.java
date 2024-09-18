package com.example.otwAppservice.service.userService;

import com.example.otwAppservice.dto.UserDetailsDTO;
import com.example.otwAppservice.entity.DeletedUsers;
import com.example.otwAppservice.entity.User;
import com.example.otwAppservice.entity.UserCardDetails;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {


    User getUserByPhoneNumber(String phoneNumber);

    User getUserById(Long userId);

    UserCardDetails getUserCardDetailsByUserId(Long userId);

    UserCardDetails getUserCardDetailsByUserIdAndIsActive(Long userId, boolean isActive);

    User saveUser(User user);

    void deleteUser(User user);

    void deleteUserCardDetails(UserCardDetails userCardDetails);

    UserCardDetails saveUserCardDetails(UserCardDetails userCardDetails);

    DeletedUsers saveRecordToDeletedUsers(DeletedUsers deletedUsers);

    UserCardDetails saveAndGetUserWithDetails(UserDetailsDTO userDetailsDTO, User user);
}
