package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.comparators.EmployeeComparator;
import com.dubaleko.vetclinic.comparators.UserComparator;
import com.dubaleko.vetclinic.dto.UserDto;
import com.dubaleko.vetclinic.entity.ClinicOrder;
import com.dubaleko.vetclinic.entity.User;
import com.dubaleko.vetclinic.entity.enums.Role;
import com.dubaleko.vetclinic.repository.OrderRepository;
import com.dubaleko.vetclinic.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import static org.springframework.security.core.userdetails.User.withUsername;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private UserRepository repository;
    private OrderService orderService;
    private OrderRepository orderRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserDetailServiceImpl(UserRepository repository, OrderService orderService,
                                 OrderRepository orderRepository) {
        this.repository = repository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        Optional<User> userOptional = repository.findByUserName(userName);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return buildUserDetailsForAuthentication(user);
        } else {
            throw new UsernameNotFoundException("Cannot find username " + userName);
        }
    }

    private UserDetails buildUserDetailsForAuthentication(User user) {
        UserBuilder builder = withUsername(user.getUserName());
        builder.password(user.getPassword());
        builder.roles(user.getRole().name());
        return builder.build();
    }

    public boolean saveUser(User user,final HttpServletResponse response) throws IOException {
        Optional<User> userOptional = repository.findByUserName(user.getUserName());
        if (!userOptional.isPresent()) {
            if (repository.findAll().size() == 0){
                user.setRole(Role.ADMIN);
            }
            else {
                user.setRole(Role.USER);
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            repository.save(user);
            return true;
        }
        else {
            response.sendError(HttpStatus.CONFLICT.value(),"Пользователь с таким именем уже существует");
            return false;
        }
    }

    public UserDto getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser")){
            return new UserDto();
        }
        else {
            User user =repository.findByUserName(((UserDetails)principal).getUsername()).get();
            return new ModelMapper().map(user, UserDto.class);
        }
    }

    public PagedListHolder<UserDto> getAllUsers(int page,Optional<String> name){
        List<User> users;
        if (name.isPresent()){
            users = repository.findAllByUserName(name.get());
        }
        else {
            users = repository.findAll();
        }
        List<UserDto> userDto = users.stream().map(user -> new ModelMapper().map(user, UserDto.class)).
                collect(Collectors.toList());
        Collections.sort(userDto, new UserComparator());
        PagedListHolder<UserDto> pagedListHolder = new PagedListHolder<UserDto>(userDto);
        pagedListHolder.setPageSize(19);
        pagedListHolder.setPage(page);
        return pagedListHolder;
    }

    public void deleteUserById(Long id){
        List<ClinicOrder> clinicOrders = orderRepository.findAllByUser(repository.getOne(id));
        for (ClinicOrder order : clinicOrders){
            orderService.refuseOrder(order.getId());
        }
        repository.deleteById(id);
    }
}
