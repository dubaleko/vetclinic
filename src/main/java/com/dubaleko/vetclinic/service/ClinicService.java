package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.User;
import com.dubaleko.vetclinic.entity.enums.Role;
import com.dubaleko.vetclinic.repository.ClinicRepository;
import com.dubaleko.vetclinic.repository.EmployeeRepository;
import com.dubaleko.vetclinic.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public ClinicService(ClinicRepository clinicRepository, UserRepository userRepository,
                         EmployeeRepository employeeRepository) {
        this.clinicRepository = clinicRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public void deleteClinicById(Long id){
        List<User> users = userRepository.findAllByClinic(clinicRepository.getOne(id));
        for (User user : users){
            user.setRole(Role.USER);
            user.setClinic(null);
            userRepository.save(user);
        }
        List<Employee> employees = employeeRepository.getByClinic(clinicRepository.getClinicById(id));
        for (Employee employee : employees){
            users = userRepository.findAllByEmployee(employee);
            for (User user : users){
                user.setRole(Role.USER);
                user.setDoctor(null);
                userRepository.save(user);
            }
            employeeRepository.delete(employee);
        }
        clinicRepository.deleteById(id);
    }
}
