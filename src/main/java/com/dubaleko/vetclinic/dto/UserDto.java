package com.dubaleko.vetclinic.dto;

import com.dubaleko.vetclinic.entity.Clinic;
import com.dubaleko.vetclinic.entity.PetEvent;
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
    private String petName;
    private String petType;
    private String petGender;
    private int petAge;
    private String ownerName;
    private String ownerSecondName;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Clinic clinic;
    private EmployeeDto doctor;
}
