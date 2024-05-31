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
public class QRVerifyTokenDTO {


    @NotNull(message = "token may not be null")
    private String token;

    @NotNull(message = "storeId may not be null")
    private String storeId;
}
