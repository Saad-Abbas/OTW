package com.example.otwAppservice.controller;


import com.example.otwAppservice.dto.DepartmentDTO;
import com.example.otwAppservice.service.departmentService.DepartmentService;
import com.example.otwAppservice.utils.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // Save Department Details
    @PostMapping("/save")
    public ResponseEntity saveDepartment(@RequestBody DepartmentDTO departmentDTO) {
        ResponseEntity r;
        try {
            DepartmentDTO saveDepartment = departmentService.saveDepartment(departmentDTO);
            if (saveDepartment != null) {
                r = ResponseEntity.ok().body(new Messages<>().setMessage("Department Create Successfully").setData(saveDepartment).setStatus(HttpStatus.CREATED.value()).setCode(String.valueOf(HttpStatus.CREATED)));

            } else {
                r = ResponseEntity.ok().body(new Messages<>().setMessage("Failed to Create New Department").setData(null).setStatus(400).setCode("Failed"));
            }
        } catch (Exception e) {
            r = ResponseEntity.badRequest().body(new Messages<>().setMessage(e.getMessage()).setData(null).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()).setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR)));

        }
        return r;
    }

    // Get Department Details by Department-Code
    @GetMapping("byDepartmentCode/{department-code}")
    public ResponseEntity getDepartmentByDepartmentCode(@PathVariable("department-code") @NonNull String departmentCode) {
        ResponseEntity r;
        System.out.println("Department Details by Department Code : [" + departmentCode + "]");
        DepartmentDTO departmentByCode = departmentService.findByDepartmentCode(departmentCode);
        try {
            if (departmentByCode != null) {

                r = ResponseEntity.ok().body(new Messages<>().setMessage("Department Fetched Successfully").setData(departmentByCode).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));
//                r = ResponseEntity.ok().body(departmentByCode);
//
            } else {
                r = ResponseEntity.badRequest().body(new Messages<>().setMessage("Failed to Fetch Department").setData(null).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

            }
        } catch (Exception e) {
            r = ResponseEntity.ok().body(new Messages<>().setMessage("Failed to Fetch Department").setData(null).setStatus(HttpStatus.OK.value()).setCode(String.valueOf(HttpStatus.OK)));

        }
        return r;
    }


}
