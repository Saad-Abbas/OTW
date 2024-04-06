package com.example.otwAppservice.service.otpVerificationService;

import com.example.otwAppservice.entity.OtpVerification;
import com.example.otwAppservice.repository.OtpVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class OtpVerificationServiceImpl implements OtpVerificationService {

    @Autowired
    private OtpVerificationRepository otpVerificationRepository;

    public OtpVerification findByPhoneNumberAndIsActiveOrderByIdDesc(String phoneNumber, int isActive) {
        return otpVerificationRepository.findByPhoneNumberAndIsActiveOrderByIdDesc(phoneNumber, isActive);
    }

    public OtpVerification saveOtp(String phoneNumber, String code, long userId) {
        OtpVerification existingOtp = findByPhoneNumberAndIsActiveOrderByIdDesc(phoneNumber, 1);
        if (existingOtp != null && existingOtp.getId() > 0) {
            existingOtp.setIsActive(0);
            otpVerificationRepository.save(existingOtp);
        }

        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setCode(code);
        otpVerification.setPhoneNumber(phoneNumber);
        otpVerification.setDate(new Date());
        otpVerification.setIsActive(1);
        otpVerification.setUserId(userId);
        return otpVerificationRepository.save(otpVerification);
    }
}
