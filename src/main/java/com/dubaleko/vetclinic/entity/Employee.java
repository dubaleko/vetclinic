package com.dubaleko.vetclinic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String position;
    private String education;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee_spec",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "spec_id")
    )
    private Set<Specialization> specs;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee_work_days",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "day_id")
    )
    private Set<WeekDay> days;
}
