package com.example.otwAppservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.NonNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    private Long id;

    @NotNull(message = "DepartmentName may not be null")
    private String departmentName;

//    @NotNull(message = "DepartmentDescription may not be null")
    @NonNull()
    private String departmentDescription;

//    @NotNull(message = "DepartmentCode may not be null")
    @NonNull()
    private String departmentCode;

}
