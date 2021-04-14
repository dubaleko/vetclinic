package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.ClinicOrder;
import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<ClinicOrder,Long> {
    List<ClinicOrder> findAllByUser(User user);
    List<ClinicOrder> findAllByEmployee(Employee employee);
}
