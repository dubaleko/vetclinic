package com.dubaleko.vetclinic.comparators;

import com.dubaleko.vetclinic.dto.SpecializationDto;

import java.util.Comparator;

public class SpecsComparator implements Comparator<SpecializationDto> {
    @Override
    public int compare(SpecializationDto spec, SpecializationDto spec1) {
        return spec.getName().compareTo(spec1.getName());
    }
}
