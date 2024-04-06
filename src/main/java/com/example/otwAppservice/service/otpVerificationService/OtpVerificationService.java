package com.example.otwAppservice.service.otpVerificationService;

import com.example.otwAppservice.entity.OtpVerification;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface OtpVerificationService {


    public OtpVerification findByPhoneNumberAndIsActiveOrderByIdDesc(String phoneNumber, int isActive);

    public OtpVerification saveOtp(String phoneNumber, String code, long userId);


}
