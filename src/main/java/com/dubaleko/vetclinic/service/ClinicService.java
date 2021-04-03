package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.entity.User;
import com.dubaleko.vetclinic.entity.enums.Role;
import com.dubaleko.vetclinic.repository.ClinicRepository;
import com.dubaleko.vetclinic.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClinicService {
    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;

    public ClinicService(ClinicRepository clinicRepository, UserRepository userRepository) {
        this.clinicRepository = clinicRepository;
        this.userRepository = userRepository;
    }

    public void deleteClinicById(Long id){
        List<User> users = userRepository.findAllByClinic(clinicRepository.getOne(id));
        for (User user : users){
            user.setRole(Role.USER);
            user.setClinic(null);
            userRepository.save(user);
        }
        clinicRepository.deleteById(id);
    }
}
