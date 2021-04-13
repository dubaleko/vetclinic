package com.dubaleko.vetclinic.entity;

import com.dubaleko.vetclinic.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private String petName;
    private String petType;
    private String petGender;
    private int petAge;
    private String ownerName;
    private String ownerSecondName;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    @JoinColumn(name = "clinic")
    private Clinic clinic;
    @ManyToOne
    @JoinColumn(name = "doctor")
    private Employee doctor;
}
