package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.dto.UserDto;
import com.dubaleko.vetclinic.entity.User;
import com.dubaleko.vetclinic.repository.UserRepository;
import com.dubaleko.vetclinic.service.UserDetailServiceImpl;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    private  UserDetailServiceImpl userDetailService;
    private UserRepository userRepository;

    public UserController(UserDetailServiceImpl userDetailService, UserRepository userRepository) {
        this.userDetailService = userDetailService;
        this.userRepository = userRepository;
    }

    @GetMapping("current")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(){
        return userDetailService.getCurrentUser();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedListHolder<UserDto> getUsers(@RequestParam int page, @RequestParam Optional<String> name){
        return  userDetailService.getAllUsers(page,name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create(@RequestBody User user, final HttpServletResponse response) throws IOException {
        return  userDetailService.saveUser(user, response);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody User user){
        userRepository.save(user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@RequestParam Long id){
        userDetailService.deleteUserById(id);
    }
}
