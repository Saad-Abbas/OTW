package com.example.otwAppservice.service.departmentService;

import com.example.otwAppservice.dto.DepartmentDTO;
import com.example.otwAppservice.entity.Department;
import com.example.otwAppservice.mapper.DepartmentMapper;
import com.example.otwAppservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {


    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public DepartmentDTO saveDepartment(DepartmentDTO departmentDTO) {
        DepartmentDTO departmentSavedDTO = null;
        Department department = DepartmentMapper.mapDepartmentDTOToDepartment(departmentDTO);
//        Department department = new Department(departmentDTO.getId(),
//                departmentDTO.getDepartmentName(),
//                departmentDTO.getDepartmentDescription(),
//                departmentDTO.getDepartmentCode());

        Department saveDepartment = departmentRepository.save(department);
        if (saveDepartment.getId() > 0) {
            departmentSavedDTO = DepartmentMapper.mapDepartmentToDepartmentDTO(saveDepartment);
        }
        return departmentSavedDTO;

//        DepartmentDTO departmentSavedDTO = new DepartmentDTO(saveDepartment.getId(),
//                departmentDTO.getDepartmentName(),
//                departmentDTO.getDepartmentDescription(),
//                departmentDTO.getDepartmentCode());


    }

    @Override
    public DepartmentDTO findByDepartmentCode(String departmentCode) {
        Department department = departmentRepository.findByDepartmentCode(departmentCode);
        DepartmentDTO departmentDTO = null;
        if (department != null) {
            departmentDTO = DepartmentMapper.mapDepartmentToDepartmentDTO(department);
        }
//        DepartmentDTO departmentDTO = new DepartmentDTO(department.getId(),
//                department.getDepartmentName(),
//                department.getDepartmentDescription(),
//                department.getDepartmentCode());

        return departmentDTO;
    }

}
