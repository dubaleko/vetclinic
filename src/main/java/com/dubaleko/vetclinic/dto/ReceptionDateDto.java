package com.dubaleko.vetclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionDateDto {
    private Long id;
    private Date date;
    private EmployeeDto employeeDto;
}
