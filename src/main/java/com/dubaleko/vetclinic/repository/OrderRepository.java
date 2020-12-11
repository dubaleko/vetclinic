package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.ClinicOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<ClinicOrder,Long> {
}
