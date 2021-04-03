package com.dubaleko.vetclinic.entity;

import com.dubaleko.vetclinic.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne(optional = true)
    @JoinColumn(name = "clinic")
    Clinic clinic;
    @ManyToOne(optional = true)
    @JoinColumn(name = "doctor")
    Employee doctor;
}
