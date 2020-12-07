package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.dto.SpecializationDto;
import com.dubaleko.vetclinic.entity.Specialization;
import com.dubaleko.vetclinic.repository.SpecializationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/specialization")
public class SpecializationController {
    @Autowired
    private SpecializationRepository specializationRepository;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SpecializationDto> list(){
        List<Specialization> specializations= specializationRepository.findAll();
        return specializations.stream().map(user -> new ModelMapper().map(user, SpecializationDto.class)).
                collect(Collectors.toList());
    }
}
