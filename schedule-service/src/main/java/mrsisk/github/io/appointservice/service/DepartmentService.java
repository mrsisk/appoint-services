package mrsisk.github.io.appointservice.service;

import jakarta.transaction.Transactional;
import mrsisk.github.io.appointservice.dto.CreateDepartmentDto;
import mrsisk.github.io.appointservice.models.Department;
import mrsisk.github.io.appointservice.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository repository;

    public Department createDepartment(CreateDepartmentDto dto){
        Department department = new Department(dto.name(), dto.code());
        return repository.save(department);
    }

    public List<Department> getAllDepartment(){
        return repository.findAll();
    }
}
