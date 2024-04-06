package com.example.otwAppservice.service;


import com.example.otwAppservice.AppConstants;
import com.example.otwAppservice.MessagingUtils;
import com.example.otwAppservice.entity.OtpVerification;
import com.example.otwAppservice.mapper.SMSServiceApiResponse;
import com.example.otwAppservice.service.otpVerificationService.OtpVerificationService;
import com.example.otwAppservice.utils.GenerateOtp;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import okhttp3.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Service
public class SmsService {
    @Autowired
    private GenerateOtp generateOtp;
    @Autowired
    private OtpVerificationService otpVerificationService;

    private String api_token = "5d1b9ca4-2cbf-4033-ae13-92dac49b324f";
    private String account_Id = "c73c2bac-f3dd-443a-a9d4-db05ad33916c";
    //    private String companyname = "OTW-App";
    private String companyname = "CMTest";


    public SMSServiceApiResponse sendSms(String phone) {
        SMSServiceApiResponse responseObject = null;
        try {
            //Transforming Message Body
            String code = generateOtp.codeGenerator();
            OtpVerification savedOtp = otpVerificationService.saveOtp(phone,code,0);
            if (savedOtp != null) {
                String message = "<#> Hello Partner, your OTP is " + code + ". \n +gE3RO6pK// ";
                String transformedBody = MessagingUtils.transformSms(phone, companyname, api_token, message);


                //Calling API
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, transformedBody);
                Request request = new Request.Builder()
                        .url(AppConstants.CMServiceProviderUrl)
                        .post(body)
                        .addHeader("accept", "application/json")
                        .addHeader("content-type", "application/json")
                        .build();

                Response response = client.newCall(request).execute();

                // Check if the response was successful
                // Check if the response was successful
                if (response.isSuccessful()) {
                    // Read the response body as a string
                    String responseBody = Objects.requireNonNull(response.body()).string();

                    // Initialize ObjectMapper
                    ObjectMapper objectMapper = new ObjectMapper();

                    // Parse the response JSON string into your SMSServiceApiResponse class
                    responseObject = objectMapper.readValue(responseBody, SMSServiceApiResponse.class);

                    // Now you can work with the parsed responseObject
                    // For example, print its content
                    System.out.println("Response status: " + Arrays.toString(responseObject.getMessages()));
                    System.out.println("Response message: " + responseObject.getDetails());
                    // Add more processing as needed

                } else {
                    // Handle unsuccessful response here
                    System.out.println("Error: " + response.code() + " - " + response.message());
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseObject;
    }
}
