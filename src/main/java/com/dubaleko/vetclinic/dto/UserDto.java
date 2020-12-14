package com.dubaleko.vetclinic.dto;

import com.dubaleko.vetclinic.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String userName;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
