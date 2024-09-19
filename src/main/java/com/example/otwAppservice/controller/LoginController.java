package com.example.otwAppservice.controller;


import com.example.otwAppservice.dto.DecryptedTokenDTO;
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
import com.example.otwAppservice.service.userService.UserServiceImpl;
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
import java.time.Instant;


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
                LOGGER.info("Validating OTP [" + validateOtpDTO.getCode() + "] : [" + validateOtpDTO.getCode() + "]");
                {
                    if (validOTP != null && validOTP.getCode() != null && (validateOtpDTO.getCode().equals(validOTP.getCode()) &&
                            validateOtpDTO.getPhoneNumber().equals(validOTP.getPhoneNumber()))) {
                        LOGGER.info("Validated Successfully ");
                        //Check If User Details Already Exists
                        User user = userService.getUserByPhoneNumber(validateOtpDTO.getPhoneNumber());
                        if (user == null) {
                            LOGGER.info("USER NOT FOUND WITH PHONE-NUMBER : [" + validateOtpDTO.getPhoneNumber() + "]");
                            user = new User();
                            user.setPhoneNumber(validateOtpDTO.getPhoneNumber());
                            user = userService.saveUser(user);
                        }


                        UserCardDetails existingUserDetails = new UserCardDetails();
                        existingUserDetails.setUser(user);
                        if (user != null) {
                            UserCardDetails existingUserCarDetailsByUSer = userService.getUserCardDetailsByUserId(user.getId());
                            if (existingUserCarDetailsByUSer != null) {
                                existingUserDetails = existingUserCarDetailsByUSer;
                                existingUserDetails.setUser(user);
                            }
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
                    user.setAge(userDetailsDTO.getAge() == null ? user.getAge() : userDetailsDTO.getAge());
                    user.setGender(userDetailsDTO.getGender() == null ? user.getGender() : userDetailsDTO.getGender());
                    user.setCity(userDetailsDTO.getCity() == null ? user.getCity() : userDetailsDTO.getCity());

                } else {
                    LOGGER.info("USER NOT FOUND WITH PHONE-NUMBER : [" + userDetailsDTO.getPhoneNumber() + "]");
                    user = new User();
                    user.setName(userDetailsDTO.getName());
                    user.setEmail(userDetailsDTO.getEmail());
                    user.setPhoneNumber(userDetailsDTO.getPhoneNumber());
                    user.setAge(userDetailsDTO.getAge());
                    user.setGender(userDetailsDTO.getGender());
                    user.setCity(userDetailsDTO.getCity());

                }
                User updatedUser = userService.saveUser(user);
                UserCardDetails userCardDetails = userService.saveAndGetUserWithDetails(userDetailsDTO, user);
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

//    private UserCardDetails saveAndGetUserWithDetails(@RequestBody UserDetailsDTO userDetailsDTO, User user) {
//        UserCardDetails userCardDetails = new UserCardDetails();
////        User updatedUser = userService.saveUser(user);
//
//        if (userDetailsDTO.getUserCardDetails() != null && userDetailsDTO.getUserCardDetails().getCardNumber() != null &&
//                !userDetailsDTO.getUserCardDetails().getCardNumber().isEmpty()) {
//            UserCardDetails UpdateUserCardDetails = userDetailsDTO.getUserCardDetails();
//            userCardDetails = userService.getUserCardDetailsByUserId(user.getId());
//            userCardDetails = UserServiceImpl.getUserCardDetails(user, userCardDetails, UpdateUserCardDetails);
//            userCardDetails = userService.saveUserCardDetails(userCardDetails);
//            return userCardDetails;
//        }
//        userCardDetails = new UserCardDetails();
//        userCardDetails.setUser(user);
//        return userCardDetails;
//    }


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
            if (token != null && token.equals("01")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new Messages<String>().setMessage("Failed to validate user.")
                                .setData(null)
                                .setStatus(HttpStatus.UNAUTHORIZED.value())
                                .setCode(String.valueOf(HttpStatus.UNAUTHORIZED)));
            } else if (token != null && token.equals("02")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new Messages<String>().setMessage("No Card Exist With This User.")
                                .setData(null)
                                .setStatus(HttpStatus.UNAUTHORIZED.value())
                                .setCode(String.valueOf(HttpStatus.UNAUTHORIZED)));
            } else if (token != null && token.equals("03")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new Messages<String>().setMessage("Failed to validate Card Details.")
                                .setData(null)
                                .setStatus(HttpStatus.UNAUTHORIZED.value())
                                .setCode(String.valueOf(HttpStatus.UNAUTHORIZED)));
            } else {
                return ResponseEntity.status(HttpStatus.CREATED).body(
                        new Messages<String>().setMessage("QR Generated Successfully.")
                                .setData(token)
                                .setStatus(HttpStatus.CREATED.value())
                                .setCode(String.valueOf(HttpStatus.CREATED)));
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

        if (validateOtpDTO != null && validateOtpDTO.getToken() != null && !validateOtpDTO.getToken().isEmpty()) {

            String decryptedToken = EncryptionUtils.decrypt(validateOtpDTO.getToken());
            if (isValidDecryptedToken(decryptedToken).equals("00")) {
                System.out.println("Decrypted Token : " + decryptedToken);

                try {
                    // Parse the decrypted token JSON data
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(decryptedToken);

                    // Create a DTO and populate it
                    DecryptedTokenDTO tokenDTO = new DecryptedTokenDTO();
                    tokenDTO.setCode(jsonNode.get("code").asText());
                    tokenDTO.setPhoneNumber(jsonNode.get("phoneNumber").asText());
                    tokenDTO.setCustomerId(jsonNode.get("customerId").asText());
                    // `expiryTime` and `timestamp` are not included in `DecryptedTokenDTO`

                    // Convert DTO to JSON string
                    String updatedData = objectMapper.writeValueAsString(tokenDTO);

                    return ResponseEntity.ok()
                            .body(new Messages<>().setMessage("Token Validated Successfully.")
                                    .setData(updatedData)
                                    .setStatus(HttpStatus.OK.value())
                                    .setCode(String.valueOf(HttpStatus.OK)));
                } catch (Exception e) {
                    // Handle JSON parsing error
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new Messages<>().setMessage("Error processing the token data.")
                                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                    .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
                }
//                return ResponseEntity.ok()
//                        .body(new Messages<>().setMessage("Token Validated Successfully.")
//                                .setData(decryptedToken)
//                                .setStatus(HttpStatus.OK.value())
//                                .setCode(String.valueOf(HttpStatus.OK)));
            } else if (isValidDecryptedToken(decryptedToken).equals("01")) {
                // Handle the error: decryption failed or token is invalid
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Messages<>().setMessage("Decryption failed or token is invalid.")
                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                .setCode(String.valueOf(HttpStatus.BAD_REQUEST)));
            } else if (isValidDecryptedToken(decryptedToken).equals("02")) {
                // Handle the error: decryption failed or token is invalid
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new Messages<>().setMessage("Internal Server Error.")
                                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));
            } else if (isValidDecryptedToken(decryptedToken).equals("03")) {
                // Handle the error: Session Expired
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Messages<>().setMessage("Token Expired.")
                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                .setCode(String.valueOf(HttpStatus.BAD_REQUEST)));
            } else {
                // Handle the error: decryption failed or token is invalid
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new Messages<>().setMessage("Decryption failed or token is invalid.")
                                .setStatus(HttpStatus.BAD_REQUEST.value())
                                .setCode(String.valueOf(HttpStatus.BAD_REQUEST)));
            }

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Messages<>().setMessage("Token is invalid.")
                            .setStatus(HttpStatus.BAD_REQUEST.value())
                            .setCode(String.valueOf(HttpStatus.BAD_REQUEST)));
        }
    }


    private String isValidDecryptedToken(String decryptedToken) {
        if (decryptedToken == null || decryptedToken.isEmpty()) {
            return "01";
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(decryptedToken);

            // Check for the expected fields in the JSON
            if (jsonNode.has("code") &&
                    jsonNode.has("phoneNumber") &&
                    jsonNode.has("customerId") &&
                    jsonNode.has("expiryTime") &&
                    jsonNode.has("timestamp")) {

                // Parse the timestamp from the JSON
                long tokenTimestamp = jsonNode.get("timestamp").asLong();
                long expirationTime = jsonNode.get("expiryTime").asLong();

                // Get the current time
                long currentTimestamp = Instant.now().getEpochSecond();

                // Check if the timestamp is within 5 minutes (300 seconds)
                long timeDifference = Math.abs(currentTimestamp - tokenTimestamp);
                // 300 seconds = 5 minutes

                // Get the current time in milliseconds
                long currentTime = Instant.now().toEpochMilli();

                // Check if the current time is before the expiration time
                System.out.println(currentTime);
                System.out.println(expirationTime);
                System.out.println("expirationTime - Current  : " + (expirationTime - currentTime));
                if (currentTime < expirationTime) {
                    System.out.println("The current time is before the expiration time. The token is still valid.");
                    return "00";
                } else {
                    System.out.println("The current time is after the expiration time. The token has expired.");
                    return "03";
                }
//                if (timeDifference <= 300) {
//                    return "00";
//
//                } else {
//                    return "03";
//                }

//                return "00";
            }
        } catch (Exception e) {
            // If an exception occurs, the token is not a valid JSON
            return "02";
        }

        return "04";
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
