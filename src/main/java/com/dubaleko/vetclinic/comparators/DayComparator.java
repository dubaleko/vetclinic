package com.dubaleko.vetclinic.comparators;

import com.dubaleko.vetclinic.dto.WeekDayDto;

import java.util.Comparator;

public class DayComparator implements Comparator<WeekDayDto> {
    @Override
    public int compare(WeekDayDto employeeDto, WeekDayDto t1) {
        return employeeDto.getId().compareTo(t1.getId());
    }
}
