package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ServiceTypeRepository extends JpaRepository<ServiceType,Long> {
    @Query("select s from ServiceType s where id =?1")
    ServiceType getServiceTypeById(Long id);
}
