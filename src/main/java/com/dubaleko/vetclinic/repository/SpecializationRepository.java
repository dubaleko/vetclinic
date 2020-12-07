package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.Specialization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecializationRepository extends JpaRepository<Specialization,Long> {
    Specialization getBySpecialization(String specialization);
}
