package com.dubaleko.vetclinic.dto;

import com.dubaleko.vetclinic.entity.WeekDay;
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
    private List<SpecializationDto> specs;
    private List<WeekDayDto> days;
}
