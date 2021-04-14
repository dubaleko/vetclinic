package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.comparators.DayComparator;
import com.dubaleko.vetclinic.comparators.EmployeeComparator;
import com.dubaleko.vetclinic.comparators.SpecsComparator;
import com.dubaleko.vetclinic.dto.EmployeeDto;
import com.dubaleko.vetclinic.dto.WeekDayDto;
import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.ReceptionDate;
import com.dubaleko.vetclinic.entity.User;
import com.dubaleko.vetclinic.entity.WeekDay;
import com.dubaleko.vetclinic.entity.enums.Role;
import com.dubaleko.vetclinic.repository.*;
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
    private final ReceptionDateService receptionDateService;
    private final ReceptionDateRepository receptionDateRepository;
    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;

    public EmployeeService(SpecializationRepository specializationRepository, EmployeeRepository employeeRepository,
                           WeekDayRepository weekDayRepository, ReceptionDateService receptionDateService,
                           ReceptionDateRepository receptionDateRepository, UserRepository userRepository,
                           ClinicRepository clinicRepository) {
        this.specializationRepository = specializationRepository;
        this.employeeRepository = employeeRepository;
        this.weekDayRepository = weekDayRepository;
        this.receptionDateService = receptionDateService;
        this.receptionDateRepository = receptionDateRepository;
        this.userRepository = userRepository;
        this.clinicRepository = clinicRepository;
    }

    public PagedListHolder<EmployeeDto> getEmployees(int page,Optional<Integer> size, Optional<Long> spec,
                                                     Optional<Long> clinic){
        List<Employee> employees = new ArrayList<>();
        if (spec.isPresent() && clinic.isPresent()){
            List<Employee> employeeList = specializationRepository.getById(spec.get()).getEmpls();
            for (Employee employee : employeeList){
                if (employee.getClinic().getId() == clinic.get()){
                    employees.add(employee);
                }
            }
        }
        else if (clinic.isPresent()){
            employees = employeeRepository.getByClinic(clinicRepository.getClinicById(clinic.get()));
        }
        else if (spec.isPresent()){
            employees = specializationRepository.getById(spec.get()).getEmpls();
        }
        else {
            employees = employeeRepository.findAll();
        }
        List<EmployeeDto> employeeDtos = employees.stream().map(user -> new ModelMapper().map(user, EmployeeDto.class)).
                collect(Collectors.toList());
        Collections.sort(employeeDtos, new EmployeeComparator());
        for (EmployeeDto employeeDto : employeeDtos){
            Collections.sort(employeeDto.getDays(),new DayComparator());
            Collections.sort(employeeDto.getSpecs(),new SpecsComparator());
        }
        PagedListHolder<EmployeeDto> pagedListHolder = new PagedListHolder<EmployeeDto>(employeeDtos);
        if (size.isEmpty())
            pagedListHolder.setPageSize(5);
        else
            pagedListHolder.setPageSize(size.get());
        pagedListHolder.setPage(page - 1);
        return pagedListHolder;
    }

    public List<EmployeeDto> getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = employees.stream().map(user -> new ModelMapper().map(user, EmployeeDto.class)).
                collect(Collectors.toList());
        Collections.sort(employeeDtos, new EmployeeComparator());
        return employeeDtos;
    }

    public void save(Employee employee){
        Set<WeekDay> weekDays = new HashSet<>();
        for (WeekDay weekDay : employee.getDays()){
            weekDays.add(weekDayRepository.getByDayName(weekDay.getDayName()));
        }
        employee.setDays(weekDays);
        employeeRepository.save(employee);

        List<ReceptionDate> receptionDates = receptionDateRepository.findAllByEmployee(employee);
        if (receptionDates.size() < 1){
            ArrayList <Employee> employees = new ArrayList<Employee>();
            employees.add(employee);
            receptionDateService.saveNewReceptionDateAndTime(7,employees);
        }
        else {
            List<WeekDayDto> newDays = weekDays.stream().map(user -> new ModelMapper().
                    map(user, WeekDayDto.class)).collect(Collectors.toList());
            Collections.sort(newDays,new DayComparator());
            List<WeekDayDto> oldDays =  receptionDateService.getDifferentDays(employee);
            List<WeekDayDto> addDays = checkDays(newDays,oldDays);
            List<WeekDayDto> deleteDays = checkDays(oldDays,newDays);
            if (addDays.size() != 0 || deleteDays.size() != 0) {
                receptionDateService.updateReceptionAndDate(addDays, deleteDays, employee);
            }
            Employee oldEmployee = employeeRepository.getOne(employee.getId());
            if (!employee.getStartWork().equals(oldEmployee.getStartWork())
                    || !employee.getEndWork().equals(oldEmployee.getEndWork())){
                receptionDateService.updateReceptionAndDate(newDays,oldDays,employee);
            }
        }
    }

    public void  deleteEmployeeById(Long id){
        List<User> users = userRepository.findAllByEmployee(employeeRepository.getOne(id));
        for (User user : users){
            user.setRole(Role.USER);
            user.setDoctor(null);
            userRepository.save(user);
        }
        employeeRepository.deleteById(id);
    }

    private List<WeekDayDto> checkDays(List<WeekDayDto> firstSet, List<WeekDayDto> secondSet){
        List<WeekDayDto> days = new ArrayList<>();
        for (WeekDayDto weekDayDto : firstSet){
            if (secondSet.contains(weekDayDto)){
                continue;
            }
            else {
                days.add(weekDayDto);
            }
        }
        return days;
    }
}
