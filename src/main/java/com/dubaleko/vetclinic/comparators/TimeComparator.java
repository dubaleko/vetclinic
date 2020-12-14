package com.dubaleko.vetclinic.comparators;

import com.dubaleko.vetclinic.dto.ReceptionTimeDto;

public class TimeComparator implements java.util.Comparator<ReceptionTimeDto> {
    @Override
    public int compare(ReceptionTimeDto receptionTimeDto, ReceptionTimeDto receptionTimeDto1){
        return receptionTimeDto.getTime().compareTo(receptionTimeDto1.getTime());
    }
}