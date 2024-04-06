package com.example.otwAppservice.controller;


import com.example.otwAppservice.entity.Country;
import com.example.otwAppservice.service.countryService.CountryService;
import com.example.otwAppservice.service.SmsService;
import com.example.otwAppservice.utils.GenerateOtp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.otwAppservice.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("api/country")
public class CountryController {
    private static Logger LOGGER = LogManager.getLogger(CountryController.class);
    @Autowired
    private CountryService countryService;
    @Autowired
    private GenerateOtp generateOtp;
    @Autowired
    private SmsService smsService;

    // Get All Active Country List
    @GetMapping("getAllCountries")
    public ResponseEntity getAllActiveCountryList() {
        ResponseEntity r;
        LOGGER.info("Fetch All Country List [START]");
        List<Country> countries = countryService.getAllActiveCountries();
        try {
            if (!countries.isEmpty()) {

                r = ResponseEntity.ok().body(new Messages<>().setMessage("Country List Fetched Successfully").setData(countries).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));
//                r = ResponseEntity.ok().body(departmentByCode);
//
            } else {
                r = ResponseEntity.badRequest().body(new Messages<>().setMessage("Failed to Fetch Country List").setData(null).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

            }
        } catch (Exception e) {
            r = ResponseEntity.ok().body(new Messages<>().setMessage("Failed to Fetch Country List").setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.OK)));

        }
        LOGGER.info("Fetch All Country List [END]");
        return r;
    }


//    @GetMapping("sendSms/{number}")
//    public ResponseEntity sendSms(@PathVariable("number") @NonNull String phoneNumber) {
//        ResponseEntity r;
//        LOGGER.info("Send Sms [START]");
////        smsService.sendSms("00966536488367");
//        SMSServiceApiResponse response = smsService.sendSms(phoneNumber.trim());
//
////        try {
////            if (!countries.isEmpty()) {
////
////                r = ResponseEntity.ok().body(new Messages<>().setMessage("Country List Fetched Successfully").setData(countries).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));
//////                r = ResponseEntity.ok().body(departmentByCode);
//////
////            } else {
////                r = ResponseEntity.badRequest().body(new Messages<>().setMessage("Failed to Fetch Country List").setData(null).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));
////
////            }
////        } catch (Exception e) {
////            r = ResponseEntity.ok().body(new Messages<>().setMessage("Failed to Fetch Country List").setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.OK)));
////
////        }
//        LOGGER.info("Send Sms [END]");
//        r = ResponseEntity.ok().body(response);
//        return r;
//    }
}
