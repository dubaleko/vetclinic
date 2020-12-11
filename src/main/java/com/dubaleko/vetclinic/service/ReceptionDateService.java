package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.ReceptionDate;
import com.dubaleko.vetclinic.entity.ReceptionTime;
import com.dubaleko.vetclinic.entity.WeekDay;
import com.dubaleko.vetclinic.repository.EmployeeRepository;
import com.dubaleko.vetclinic.repository.ReceptionDateRepository;
import com.dubaleko.vetclinic.repository.ReceptionTimeRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class ReceptionDateService {
    private final ReceptionDateRepository receptionDateRepository;
    private final ReceptionTimeRepository receptionTimeRepository;
    private final EmployeeRepository employeeRepository;

    public ReceptionDateService(ReceptionDateRepository receptionDateRepository,
                                ReceptionTimeRepository receptionTimeRepository, EmployeeRepository employeeRepository){
        this.receptionDateRepository = receptionDateRepository;
        this.receptionTimeRepository = receptionTimeRepository;
        this.employeeRepository = employeeRepository;
    }

    public void saveNewReceptionDateAndTime(int count){
        LocalDate localDate = LocalDate.now();

        for (Employee employee : employeeRepository.findAll()){
            for (int i = 0; i < count; i++) {
                LocalDate date;
                if (count == 1) {
                    date = localDate.plusDays(7);
                }
                else {
                    date = localDate.plusDays(i);
                }
                String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL,
                        new Locale("ru", "RU"));
                for (WeekDay day : employee.getDays()){
                    if (day.getDayName().toLowerCase().equals(dayOfWeek)){
                        ReceptionDate receptionDate = new ReceptionDate(null, java.sql.Date.valueOf(date), employee);
                        receptionDateRepository.save(receptionDate);
                        for (int j = 0; j < 23; j++){
                            ReceptionTime receptionTime = new ReceptionTime(null,
                                    new Time(21600000 + 1800000 * j),false,receptionDate);
                            receptionTimeRepository.save(receptionTime);
                        }
                    }
                }
            }
        }
    }

    public void deleteOldDateAndTime(Date date){
        List<ReceptionDate> receptionDates = receptionDateRepository.findAllByDateBefore(date);
        for (ReceptionDate receptionDate  : receptionDates){
            receptionDateRepository.delete(receptionDate);
        }
    }
}
