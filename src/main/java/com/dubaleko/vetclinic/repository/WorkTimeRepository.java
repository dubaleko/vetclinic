package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.WorkTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkTimeRepository extends JpaRepository<WorkTime,Long> {
}
