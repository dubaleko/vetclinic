package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.ReceptionDate;
import com.dubaleko.vetclinic.entity.ReceptionTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceptionTimeRepository extends JpaRepository<ReceptionTime,Long> {
    @Query("select t from ReceptionTime t where date = ?1")
    public List<ReceptionTime> findAllByDate(ReceptionDate date);
}
