package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.entity.Service;
import com.dubaleko.vetclinic.repository.ServiceRepository;
import com.dubaleko.vetclinic.service.ServiceForService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/service")
public class ServiceController {
    private final ServiceRepository serviceRepository;
    private final ServiceForService service;

    public ServiceController(ServiceRepository serviceRepository, ServiceForService service) {
        this.serviceRepository = serviceRepository;
        this.service = service;
    }

    @GetMapping
    public Page list(@RequestParam int page, @RequestParam Optional<String> type){
        return  service.getServices(page,type);
    }

    @GetMapping("preview")
    public Page<Service> getFive(){
        return serviceRepository.findAll(PageRequest.of(0, 6,
                Sort.by(Sort.Direction.ASC, "serviceName")));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Service service)  {
        serviceRepository.save(service);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void edit(@RequestBody Service service){
        serviceRepository.save(service);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam long id){
        serviceRepository.delete(serviceRepository.getOne(id));
    }
}
