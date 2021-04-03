package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.comparators.ClinicComparator;
import com.dubaleko.vetclinic.entity.Clinic;
import com.dubaleko.vetclinic.repository.ClinicRepository;
import com.dubaleko.vetclinic.service.ClinicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api/clinic")
public class ClinicController {
    private final ClinicRepository clinicRepository;
    private final ClinicService clinicService;

    public ClinicController(ClinicRepository clinicRepository, ClinicService clinicService) {
        this.clinicRepository = clinicRepository;
        this.clinicService = clinicService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Clinic> getClinics(@RequestParam int page, @RequestParam String city){
        if (city.equals("Все города")) {
            city = "";
        }
        return clinicRepository.findClinicByPage(city,
                PageRequest.of(page - 1, 5, Sort.Direction.ASC, "name"));
    }

    @GetMapping("cities")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getCityClinics(){
        return clinicRepository.findCityClinics();
    }

    @GetMapping("preview")
    @ResponseStatus(HttpStatus.OK)
    public List<Clinic> getPreviewClinics(){
        return clinicRepository.findTwoClinic();
    }

    @GetMapping("all")
    @ResponseStatus(HttpStatus.OK)
    public List<Clinic> getAllClinics(){
        List<Clinic> clinics = clinicRepository.findAll();
        Collections.sort(clinics, new ClinicComparator());
        return clinics;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public  void  addNewClinic(@RequestBody Clinic clinic){
        clinicRepository.save(clinic);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void edit(@RequestBody Clinic clinic){
        clinicRepository.save(clinic);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete(@RequestParam long id){
        clinicService.deleteClinicById(id);
    }
}
