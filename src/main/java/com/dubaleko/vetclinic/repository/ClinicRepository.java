package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.Clinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    @Query("select c from Clinic c where city like %?1%")
    Page<Clinic> findClinicByPage(String city, Pageable pageable);
    @Query(value = "select distinct ltrim(rtrim(city)) from Clinic", nativeQuery = true)
    List<String> findCityClinics();
    @Query(value = "select * from clinic order by random() limit 2", nativeQuery = true)
    List<Clinic> findTwoClinic();
    @Query("select c from Clinic c where id =?1")
    Clinic getClinicById(Long id);
}
