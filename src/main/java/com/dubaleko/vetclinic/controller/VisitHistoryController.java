package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.dto.VisitHistoryDto;
import com.dubaleko.vetclinic.entity.VisitHistory;
import com.dubaleko.vetclinic.repository.UserRepository;
import com.dubaleko.vetclinic.repository.VisitHistoryRepository;
import com.dubaleko.vetclinic.service.OrderService;
import com.dubaleko.vetclinic.service.VisitHistoryService;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/visitHistory")
public class VisitHistoryController {
    private final VisitHistoryRepository visitHistoryRepository;
    private final VisitHistoryService visitHistoryService;
    private final UserRepository userRepository;
    private final OrderService orderService;

    public VisitHistoryController(VisitHistoryRepository visitHistoryRepository, UserRepository userRepository,
                                  VisitHistoryService visitHistoryService, OrderService orderService) {
        this.visitHistoryRepository = visitHistoryRepository;
        this.visitHistoryService = visitHistoryService;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedListHolder<VisitHistoryDto> getVisitHistory(@RequestParam Long id, @RequestParam int page){
        return visitHistoryService.getVisitHistoryForVisitLog(id, page);
    }

    @GetMapping("profile")
    @ResponseStatus(HttpStatus.OK)
    public PagedListHolder<VisitHistoryDto> getVisitHistoryForUser(@RequestParam Long id, @RequestParam int page){
        List<VisitHistory> visitHistories = visitHistoryRepository.findAllByUser(userRepository.getOne(id));
        return visitHistoryService.convertListToPage(visitHistories,page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void saveNewEvent(@RequestBody VisitHistory visitHistory, @RequestParam Long orderId){
        orderService.refuseOrder(orderId);
        visitHistoryRepository.save(visitHistory);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateEvent(@RequestBody VisitHistory visitHistory){
        visitHistoryRepository.save(visitHistory);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteEvent(@RequestParam Long id){
        visitHistoryRepository.deleteById(id);
    }
}
