package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.entity.User;
import com.dubaleko.vetclinic.entity.enums.Role;
import com.dubaleko.vetclinic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private UserRepository repository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserDetailServiceImpl(UserRepository repository) {
        this.repository = repository;
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
            user.setRole(Role.USER);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            repository.save(user);
            return true;
        }
        else {
            response.sendError(HttpStatus.CONFLICT.value(),"Пользователь с таким именем уже существует");
            return false;
        }
    }

    public User getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.equals("anonymousUser")){
            return new User();
        }
        else {
            return repository.findByUserName(((UserDetails)principal).getUsername()).get();
        }
    }
}
