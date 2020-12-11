package com.dubaleko.vetclinic.comparators;

import com.dubaleko.vetclinic.dto.WeekDayDto;

public class DayComparator implements java.util.Comparator<WeekDayDto> {
    @Override
    public int compare(WeekDayDto employeeDto, WeekDayDto t1) {
        return employeeDto.getId().compareTo(t1.getId());
    }
}
