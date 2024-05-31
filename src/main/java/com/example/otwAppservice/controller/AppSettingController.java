package com.example.otwAppservice.controller;


import com.example.otwAppservice.TermsAndConditions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/setting")
public class AppSettingController {

    @GetMapping(value = "/getTermsAndConditions", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String welcomeAsHTML() {
        return TermsAndConditions.termsAndConditionsText;
    }

    @GetMapping(value = "/getFAQS", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String getFAQS() {
        return TermsAndConditions.faqContent;
    }
}
