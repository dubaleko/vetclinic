package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.Clinic;
import com.dubaleko.vetclinic.entity.Service;
import com.dubaleko.vetclinic.entity.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service,Long> {
    @Query("select s from Service s where type_id = ?1")
    Page<Service> findServiceByPage(Long typeId, Pageable pageable);
    @Query("select s from Service s")
    Page<Service> findAllServiceByPage(Pageable pageable);
    @Query(value = "select * from service order by random() limit 6", nativeQuery = true)
    List<Service> findSixService();
    Page<Service> findServiceByClinic(Clinic clinic,Pageable pageable);
    Page<Service> findServiceByServiceType(ServiceType serviceType, Pageable pageable);
    Page<Service> findServicesByClinicAndServiceType(Clinic clinic, ServiceType serviceType, Pageable pageable);
}
