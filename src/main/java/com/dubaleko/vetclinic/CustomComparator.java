package com.dubaleko.vetclinic;

import com.dubaleko.vetclinic.dto.EmployeeDto;

public class CustomComparator implements java.util.Comparator<EmployeeDto> {
    @Override
    public int compare(EmployeeDto employeeDto, EmployeeDto t1) {
        return employeeDto.getName().compareTo(t1.getName());
    }
}
