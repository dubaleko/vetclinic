package com.dubaleko.vetclinic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "clinic")
    private Clinic clinic;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee_spec",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "spec_id")
    )
    private Set<Specialization> specs;
    private Boolean onVacation;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "employee_work_days",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "day_id")
    )
    private Set<WeekDay> days;
    @ManyToOne
    @JoinColumn(name = "startWork")
    private WorkTime startWork;
    @ManyToOne
    @JoinColumn(name = "endWork")
    private WorkTime endWork;
}
