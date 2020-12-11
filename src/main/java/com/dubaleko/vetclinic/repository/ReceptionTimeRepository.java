package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.ReceptionTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionTimeRepository extends JpaRepository<ReceptionTime,Long> {
}
