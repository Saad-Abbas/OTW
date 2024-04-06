package com.example.otwAppservice.service.departmentService;

import com.example.otwAppservice.dto.DepartmentDTO;

public interface DepartmentService {


    public DepartmentDTO saveDepartment(DepartmentDTO departmentDTO);
    public DepartmentDTO findByDepartmentCode(String departmentCode);

}
