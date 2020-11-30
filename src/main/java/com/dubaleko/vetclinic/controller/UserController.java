package com.dubaleko.vetclinic.controller;

import com.dubaleko.vetclinic.entity.User;
import com.dubaleko.vetclinic.service.UserDetailServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserDetailServiceImpl userDetailService;

    public UserController(UserDetailServiceImpl userDetailService) {

        this.userDetailService = userDetailService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public User getUser(){
        return userDetailService.getCurrentUser();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create(@RequestBody User user, final HttpServletResponse response) throws IOException {
        return  userDetailService.saveUser(user, response);
    }
}
