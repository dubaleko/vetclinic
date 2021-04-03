package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.Clinic;
import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String userName);
    @Query("select u from User u where doctor = ?1")
    List<User> findAllByEmployee(Employee employee);
    @Query("select u from User  u where clinic = ?1")
    List<User> findAllByClinic(Clinic clinic);
}
