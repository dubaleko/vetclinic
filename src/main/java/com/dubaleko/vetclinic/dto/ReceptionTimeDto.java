package com.dubaleko.vetclinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceptionTimeDto {
    private Long id;
    private Boolean occupied;
    private Time time;
    private ReceptionDateDto receptionDateDto;
}
