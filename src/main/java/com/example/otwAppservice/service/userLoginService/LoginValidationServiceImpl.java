package com.example.otwAppservice.service.userLoginService;


import com.example.otwAppservice.dto.ValidateOtpDTO;
import com.example.otwAppservice.entity.User;
import com.example.otwAppservice.service.userService.UserService;
import com.example.otwAppservice.utils.EncryptionUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class LoginValidationServiceImpl implements LoginValidationService {


    @Autowired
    UserService userService;

    public byte[] validateUserAndGenerateQR(ValidateOtpDTO validateOtpDTO) throws WriterException, IOException {

        long currentTimeMillis = System.currentTimeMillis();
        long fiveMinutesInMillis = 5 * 60 * 1000; // 5 minutes in milliseconds
        long expiryTimeMillis = currentTimeMillis + fiveMinutesInMillis;
        User user = userService.getUserByPhoneNumber(validateOtpDTO.getPhoneNumber());
        String json = null;
        String token = null;
        if (user != null) {
            json = "{\"code\":\"" + validateOtpDTO.getCode() + "\","
                    + "\"phoneNumber\":\"" + validateOtpDTO.getPhoneNumber() + "\","
                    + "\"customerId\":\"" + user.getId() + "\","
                    + "\"expiryTime\":\"" + expiryTimeMillis + "\","
                    + "\"timestamp\":" + currentTimeMillis + "}";
            token = EncryptionUtils.encrypt(json);
        }

        // Generate QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        System.out.println("Json Body  : " + json);
        System.out.println("\n Token  : " + token);
        BitMatrix bitMatrix = qrCodeWriter.encode(token, BarcodeFormat.QR_CODE, 200, 200);

        // Convert to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
        byte[] qrCodeBytes = byteArrayOutputStream.toByteArray();

        // Return the QR code as a byte array in the response
        return qrCodeBytes;

    }

    public String validateUserAndGenerateToken(ValidateOtpDTO validateOtpDTO) {

        long currentTimeMillis = System.currentTimeMillis();
        long fiveMinutesInMillis = 5 * 60 * 1000; // 5 minutes in milliseconds
        long expiryTimeMillis = currentTimeMillis + fiveMinutesInMillis;
        User user = userService.getUserByPhoneNumber(validateOtpDTO.getPhoneNumber());
        String json = null;
        String token = null;
        if (user != null) {
            json = "{\"code\":\"" + validateOtpDTO.getCode() + "\","
                    + "\"phoneNumber\":\"" + validateOtpDTO.getPhoneNumber() + "\","
                    + "\"customerId\":\"" + user.getId() + "\","
                    + "\"expiryTime\":\"" + expiryTimeMillis + "\","
                    + "\"timestamp\":" + currentTimeMillis + "}";
            token = EncryptionUtils.encrypt(json);
        }
        return token;

//        // Generate QR code
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        System.out.println("Json Body  : " + json);
//        System.out.println("\n Token  : " + token);
//        BitMatrix bitMatrix = qrCodeWriter.encode(token, BarcodeFormat.QR_CODE, 200, 200);
//
//        // Convert to byte array
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", byteArrayOutputStream);
//        byte[] qrCodeBytes = byteArrayOutputStream.toByteArray();
//
//        // Return the QR code as a byte array in the response
//        return qrCodeBytes;

    }
}
