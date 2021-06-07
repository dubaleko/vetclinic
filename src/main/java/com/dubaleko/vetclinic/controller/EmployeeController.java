package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.dto.EmployeeDto;
import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.WorkTime;
import com.dubaleko.vetclinic.repository.WorkTimeRepository;
import com.dubaleko.vetclinic.service.EmployeeService;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final WorkTimeRepository workTimeRepository;

    public EmployeeController(EmployeeService employeeService, WorkTimeRepository workTimeRepository) {
        this.employeeService = employeeService;
        this.workTimeRepository = workTimeRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedListHolder<EmployeeDto> list(@RequestParam int page, @RequestParam Optional<Integer> size,
                                             @RequestParam Optional<Long> spec,
                                             @RequestParam Optional<Long> clinic ){
        return employeeService.getEmployees(page,size,spec,clinic);
    }

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeDto> allEmployees(){
        return  employeeService.getAllEmployees();
    }

    @GetMapping("workTime")
    @ResponseStatus(HttpStatus.OK)
    public List<WorkTime> getWorkTime(){
       return workTimeRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public  void  addNewEmployee(@RequestBody Employee employee){
        employeeService.save(employee);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void  updateEmployee(@RequestBody Employee employee){
        employeeService.update(employee);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@RequestParam long id){
        employeeService.deleteEmployeeById(id);
    }
}
