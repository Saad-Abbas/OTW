package com.example.otwAppservice.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateOtpDTO {


    @NotNull(message = "dode may not be null")
    private String code;

    @NotNull(message = "phoneNumber may not be null")
    private String phoneNumber;

}
