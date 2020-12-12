package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.comparators.DayComparator;
import com.dubaleko.vetclinic.comparators.EmployeeComparator;
import com.dubaleko.vetclinic.dto.EmployeeDto;
import com.dubaleko.vetclinic.dto.WeekDayDto;
import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.ReceptionDate;
import com.dubaleko.vetclinic.entity.WeekDay;
import com.dubaleko.vetclinic.repository.EmployeeRepository;
import com.dubaleko.vetclinic.repository.ReceptionDateRepository;
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
    private final ReceptionDateService receptionDateService;
    private final ReceptionDateRepository receptionDateRepository;
    private String specializationName = "Empty";

    public EmployeeService(SpecializationRepository specializationRepository, EmployeeRepository employeeRepository,
                           WeekDayRepository weekDayRepository, ReceptionDateService receptionDateService,
                           ReceptionDateRepository receptionDateRepository) {
        this.specializationRepository = specializationRepository;
        this.employeeRepository = employeeRepository;
        this.weekDayRepository = weekDayRepository;
        this.receptionDateService = receptionDateService;
        this.receptionDateRepository = receptionDateRepository;
    }

    public PagedListHolder<EmployeeDto> getEmployees(int page, Optional<String> spec,Optional<Integer> size){
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
        if (size.isEmpty())
            pagedListHolder.setPageSize(5);
        else
            pagedListHolder.setPageSize(size.get());
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
            if (addDays.size() != 0 || deleteDays.size() != 0)
                receptionDateService.updateReceptionAndDate(addDays,deleteDays,employee);
        }
    }

    public List<EmployeeDto> getPreview(){
        List<Employee> employees = employeeRepository.findTopByName();
        return employees.stream().map(user -> new ModelMapper().map(user, EmployeeDto.class)).
                collect(Collectors.toList());
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
