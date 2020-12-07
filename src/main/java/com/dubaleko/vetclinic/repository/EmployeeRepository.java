package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    @Query(value = "select * from Employee order by name limit 2", nativeQuery = true)
    List<Employee> findTopByName();
}
