package com.dubaleko.vetclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long id;
    private UserDto user;
    private EmployeeDto employeeDto;
    private ReceptionDateDto receptionDateDto;
    private ReceptionTimeDto receptionTimeDto;
}
