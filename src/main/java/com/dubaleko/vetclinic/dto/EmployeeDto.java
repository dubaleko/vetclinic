package com.dubaleko.vetclinic.dto;

import com.dubaleko.vetclinic.entity.Clinic;
import com.dubaleko.vetclinic.entity.WorkTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String name;
    private String education;
    private String position;
    private Clinic clinic;
    private List<SpecializationDto> specs;
    private Boolean onVacation;
    private List<WeekDayDto> days;
    private WorkTime startWork;
    private WorkTime endWork;
}
