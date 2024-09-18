package com.example.otwAppservice.service.userService;

import com.example.otwAppservice.dto.UserDetailsDTO;
import com.example.otwAppservice.entity.DeletedUsers;
import com.example.otwAppservice.entity.User;
import com.example.otwAppservice.entity.UserCardDetails;
import com.example.otwAppservice.repository.DeletedUserRepository;
import com.example.otwAppservice.repository.UserCardDetailsRepository;
import com.example.otwAppservice.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCardDetailsRepository userCardDetailsRepository;
    @Autowired
    DeletedUserRepository deletedUserRepository;

    @Override
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

    @Override
    public UserCardDetails getUserCardDetailsByUserId(Long userId) {
        return userCardDetailsRepository.findUserCardDetailsByUserId(userId);
    }

    @Override
    public UserCardDetails getUserCardDetailsByUserIdAndIsActive(Long userId, boolean isActive) {
        return userCardDetailsRepository.findUserCardDetailsByUserIdAndIsActive(userId,isActive);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    public void deleteUserCardDetails(UserCardDetails userCardDetails) {
        userCardDetailsRepository.delete(userCardDetails);
    }

    @Override
    public UserCardDetails saveUserCardDetails(UserCardDetails userCardDetails) {
        return userCardDetailsRepository.save(userCardDetails);
    }

    @Override
    public DeletedUsers saveRecordToDeletedUsers(DeletedUsers deletedUsers) {
        return deletedUserRepository.save(deletedUsers);
    }

    @Override
    public UserCardDetails saveAndGetUserWithDetails(@RequestBody UserDetailsDTO userDetailsDTO, User user) {
        UserCardDetails userCardDetails = new UserCardDetails();
//        User updatedUser = userService.saveUser(user);

        if (userDetailsDTO.getUserCardDetails() != null && userDetailsDTO.getUserCardDetails().getCardNumber() != null &&
                !userDetailsDTO.getUserCardDetails().getCardNumber().isEmpty()) {
            UserCardDetails UpdateUserCardDetails = userDetailsDTO.getUserCardDetails();
            userCardDetails = getUserCardDetailsByUserId(user.getId());
            userCardDetails = getUserCardDetails(user, userCardDetails, UpdateUserCardDetails);
            userCardDetails = saveUserCardDetails(userCardDetails);
            return userCardDetails;
        }
        userCardDetails = new UserCardDetails();
        userCardDetails.setUser(user);
        return userCardDetails;
    }

    @NotNull
    public static UserCardDetails getUserCardDetails(User user, UserCardDetails userCardDetails, UserCardDetails updateUserCardDetails) {
        if (userCardDetails == null) {
            userCardDetails = new UserCardDetails();

        }
        userCardDetails.setCardNumber(updateUserCardDetails.getCardNumber() == null ? userCardDetails.getCardNumber() : updateUserCardDetails.getCardNumber());
        userCardDetails.setCvc(updateUserCardDetails.getCvc() == null ? userCardDetails.getCvc() : updateUserCardDetails.getCvc());
        userCardDetails.setExpiry(updateUserCardDetails.getExpiry() == null ? userCardDetails.getExpiry() : updateUserCardDetails.getExpiry());
        userCardDetails.setCardUserName(updateUserCardDetails.getCardUserName() == null ? userCardDetails.getCardUserName() : updateUserCardDetails.getCardUserName());
//        userCardDetails.setGender(updateUserCardDetails.getGender() == null ? userCardDetails.getGender() : updateUserCardDetails.getGender());
//        userCardDetails.setAge(updateUserCardDetails.getAge() == null ? userCardDetails.getAge() : updateUserCardDetails.getAge());
        userCardDetails.setUser(user);
        return userCardDetails;
    }

}
