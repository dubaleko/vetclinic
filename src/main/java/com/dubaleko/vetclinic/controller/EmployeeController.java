package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.dto.EmployeeDto;
import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.repository.EmployeeRepository;
import com.dubaleko.vetclinic.service.EmployeeService;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeService employeeService) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedListHolder<EmployeeDto> list(@RequestParam int page, @RequestParam String spec,
                                             @RequestParam Optional<Integer> size){
        return employeeService.getEmployees(page,spec,size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public  void  addNewEmployee(@RequestBody Employee employee){
        employeeService.save(employee);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void  updateEmployee(@RequestBody Employee employee){
        employeeService.save(employee);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam long id){
        employeeRepository.deleteById(id);
    }
}
