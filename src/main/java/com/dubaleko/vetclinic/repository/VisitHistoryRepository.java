package com.dubaleko.vetclinic.repository;

import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.User;
import com.dubaleko.vetclinic.entity.VisitHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VisitHistoryRepository extends JpaRepository<VisitHistory,Long> {
    List<VisitHistory> findAllByUser(User user);
    List<VisitHistory> findAllByEmployee(Employee employee);
}
