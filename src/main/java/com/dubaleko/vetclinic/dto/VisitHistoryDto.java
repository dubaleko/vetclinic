package com.dubaleko.vetclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitHistoryDto {
    private Long id;
    private Date date;
    private String article;
    private EmployeeDto employee;
    private UserDto user;
}
