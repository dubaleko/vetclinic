package com.dubaleko.vetclinic.comparators;

import com.dubaleko.vetclinic.dto.OrderDto;

public class OrderComparator implements java.util.Comparator<OrderDto> {
    @Override
    public int compare(OrderDto orderDto, OrderDto t1) {
        return orderDto.getReceptionDateDto().getDate().compareTo(t1.getReceptionDateDto().getDate());
    }
}