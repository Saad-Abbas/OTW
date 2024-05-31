package com.example.otwAppservice.service.userService;

import com.example.otwAppservice.entity.DeletedUsers;
import com.example.otwAppservice.entity.User;
import com.example.otwAppservice.entity.UserCardDetails;

public interface UserService {


    User getUserByPhoneNumber(String phoneNumber);
    User getUserById(Long userId);

    UserCardDetails getUserCardDetailsByUserId(Long userId);

    User saveUser(User user);
    void deleteUser(User user);
    void deleteUserCardDetails(UserCardDetails userCardDetails);

    UserCardDetails saveUserCardDetails(UserCardDetails userCardDetails);
    DeletedUsers saveRecordToDeletedUsers(DeletedUsers deletedUsers);
}
