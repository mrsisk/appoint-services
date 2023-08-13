package mrsisk.github.io.appointservice.controllers;

import mrsisk.github.io.appointservice.dto.CreateDepartmentDto;
import mrsisk.github.io.appointservice.models.Department;
import mrsisk.github.io.appointservice.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<Department> createDepartment(@RequestBody CreateDepartmentDto dto){
        Department department = departmentService.createDepartment(dto);
        return ResponseEntity.ok(department);
    }

    @GetMapping
    public ResponseEntity<List<Department>> findAllDepartments(){
        var departments = departmentService.getAllDepartment();
        return ResponseEntity.ok(departments);
    }
}
