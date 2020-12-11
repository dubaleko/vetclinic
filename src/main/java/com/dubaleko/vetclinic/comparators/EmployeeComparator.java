package com.dubaleko.vetclinic.comparators;

import com.dubaleko.vetclinic.dto.EmployeeDto;

public class EmployeeComparator implements java.util.Comparator<EmployeeDto> {
    @Override
    public int compare(EmployeeDto employeeDto, EmployeeDto t1) {
        return employeeDto.getName().compareTo(t1.getName());
    }
}
