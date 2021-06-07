package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.entity.PetEvent;
import com.dubaleko.vetclinic.repository.PetEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/petEvent")
public class PetEventController {
    private final PetEventRepository petEventRepository;

    public PetEventController(PetEventRepository petEventRepository) {
        this.petEventRepository = petEventRepository;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PetEvent> getPetEvents(@RequestParam Long id, @RequestParam int page){
        return petEventRepository.findAllEventByUserId(id,
                PageRequest.of(page - 1, 18, Sort.Direction.DESC, "date"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveNewEvent(@RequestBody PetEvent petEvent){
        petEventRepository.save(petEvent);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateEvent(@RequestBody PetEvent petEvent){
        petEventRepository.save(petEvent);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvent(@RequestParam Long id){
        petEventRepository.deleteById(id);
    }
}
