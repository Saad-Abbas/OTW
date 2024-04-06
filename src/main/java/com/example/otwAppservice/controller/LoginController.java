package com.example.otwAppservice.controller;


import com.example.otwAppservice.dto.UserDetailsDTO;
import com.example.otwAppservice.dto.ValidateOtpDTO;
import com.example.otwAppservice.entity.OtpVerification;
import com.example.otwAppservice.entity.User;
import com.example.otwAppservice.entity.UserCardDetails;
import com.example.otwAppservice.mapper.SMSServiceApiResponse;
import com.example.otwAppservice.service.otpVerificationService.OtpVerificationService;
import com.example.otwAppservice.service.SmsService;
import com.example.otwAppservice.service.userService.UserService;
import com.example.otwAppservice.utils.Messages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/login")
public class LoginController {
    private static Logger LOGGER = LogManager.getLogger(LoginController.class);
    @Autowired
    private SmsService smsService;
    @Autowired
    private OtpVerificationService otpVerificationService;
    @Autowired
    private UserService userService;


    @GetMapping("sendSms/{number}")
    public ResponseEntity sendSms(@PathVariable("number") @NonNull String phoneNumber) {
        ResponseEntity r;
        LOGGER.info("Send Sms [START]");
//        smsService.sendSms("00966536488367");

        SMSServiceApiResponse response = smsService.sendSms(phoneNumber.trim());

//        try {
//            if (!countries.isEmpty()) {
//
//                r = ResponseEntity.ok().body(new Messages<>().setMessage("Country List Fetched Successfully").setData(countries).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));
////                r = ResponseEntity.ok().body(departmentByCode);
////
//            } else {
//                r = ResponseEntity.badRequest().body(new Messages<>().setMessage("Failed to Fetch Country List").setData(null).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));
//
//            }
//        } catch (Exception e) {
//            r = ResponseEntity.ok().body(new Messages<>().setMessage("Failed to Fetch Country List").setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.OK)));
//
//        }
        LOGGER.info("Send Sms [END]");
        r = ResponseEntity.ok().body(response);
        return r;
    }


    @PostMapping(path = "/validateOTP")
    public ResponseEntity validateOTP(@RequestBody ValidateOtpDTO validateOtpDTO) {
        LOGGER.info(" ----  validate OTP  [START] ----");
        ResponseEntity r = null;
        try {
            if ((validateOtpDTO.getPhoneNumber() != null && !validateOtpDTO.getPhoneNumber().isEmpty()) &&
                    (validateOtpDTO.getCode() != null && !validateOtpDTO.getCode().isEmpty())) {

                OtpVerification validOTP = otpVerificationService.findByPhoneNumberAndIsActiveOrderByIdDesc(validateOtpDTO.getPhoneNumber(), 1);
                LOGGER.info("Validating OTP [" + validOTP.getCode() + "] : [" + validateOtpDTO.getCode() + "]");
                {
                    if (validateOtpDTO.getCode().equals(validOTP.getCode()) &&
                            validateOtpDTO.getPhoneNumber().equals(validOTP.getPhoneNumber())) {
                        LOGGER.info("Validated Successfully ");
                        //Check If User Details Already Exists
                        User user = userService.getUserByPhoneNumber(validateOtpDTO.getPhoneNumber());
                        UserCardDetails existingUserDetails = new UserCardDetails();
                        existingUserDetails.setUser(user);
                        if (user != null) {
                            existingUserDetails = userService.getUserCardDetailsByUserId(user.getId());
                        }
                        r = ResponseEntity.ok().body(new Messages<>().setMessage("OTP validated successfully.").setData(existingUserDetails).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

                    } else {
                        LOGGER.info("Failed to Validate OTP  : " + validateOtpDTO.getCode());
                        r = ResponseEntity.ok().body(new Messages<>().setMessage("Failed to Validate OTP  : " + validateOtpDTO.getCode()).setData(null).setStatus(HttpStatus.UNAUTHORIZED.value()).setCode(String.valueOf(HttpStatus.UNAUTHORIZED)));
                    }
                }
            } else {
                LOGGER.info("Failed to Validate OTP \n Getting null values.");
                r = ResponseEntity.badRequest().body(new Messages<>().setMessage("Getting Null Values").setData(null).setStatus(HttpStatus.BAD_REQUEST.value()).setCode(String.valueOf(HttpStatus.BAD_REQUEST)));

            }
        } catch (Exception e) {
            r = ResponseEntity.internalServerError().body(new Messages<>().setMessage("Exception Error : " + e.getMessage()).setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));

        }
        LOGGER.info(" ---- validate OTP  [END] ----");

        return r;
    }



    @PostMapping(path = "/addUpdateUserDetails")
    public ResponseEntity addUpdateUserDetails(@RequestBody UserDetailsDTO userDetailsDTO) {
        LOGGER.info(" ----  Add/Update User Details  [START] ----");
        ResponseEntity r = null;
        try {
            if (userDetailsDTO.getPhoneNumber() != null && !userDetailsDTO.getPhoneNumber().isEmpty()) {

                User user = userService.getUserByPhoneNumber(userDetailsDTO.getPhoneNumber());
                if (user != null) {
                    LOGGER.info("USER  FOUND WITH PHONE-NUMBER : [" + userDetailsDTO.getPhoneNumber() + "]");
                    user.setName(userDetailsDTO.getName() == null ? user.getName() : userDetailsDTO.getName());
                    user.setEmail(userDetailsDTO.getEmail() == null ? user.getEmail() : userDetailsDTO.getEmail());
                } else {
                    LOGGER.info("USER NOT FOUND WITH PHONE-NUMBER : [" + userDetailsDTO.getPhoneNumber() + "]");
                    user = new User();
                    user.setName(userDetailsDTO.getName() == null ? user.getName() : userDetailsDTO.getName());
                    user.setEmail(userDetailsDTO.getEmail() == null ? user.getEmail() : userDetailsDTO.getEmail());
                    user.setPhoneNumber(userDetailsDTO.getPhoneNumber() == null ? user.getPhoneNumber() : userDetailsDTO.getPhoneNumber());

                }
                User updatedUser = userService.saveUser(user);
                UserCardDetails userCardDetails = saveAndGetUserWithDetails(userDetailsDTO, user);
                return ResponseEntity.ok().body(new Messages<>().setMessage("Successfully Updated")
                        .setData(userCardDetails)
                        .setStatus(HttpStatus.CREATED.value())
                        .setCode(String.valueOf(HttpStatus.CREATED)));
//                r = ResponseEntity.ok().body(userCardDetails);
            }
        } catch (Exception e) {
            LOGGER.error("Exception occurred while updating user details: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(new Messages<>()
                    .setMessage("Internal Server Error")
                    .setData(null)
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        } finally {
            LOGGER.info(" ---- Add/Update User Details [END] ----");
        }
        return r;
    }

    private UserCardDetails saveAndGetUserWithDetails(@RequestBody UserDetailsDTO userDetailsDTO, User user) {
        UserCardDetails userCardDetails = new UserCardDetails();
//        User updatedUser = userService.saveUser(user);

        if (userDetailsDTO.getUserCardDetails() != null && userDetailsDTO.getUserCardDetails().getCardNumber() != null &&
                !userDetailsDTO.getUserCardDetails().getCardNumber().isEmpty()) {
            UserCardDetails UpdateUserCardDetails = userDetailsDTO.getUserCardDetails();
            userCardDetails = userService.getUserCardDetailsByUserId(user.getId());
            if (userCardDetails == null) {
                userCardDetails = new UserCardDetails();

            }
            userCardDetails.setCardNumber(UpdateUserCardDetails.getCardNumber() == null ? userCardDetails.getCardNumber() : UpdateUserCardDetails.getCardNumber());
            userCardDetails.setCvc(UpdateUserCardDetails.getCvc() == null ? userCardDetails.getCvc() : UpdateUserCardDetails.getCvc());
            userCardDetails.setExpiry(UpdateUserCardDetails.getExpiry() == null ? userCardDetails.getExpiry() : UpdateUserCardDetails.getExpiry());
            userCardDetails.setCardUserName(UpdateUserCardDetails.getCardUserName() == null ? userCardDetails.getCardUserName() : UpdateUserCardDetails.getCardUserName());
            userCardDetails.setUser(user);
            userCardDetails = userService.saveUserCardDetails(userCardDetails);
            return userCardDetails;
        }
        userCardDetails = new UserCardDetails();
        userCardDetails.setUser(user);
        return userCardDetails;
    }

}
