package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.dto.OrderDto;
import com.dubaleko.vetclinic.entity.ClinicOrder;
import com.dubaleko.vetclinic.service.OrderService;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedListHolder<OrderDto> getOrders(@RequestParam long id, @RequestParam int page){
        return orderService.getAllOrdersByUserId(id,page);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public  boolean  saveOrder(@RequestBody ClinicOrder clinicOrder, final HttpServletResponse response) throws IOException {
        return  orderService.saveOrder(clinicOrder, response);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void  refuseOrder(@RequestParam long id){
        orderService.refuseOrder(id);
    }
}
