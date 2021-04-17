package com.dubaleko.vetclinic.comparators;

import com.dubaleko.vetclinic.dto.VisitHistoryDto;

import java.util.Comparator;

public class VisitHistoryComparator implements Comparator<VisitHistoryDto> {
    @Override
    public int compare(VisitHistoryDto visit, VisitHistoryDto visit1) {
        return visit1.getDate().compareTo(visit.getDate());
    }
}
