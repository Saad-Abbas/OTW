package com.example.otwAppservice.service.userService;

import com.example.otwAppservice.AppConstants;
import com.example.otwAppservice.MessagingUtils;
import com.example.otwAppservice.dto.UserDetailsDTO;
import com.example.otwAppservice.entity.FinancialTransactions;
import com.example.otwAppservice.entity.OtpVerification;
import com.example.otwAppservice.entity.UserCardDetails;
import com.example.otwAppservice.mapper.FinancialTransactionMapper;
import com.example.otwAppservice.mapper.MoyasarResponses.CardPaymentResponse;
import com.example.otwAppservice.mapper.SMSServiceApiResponse;
import com.example.otwAppservice.service.financialTransactionService.FinancialTransactionService;
import com.example.otwAppservice.service.otpVerificationService.OtpVerificationService;
import com.example.otwAppservice.utils.GenerateOtp;
import com.example.otwAppservice.utils.MoyasarConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class MoyasarService {

    // Test Username
    String username = "pk_test_ozE34G2fQaWq96Uyy3r4KzwNsKJ2DVNFSknXBEpy";
    String password = "sk_test_78Cx4XwYiGNC3a7oYKYvHZsm3QybRkqGEef2WXr7";

    //    Live Username
//    String username = "pk_live_r7eNgYjbqpqV8bqxNruAbhsyUN7j5H5YqzGskvXD";
//    String password = "sk_test_NPhxn78Z9fE9Ni3DaNeKUE5V2kY23jHbztFB9kJH";

    @Autowired
    FinancialTransactionService financialTransactionService;

    public String generateAuthorizationToken() {


        // Create the "username:password" string
        String authString = username + ":" + password;

        // Encode the string in Base64
        String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());

        // Create the Authorization header
        String authorizationHeader = "Basic " + authStringEnc;
        System.out.println("Auth Header : " + authorizationHeader);
        return authorizationHeader;
    }


    //    public CardPaymentResponse cardPaymentViaCardDetails(UserCardDetails userCardDetails) {
    public CardPaymentResponse cardPaymentViaCardDetails() {
        CardPaymentResponse responseObject = null;
        try {
            //Transforming Message Body

//            if (userCardDetails != null) {
//                String message = "<#> Hello Partner, your OTP is " + code + ". \n +gE3RO6pK// ";
//                String transformedBody = MessagingUtils.transformSms(phone, companyname, api_token, message);
            String jsonBody = "{"
                    + "\"amount\": 100,"
                    + "\"currency\": \"SAR\","
                    + "\"description\": \"Payment for order #\","
                    + "\"callback_url\": \"https://example.com/thankyou\","
                    + "\"source\": {"
                    + "\"type\": \"creditcard\","
                    + "\"name\": \"Mohammed Ali\","
                    + "\"number\": \"4111111111111111\","
                    + "\"cvc\": \"123\","
                    + "\"month\": \"12\","
                    + "\"year\": \"26\""
                    + "}"
                    + "}";

            //Calling API
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonBody);
            Request request = new Request.Builder()
                    .url(MoyasarConstants.CreateCardPayment)
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", generateAuthorizationToken())
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
                responseObject = objectMapper.readValue(responseBody, CardPaymentResponse.class);
                System.out.println("Response Body: " + responseObject);

                if (responseObject != null) {
                    CardPaymentResponse.Source paymentCardDetails = responseObject.getSource();

                }

                // Now you can work with the parsed responseObject
                // For example, print its content
//                    System.out.println("Response status: " + Arrays.toString(responseObject));
//                    System.out.println("Response message: " + responseObject.getDetails());
                // Add more processing as needed

//                } else {
//                    // Handle unsuccessful response here
//                    System.out.println("Error: " + response.code() + " - " + response.message());
//                }
            } else {
                // Handle unsuccessful response here
                System.out.println("Error: " + response.code() + " - " + response.message());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseObject;
    }

    public CardPaymentResponse cardPaymentViaPaymentToken(String paymentToken, int amount) {
        CardPaymentResponse responseObject = null;
        try {
            //Transforming Message Body

//            if (userCardDetails != null) {
//                String message = "<#> Hello Partner, your OTP is " + code + ". \n +gE3RO6pK// ";
//                String transformedBody = MessagingUtils.transformSms(phone, companyname, api_token, message);
            String jsonBody = "{"
                    + "\"amount\": " + amount + ","
                    + "\"currency\": \"SAR\","
                    + "\"description\": \"Payment for order\","
                    + "\"callback_url\": \"https://example.com/thankyou\","
                    + "\"source\": {"
                    + "\"type\": \"token\","
                    + "\"token\": \"" + paymentToken + "\""
                    + "}"
                    + "}";

            //Calling API
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)  // Increase connect timeout
                    .readTimeout(60, TimeUnit.SECONDS)     // Increase read timeout
                    .writeTimeout(60, TimeUnit.SECONDS)    // Increase write timeout
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonBody);
            Request request = new Request.Builder()
                    .url(MoyasarConstants.CreateCardPayment)
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", generateAuthorizationToken())
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
                responseObject = objectMapper.readValue(responseBody, CardPaymentResponse.class);
                System.out.println("Response Body: " + responseObject);
                FinancialTransactions financialTransactions = FinancialTransactionMapper.mapFinancialResponseToFinancialTransactionEntity(responseObject);
                financialTransactions = financialTransactionService.saveFinancialTransaction(financialTransactions);
                if (responseObject != null) {
                    CardPaymentResponse.Source paymentCardDetails = responseObject.getSource();

                }

                // Now you can work with the parsed responseObject
                // For example, print its content
//                    System.out.println("Response status: " + Arrays.toString(responseObject));
//                    System.out.println("Response message: " + responseObject.getDetails());
                // Add more processing as needed

//                } else {
//                    // Handle unsuccessful response here
//                    System.out.println("Error: " + response.code() + " - " + response.message());
//                }
            } else {
                // Handle unsuccessful response here
                System.out.println("Error: " + response.code() + " - " + response.message());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseObject;
    }

    public CardPaymentResponse refundPayment(String paymentId, int refundAmount) {
        CardPaymentResponse responseObject = null;
        try {
            // Constructing the JSON body
            String jsonBody = "{"
                    + "\"amount\": " + refundAmount
                    + "}";

            // Calling API
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonBody);
            Request request = new Request.Builder()
                    .url(MoyasarConstants.RefundPayment + paymentId + "/refund")
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", generateAuthorizationToken())
                    .build();

            Response response = client.newCall(request).execute();

            // Check if the response was successful
            if (response.isSuccessful()) {
                // Read the response body as a string
                String responseBody = Objects.requireNonNull(response.body()).string();

                // Initialize ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();

                // Parse the response JSON string into your CardPaymentResponse class
                responseObject = objectMapper.readValue(responseBody, CardPaymentResponse.class);
                System.out.println("Response Body: " + responseObject);

                // Additional processing if needed
            } else {
                // Handle unsuccessful response
                System.out.println("Error: " + response.code() + " - " + response.message());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return responseObject;
    }

}
