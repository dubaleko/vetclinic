package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.entity.ServiceType;
import com.dubaleko.vetclinic.repository.ServiceTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/servicetype")
public class ServiceTypeController {
    private final ServiceTypeRepository serviceTypeRepository;

    public ServiceTypeController(ServiceTypeRepository serviceTypeRepository) {
        this.serviceTypeRepository = serviceTypeRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ServiceType> list(){
        return serviceTypeRepository.findAll();
    }
}
