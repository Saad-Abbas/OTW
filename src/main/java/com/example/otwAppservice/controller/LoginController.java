package com.example.otwAppservice.controller;


import com.example.otwAppservice.dto.QRVerifyTokenDTO;
import com.example.otwAppservice.dto.UserDetailsDTO;
import com.example.otwAppservice.dto.ValidateOtpDTO;
import com.example.otwAppservice.entity.OtpVerification;
import com.example.otwAppservice.entity.User;
import com.example.otwAppservice.entity.UserCardDetails;
import com.example.otwAppservice.mapper.SMSServiceApiResponse;
import com.example.otwAppservice.service.otpVerificationService.OtpVerificationService;
import com.example.otwAppservice.service.SmsService;
import com.example.otwAppservice.service.userLoginService.LoginValidationServiceImpl;
import com.example.otwAppservice.service.userService.UserService;
import com.example.otwAppservice.utils.EncryptionUtils;
import com.example.otwAppservice.utils.Messages;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


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
    @Autowired
    private LoginValidationServiceImpl loginValidationService;


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


    //Cloudpick APIS

    @PostMapping("/generateQR")
    public ResponseEntity<Messages<String>> generateQRCode(@RequestBody ValidateOtpDTO validateOtpDTO) {
        try {
            if (validateOtpDTO.getPhoneNumber() == null || validateOtpDTO.getPhoneNumber().isEmpty() ||
                    validateOtpDTO.getCode() == null || validateOtpDTO.getCode().isEmpty()) {
                return ResponseEntity.badRequest().body(
                        new Messages<String>().setMessage("Phone number or code is missing.")
                                .setData(null)
                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                .setCode(String.valueOf(HttpStatus.BAD_REQUEST)));
            }

            String token = loginValidationService.validateUserAndGenerateToken(validateOtpDTO);
            if (token != null) {
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        new Messages<String>().setMessage("QR Generated Successfully.")
                                .setData(token)
                                .setStatus(HttpStatus.CREATED.value())
                                .setCode(String.valueOf(HttpStatus.CREATED)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new Messages<String>().setMessage("Failed to validate user.")
                                .setData(null)
                                .setStatus(HttpStatus.UNAUTHORIZED.value())
                                .setCode(String.valueOf(HttpStatus.UNAUTHORIZED)));
            }

        } catch (Exception e) {
            LOGGER.error("Error generating QR code", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new Messages<String>().setMessage("Internal Server Error.")
                            .setData(null)
                            .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
        }
    }

    @PostMapping("/resource/cloudpick/qrCodeVerify")
    public ResponseEntity validateQR(@RequestBody ValidateOtpDTO validateOtpDTO) {
        ResponseEntity r = null;
        LOGGER.info("ValidateOtpDTO : " + validateOtpDTO.toString());

        String decryptedToken = EncryptionUtils.decrypt(validateOtpDTO.getToken());
        if (isValidDecryptedToken(decryptedToken)) {
            System.out.println("Decrypted Token : " + decryptedToken);
            return ResponseEntity.ok()
                    .body(new Messages<>().setMessage("Token Validated Successfully.")
                            .setData(decryptedToken)
                            .setStatus(HttpStatus.OK.value())
                            .setCode(String.valueOf(HttpStatus.OK)));
        } else {
            // Handle the error: decryption failed or token is invalid
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Messages<>().setMessage("Decryption failed or token is invalid.")
                            .setStatus(HttpStatus.BAD_REQUEST.value())
                            .setCode(String.valueOf(HttpStatus.BAD_REQUEST)));
        }
    }


    private boolean isValidDecryptedToken(String decryptedToken) {
        if (decryptedToken == null || decryptedToken.isEmpty()) {
            return false;
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(decryptedToken);

            // Check for the expected fields in the JSON
            return jsonNode.has("code") &&
                    jsonNode.has("phoneNumber") &&
                    jsonNode.has("customerId") &&
                    jsonNode.has("expiryTime") &&
                    jsonNode.has("timestamp");
        } catch (Exception e) {
            // If an exception occurs, the token is not a valid JSON
            return false;
        }
    }


//    @PostMapping(path = "/resource/cloudpick/qrCodeVerify")
//    public ResponseEntity qrCodeVerify(@RequestBody QRVerifyTokenDTO qrVerifyTokenDTO) {
//        LOGGER.info(" ----  validate QR Token  [START] ----");
//        ResponseEntity r = null;
//        try {
//            if ((qrVerifyTokenDTO.getToken() != null && !qrVerifyTokenDTO.getToken().isEmpty()) &&
//                    (qrVerifyTokenDTO.getStoreId() != null && !qrVerifyTokenDTO.getStoreId().isEmpty())) {
//
//                OtpVerification validOTP = otpVerificationService.findByPhoneNumberAndIsActiveOrderByIdDesc(validateOtpDTO.getPhoneNumber(), 1);
//                LOGGER.info("Validating OTP [" + validOTP.getCode() + "] : [" + validateOtpDTO.getCode() + "]");
//                {
//                    if (validateOtpDTO.getCode().equals(validOTP.getCode()) &&
//                            validateOtpDTO.getPhoneNumber().equals(validOTP.getPhoneNumber())) {
//                        LOGGER.info("Validated Successfully ");
//                        //Check If User Details Already Exists
//                        User user = userService.getUserByPhoneNumber(validateOtpDTO.getPhoneNumber());
//                        UserCardDetails existingUserDetails = new UserCardDetails();
//                        existingUserDetails.setUser(user);
//                        if (user != null) {
//                            existingUserDetails = userService.getUserCardDetailsByUserId(user.getId());
//                        }
//                        r = ResponseEntity.ok().body(new Messages<>().setMessage("OTP validated successfully.").setData(existingUserDetails).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));
//
//                    } else {
//                        LOGGER.info("Failed to Validate OTP  : " + validateOtpDTO.getCode());
//                        r = ResponseEntity.ok().body(new Messages<>().setMessage("Failed to Validate OTP  : " + validateOtpDTO.getCode()).setData(null).setStatus(HttpStatus.UNAUTHORIZED.value()).setCode(String.valueOf(HttpStatus.UNAUTHORIZED)));
//                    }
//                }
//            } else {
//                LOGGER.info("Failed to Validate OTP \n Getting null values.");
//                r = ResponseEntity.badRequest().body(new Messages<>().setMessage("Getting Null Values").setData(null).setStatus(HttpStatus.BAD_REQUEST.value()).setCode(String.valueOf(HttpStatus.BAD_REQUEST)));
//
//            }
//        } catch (Exception e) {
//            r = ResponseEntity.internalServerError().body(new Messages<>().setMessage("Exception Error : " + e.getMessage()).setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
//
//        }
//        LOGGER.info(" ----  validate QR Token   [END] ----");
//
//        return r;
//    }
//

}
