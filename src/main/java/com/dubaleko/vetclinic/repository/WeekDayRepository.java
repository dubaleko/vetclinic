package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.WeekDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WeekDayRepository extends JpaRepository<WeekDay,Long> {
    @Query("select d from WeekDay d where d.dayName=?1")
    public WeekDay getByDayName(String dayName);
}
