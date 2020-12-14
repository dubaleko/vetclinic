package com.dubaleko.vetclinic.comparators;

import com.dubaleko.vetclinic.dto.ReceptionDateDto;

public class DateComparator implements java.util.Comparator<ReceptionDateDto> {
    @Override
    public int compare(ReceptionDateDto receptionDateDto, ReceptionDateDto receptionDateDto1){
        return receptionDateDto.getDate().compareTo(receptionDateDto1.getDate());
    }
}
