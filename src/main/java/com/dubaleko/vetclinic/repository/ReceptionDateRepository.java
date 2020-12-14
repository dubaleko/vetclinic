package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.ReceptionDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface ReceptionDateRepository extends JpaRepository<ReceptionDate,Long> {
    public List<ReceptionDate> findAllByDateBefore(Date date);
    @Query(value = "select  * from reception_date order by date desc limit 1",nativeQuery = true)
    public ReceptionDate findBottomByDate (Date date);
    public List<ReceptionDate> findAllByEmployee(Employee employee);
    public List<ReceptionDate> findAllByDate(Date receptionDate);
}
