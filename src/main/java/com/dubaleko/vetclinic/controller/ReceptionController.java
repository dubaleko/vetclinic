package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.dto.ReceptionDateDto;
import com.dubaleko.vetclinic.dto.ReceptionTimeDto;
import com.dubaleko.vetclinic.service.ReceptionDateService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reception")
public class ReceptionController {
    private final ReceptionDateService receptionDateService;

    public ReceptionController(ReceptionDateService receptionDateService){
        this.receptionDateService = receptionDateService;
    }

    @GetMapping("date/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReceptionDateDto> getDates(@PathVariable Long id){
        return  receptionDateService.getAllDateByEmployeeId(id);
    }

    @GetMapping("time/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ReceptionTimeDto> getTime(@PathVariable Long id){
        return  receptionDateService.getAllTimeByDateId(id);
    }
}
