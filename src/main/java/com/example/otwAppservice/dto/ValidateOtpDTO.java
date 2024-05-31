package com.example.otwAppservice.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ValidateOtpDTO {


    @NotNull(message = "code may not be null")
    private String code;

    @NotNull(message = "phoneNumber may not be null")
    private String phoneNumber;

    private String token;
    private String storeId;

}
