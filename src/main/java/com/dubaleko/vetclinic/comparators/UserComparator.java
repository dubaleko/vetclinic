package com.dubaleko.vetclinic.comparators;

import com.dubaleko.vetclinic.dto.UserDto;

public class UserComparator implements java.util.Comparator<UserDto> {
    @Override
    public int compare(UserDto userDto, UserDto userDto1) {
        return userDto.getUserName().compareTo(userDto1.getUserName());
    }
}
