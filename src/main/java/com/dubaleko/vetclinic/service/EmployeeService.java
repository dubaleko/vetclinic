package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.comparators.DayComparator;
import com.dubaleko.vetclinic.comparators.EmployeeComparator;
import com.dubaleko.vetclinic.dto.EmployeeDto;
import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.WeekDay;
import com.dubaleko.vetclinic.repository.EmployeeRepository;
import com.dubaleko.vetclinic.repository.SpecializationRepository;
import com.dubaleko.vetclinic.repository.WeekDayRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private final SpecializationRepository specializationRepository;
    private final EmployeeRepository employeeRepository;
    private final WeekDayRepository weekDayRepository;
    private String specializationName = "Empty";

    public EmployeeService(SpecializationRepository specializationRepository, EmployeeRepository employeeRepository,
                           WeekDayRepository weekDayRepository) {
        this.specializationRepository = specializationRepository;
        this.employeeRepository = employeeRepository;
        this.weekDayRepository = weekDayRepository;
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
        Collections.sort(employeeDtos, new EmployeeComparator());
        for (EmployeeDto employeeDto : employeeDtos){
            Collections.sort(employeeDto.getDays(),new DayComparator());
        }
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

    public void save(Employee employee){
        Set<WeekDay> weekDays = new HashSet<>();
        for (WeekDay weekDay : employee.getDays()){
            weekDays.add(weekDayRepository.getByDayName(weekDay.getDayName()));
        }
        employee.setDays(weekDays);
        if (employee.getId() == null){

        }
        employeeRepository.save(employee);
    }

    public List<EmployeeDto> getPreview(){
        List<Employee> employees = employeeRepository.findTopByName();
        return employees.stream().map(user -> new ModelMapper().map(user, EmployeeDto.class)).
                collect(Collectors.toList());
    }
}
