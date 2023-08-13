package mrsisk.github.io.appointservice.repository;

import mrsisk.github.io.appointservice.models.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
