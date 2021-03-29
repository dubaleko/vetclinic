package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.entity.Service;
import com.dubaleko.vetclinic.repository.ServiceRepository;
import com.dubaleko.vetclinic.service.ServiceForService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ResponseStatus(HttpStatus.OK)
    public Page list(@RequestParam int page, @RequestParam String type){
        return  service.getServices(page,type);
    }

    @GetMapping("preview")
    @ResponseStatus(HttpStatus.OK)
    public List<Service> getFive(){
        return serviceRepository.findSixService();
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
        serviceRepository.deleteById(id);
    }
}
