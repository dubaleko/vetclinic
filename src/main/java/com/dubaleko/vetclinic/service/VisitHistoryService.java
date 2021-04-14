package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.comparators.VisitHistoryComparator;
import com.dubaleko.vetclinic.dto.VisitHistoryDto;
import com.dubaleko.vetclinic.entity.Clinic;
import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.User;
import com.dubaleko.vetclinic.entity.VisitHistory;
import com.dubaleko.vetclinic.entity.enums.Role;
import com.dubaleko.vetclinic.repository.EmployeeRepository;
import com.dubaleko.vetclinic.repository.UserRepository;
import com.dubaleko.vetclinic.repository.VisitHistoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VisitHistoryService {
    private final VisitHistoryRepository visitHistoryRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    public VisitHistoryService(VisitHistoryRepository visitHistoryRepository,
                               UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.visitHistoryRepository = visitHistoryRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public PagedListHolder<VisitHistoryDto> getVisitHistoryForVisitLog(Long id, int page){
            List<VisitHistory> visitHistories = new ArrayList<>();
            User currentUser = userRepository.getOne(id);
            if (currentUser.getRole().equals(Role.ADMIN)){
                visitHistories = visitHistoryRepository.findAll();
            }
            else {
                Clinic clinic;
                if (currentUser.getRole().equals(Role.MODERATOR)){
                    clinic = currentUser.getClinic();
                }
                else {
                    clinic = currentUser.getDoctor().getClinic();
                }
                List<Employee> employeeList = employeeRepository.getByClinic(clinic);
                for (Employee employee : employeeList){
                    visitHistories.addAll(visitHistoryRepository.findAllByEmployee(employee));
                }
            }
            return convertListToPage(visitHistories,page);
    }

    public PagedListHolder<VisitHistoryDto> convertListToPage(List<VisitHistory> visitHistories, int page){
        List<VisitHistoryDto> visitHistoryDtos = visitHistories.stream()
                .map(user -> new ModelMapper().map(user, VisitHistoryDto.class)).collect(Collectors.toList());
        Collections.sort(visitHistoryDtos, new VisitHistoryComparator());
        PagedListHolder<VisitHistoryDto> pagedListHolder = new PagedListHolder<VisitHistoryDto>(visitHistoryDtos);
        pagedListHolder.setPageSize(18);
        pagedListHolder.setPage(page - 1);
        return pagedListHolder;
    }
}
