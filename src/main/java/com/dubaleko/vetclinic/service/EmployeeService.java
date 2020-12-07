package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.CustomComparator;
import com.dubaleko.vetclinic.dto.EmployeeDto;
import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.repository.EmployeeRepository;
import com.dubaleko.vetclinic.repository.SpecializationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final SpecializationRepository specializationRepository;
    private final EmployeeRepository employeeRepository;
    private String specializationName = "Empty";

    public EmployeeService(SpecializationRepository specializationRepository, EmployeeRepository employeeRepository) {
        this.specializationRepository = specializationRepository;
        this.employeeRepository = employeeRepository;
    }

    public PagedListHolder<EmployeeDto> getEmployees(int page, Optional<String> spec){
        List<Employee> employees;
        if (spec.isEmpty() || spec.get().equals("Любая специализация")) {
            employees = employeeRepository.findAll();
        }
        else  {
            employees = specializationRepository.getBySpecialization(spec.get()).getEmpls();
        }
        List<EmployeeDto> employeeDtos = employees.stream().map(user -> new ModelMapper().map(user, EmployeeDto.class)).
                collect(Collectors.toList());
        Collections.sort(employeeDtos, new CustomComparator());
        PagedListHolder<EmployeeDto> pagedListHolder = new PagedListHolder<EmployeeDto>(employeeDtos);
        pagedListHolder.setPageSize(5);
        if (spec.isEmpty() || spec.get().equals(specializationName)) {
            pagedListHolder.setPage(page - 1);
        }
        else {
            pagedListHolder.setPage(0);
            specializationName = spec.get();
        }
        return pagedListHolder;
    }

    public List<EmployeeDto> getPreview(){
        List<Employee> employees = employeeRepository.findTopByName();
        return employees.stream().map(user -> new ModelMapper().map(user, EmployeeDto.class)).
                collect(Collectors.toList());
    }
}
