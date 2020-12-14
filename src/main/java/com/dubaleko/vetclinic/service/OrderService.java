package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.comparators.OrderComparator;
import com.dubaleko.vetclinic.dto.*;
import com.dubaleko.vetclinic.entity.ClinicOrder;
import com.dubaleko.vetclinic.entity.ReceptionTime;
import com.dubaleko.vetclinic.entity.User;
import com.dubaleko.vetclinic.entity.enums.Role;
import com.dubaleko.vetclinic.repository.OrderRepository;
import com.dubaleko.vetclinic.repository.ReceptionTimeRepository;
import com.dubaleko.vetclinic.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ReceptionTimeRepository receptionTimeRepository;

    public OrderService(UserRepository userRepository, OrderRepository orderRepository,
                        ReceptionTimeRepository receptionTimeRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.receptionTimeRepository = receptionTimeRepository;
    }

    public PagedListHolder<OrderDto> getAllOrdersByUserId(long id, int  page){
        List<ClinicOrder> clinicOrders;
        User user = userRepository.getOne(id);
        if (user.getRole().equals(Role.ADMIN)){
            clinicOrders = orderRepository.findAll();
        }
        else{
            clinicOrders = orderRepository.findAllByUser(userRepository.getOne(id));
        }
        List<OrderDto> orderDtos = getDtosList(clinicOrders);
        PagedListHolder<OrderDto> pagedListHolder = new PagedListHolder<OrderDto>(orderDtos);
        pagedListHolder.setPageSize(15);
        pagedListHolder.setPage(page - 1);
        return pagedListHolder;
    }

    private List<OrderDto> getDtosList(List<ClinicOrder> clinicOrders){
        List<OrderDto> orderDtos = new ArrayList<>();
        for (ClinicOrder clinicOrder : clinicOrders){
            EmployeeDto employeeDto = new EmployeeDto();
            new ModelMapper().map(clinicOrder.getEmployee(),employeeDto);
            ReceptionDateDto receptionDateDto = new ReceptionDateDto();
            new ModelMapper().map(clinicOrder.getReceptionDate(),receptionDateDto);
            ReceptionTimeDto receptionTimeDto = new ReceptionTimeDto();
            new ModelMapper().map(clinicOrder.getReceptionTime(),receptionTimeDto);
            UserDto userDto = new UserDto();
            new ModelMapper().map(clinicOrder.getUser(),userDto);
            OrderDto orderDto = new OrderDto(clinicOrder.getId(),userDto,employeeDto,
                    receptionDateDto,receptionTimeDto);
            orderDtos.add(orderDto);
        }
        Collections.sort(orderDtos, new OrderComparator());
        return  orderDtos;
    }

    public  void  refuseOrder(long id){
        LocalDate  localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        ClinicOrder clinicOrder = orderRepository.getOne(id);
        if (localDate.isBefore(clinicOrder.getReceptionDate().getDate().toLocalDate())){
            uncheckOccupied(clinicOrder.getReceptionTime());
        }
        else if (localTime.isBefore(clinicOrder.getReceptionTime().getTime().toLocalTime())){
            uncheckOccupied(clinicOrder.getReceptionTime());
        }
        orderRepository.delete(clinicOrder);
    }

    private void uncheckOccupied(ReceptionTime receptionTime){
        receptionTime.setOccupied(false);
        receptionTimeRepository.save(receptionTime);
    }

    public boolean saveOrder(ClinicOrder clinicOrder, final HttpServletResponse response) throws IOException {
        ReceptionTime receptionTime = receptionTimeRepository.getOne(clinicOrder.getReceptionTime().getId());
        if (!receptionTime.getOccupied()){
            receptionTime = clinicOrder.getReceptionTime();
            receptionTime.setOccupied(true);
            receptionTime.setDate(clinicOrder.getReceptionDate());
            receptionTimeRepository.save(receptionTime);
            clinicOrder.getReceptionDate().setEmployee(clinicOrder.getEmployee());
            orderRepository.save(clinicOrder);
            return true;
        }
        else {
            response.sendError(HttpStatus.CONFLICT.value(),"Это место больше не доступно для записи");
            return false;
        }
    }
}
