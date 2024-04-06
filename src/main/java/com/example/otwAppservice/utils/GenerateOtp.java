package com.example.otwAppservice.utils;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class GenerateOtp {
    public  String codeGenerator(){
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(6);
        for(int i=0; i < 6; i++)
            sb.append((char)('0' + rnd.nextInt(10)));
        return sb.toString();
    }
}
