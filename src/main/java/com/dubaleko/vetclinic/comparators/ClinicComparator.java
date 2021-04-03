package com.dubaleko.vetclinic.comparators;

import com.dubaleko.vetclinic.entity.Clinic;

import java.util.Comparator;

public class ClinicComparator implements Comparator<Clinic> {
    @Override
    public int compare(Clinic clinic, Clinic clinic1) {
        return clinic.getName().compareTo(clinic1.getName());
    }
}
