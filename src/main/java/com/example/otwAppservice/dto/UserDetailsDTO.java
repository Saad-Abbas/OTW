package com.example.otwAppservice.dto;

import com.example.otwAppservice.entity.UserCardDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private UserCardDetails userCardDetails;


}
