package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.PetEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PetEventRepository extends JpaRepository<PetEvent,Long> {
    @Query(value = "select * from pet_event where user_id = ?1", nativeQuery = true)
    Page<PetEvent> findAllEventByUserId(Long userId, Pageable pageable);
}
