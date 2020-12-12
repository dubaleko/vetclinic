package com.dubaleko.vetclinic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.websocket.OnMessage;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ClinicOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    User user;
    @ManyToOne(optional = false)
    @JoinColumn(name= "employee")
    Employee employee;
    @ManyToOne(optional = false)
    @JoinColumn(name = "date")
    ReceptionDate receptionDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "time")
    ReceptionTime receptionTime;
}
