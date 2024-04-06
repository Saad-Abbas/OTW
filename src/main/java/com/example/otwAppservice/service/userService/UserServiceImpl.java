package com.example.otwAppservice.service.userService;

import com.example.otwAppservice.entity.User;
import com.example.otwAppservice.entity.UserCardDetails;
import com.example.otwAppservice.repository.UserCardDetailsRepository;
import com.example.otwAppservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCardDetailsRepository userCardDetailsRepository;

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
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserCardDetails saveUserCardDetails(UserCardDetails userCardDetails) {
        return userCardDetailsRepository.save(userCardDetails);
    }

}
