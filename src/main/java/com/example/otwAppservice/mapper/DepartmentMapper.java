package com.example.otwAppservice.mapper;

import com.example.otwAppservice.dto.DepartmentDTO;
import com.example.otwAppservice.entity.Department;

public class DepartmentMapper {

    //Convert Department Entity to DepartmentDTO
    public static DepartmentDTO mapDepartmentToDepartmentDTO(Department department) {
        return new DepartmentDTO(department.getId(),
                department.getDepartmentName(),
                department.getDepartmentDescription(),
                department.getDepartmentCode());
    }
    //Convert DepartmentDTO to Department Entity
    public static Department mapDepartmentDTOToDepartment(DepartmentDTO departmentDTO) {
        return new Department(departmentDTO.getId(),
                departmentDTO.getDepartmentName(),
                departmentDTO.getDepartmentDescription(),
                departmentDTO.getDepartmentCode());
    }
}
