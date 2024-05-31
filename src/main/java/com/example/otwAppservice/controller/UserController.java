package com.example.otwAppservice.controller;


import com.example.otwAppservice.dto.ValidateOtpDTO;
import com.example.otwAppservice.entity.DeletedUsers;
import com.example.otwAppservice.entity.User;
import com.example.otwAppservice.entity.UserCardDetails;
import com.example.otwAppservice.service.SmsService;
import com.example.otwAppservice.service.otpVerificationService.OtpVerificationService;
import com.example.otwAppservice.service.userService.UserService;
import com.example.otwAppservice.utils.Messages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {

    private static Logger LOGGER = LogManager.getLogger(UserController.class);
    @Autowired
    private SmsService smsService;
    @Autowired
    private OtpVerificationService otpVerificationService;
    @Autowired
    private UserService userService;

    @PostMapping(path = "/getUserByPhoneNumber")
    public ResponseEntity getUserByPhoneNumber(@RequestBody ValidateOtpDTO validateOtpDTO) {
        LOGGER.info(" ----  FETCH USER DETAILS BY PHONE-NUMBER : [" + validateOtpDTO.getPhoneNumber() + "]  [START] ----");
        ResponseEntity r = null;
        try {
            if (validateOtpDTO.getPhoneNumber() != null && !validateOtpDTO.getPhoneNumber().isEmpty()) {

                User existingUser = userService.getUserByPhoneNumber(validateOtpDTO.getPhoneNumber());

                if (existingUser != null) {
                    UserCardDetails userCardDetails = userService.getUserCardDetailsByUserId(existingUser.getId());

                    LOGGER.info("USER FOUND WITH PHONE-NUMBER : " + validateOtpDTO.getPhoneNumber());
                    r = ResponseEntity.ok().body(new Messages<>().setMessage("User Fetched successfully.").setData(userCardDetails).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

                } else {
                    LOGGER.info("USER NOT FOUND WITH PHONE-NUMBER : " + validateOtpDTO.getPhoneNumber());
                    r = ResponseEntity.ok().body(new Messages<>().setMessage("No User found with Phone number : " + validateOtpDTO.getCode()).setData(null).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));
                }

            } else {
                LOGGER.info("Failed to Validate OTP \n Getting null values.");
                r = ResponseEntity.badRequest().body(new Messages<>().setMessage("Getting Null Values").setData(null).setStatus(HttpStatus.BAD_REQUEST.value()).setCode(String.valueOf(HttpStatus.BAD_REQUEST)));

            }
        } catch (Exception e) {
            r = ResponseEntity.internalServerError().body(new Messages<>().setMessage("Exception Error : " + e.getMessage()).setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));

        }
        LOGGER.info(" ----  FETCH USER DETAILS BY PHONE-NUMBER : [" + validateOtpDTO.getPhoneNumber() + "]  [END] ----");

        return r;
    }

    @PostMapping(path = "/deleteUserByPhoneNumber")
    public ResponseEntity deleteUserByPhoneNumber(@RequestBody ValidateOtpDTO validateOtpDTO) {
        LOGGER.info(" ----  DELETE USER BY PHONE-NUMBER : [" + validateOtpDTO.getPhoneNumber() + "]  [START] ----");
        ResponseEntity r = null;
        try {
            if (validateOtpDTO.getPhoneNumber() != null && !validateOtpDTO.getPhoneNumber().isEmpty()) {

                User existingUser = userService.getUserByPhoneNumber(validateOtpDTO.getPhoneNumber());

                if (existingUser != null) {
                    LOGGER.info("Fetched User by PhoneNumber : " + existingUser.getPhoneNumber());
                    UserCardDetails userCardDetails = userService.getUserCardDetailsByUserId(existingUser.getId());


                    DeletedUsers deletedUsers = new DeletedUsers();
                    deletedUsers.setUserId(existingUser.getId());
                    deletedUsers.setPhoneNumber(existingUser.getPhoneNumber());
                    deletedUsers.setEmail(existingUser.getEmail());
                    deletedUsers.setName(existingUser.getName());


                    if (userCardDetails != null) {
                        LOGGER.info("Fetched UserCardDetails by UserId : " + userCardDetails.getUser().getId());
                        deletedUsers.setCardUserName(userCardDetails.getCardUserName());
                        deletedUsers.setCvc(userCardDetails.getCvc());
                        deletedUsers.setExpiry(userCardDetails.getExpiry());
                        deletedUsers.setCardNumber(userCardDetails.getCardNumber());


                    }
                    deletedUsers = userService.saveRecordToDeletedUsers(deletedUsers);
                    if (deletedUsers != null && userCardDetails != null) {
                        userService.deleteUserCardDetails(userCardDetails);
                        LOGGER.info("User Card Details Deleted Successfully");
                    }
                    if (deletedUsers != null) {
                        userService.deleteUser(existingUser);
                        LOGGER.info("User Deleted Successfully");
                    }

                    r = ResponseEntity.ok().body(new Messages<>().setMessage("User Deleted Successfully.").setData(userCardDetails).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

                } else {
                    LOGGER.info("USER NOT FOUND WITH PHONE-NUMBER : " + validateOtpDTO.getPhoneNumber());
                    r = ResponseEntity.ok().body(new Messages<>().setMessage("No User found with Phone number : " + validateOtpDTO.getPhoneNumber()).setData(null).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));
                }

            } else {
                LOGGER.info("Failed to Validate OTP \n Getting null values.");
                r = ResponseEntity.badRequest().body(new Messages<>().setMessage("Getting Null Values").setData(null).setStatus(HttpStatus.BAD_REQUEST.value()).setCode(String.valueOf(HttpStatus.BAD_REQUEST)));

            }
        } catch (Exception e) {
            r = ResponseEntity.internalServerError().body(new Messages<>().setMessage("Exception Error : " + e.getMessage()).setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));

        }
        LOGGER.info(" ----  DELETE USER DETAILS BY PHONE-NUMBER : [" + validateOtpDTO.getPhoneNumber() + "]  [END] ----");

        return r;
    }

}
