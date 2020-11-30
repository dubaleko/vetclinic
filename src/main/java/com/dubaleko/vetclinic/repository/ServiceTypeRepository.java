package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceType,Long> {
}
