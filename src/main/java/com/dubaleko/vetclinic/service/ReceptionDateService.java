package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.dto.WeekDayDto;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ReceptionDateService {
    private final ReceptionDateRepository receptionDateRepository;
    private final ReceptionTimeRepository receptionTimeRepository;
    private final EmployeeRepository employeeRepository;
    Locale local = new Locale("ru", "RU");

    public ReceptionDateService(ReceptionDateRepository receptionDateRepository,
                                ReceptionTimeRepository receptionTimeRepository, EmployeeRepository employeeRepository){
        this.receptionDateRepository = receptionDateRepository;
        this.receptionTimeRepository = receptionTimeRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<WeekDayDto> getDifferentDays(Employee employee){
        List<ReceptionDate> receptionDates = receptionDateRepository.findAllByEmployee(employee);
        List<WeekDayDto> weekDayDtos = new ArrayList<WeekDayDto>();
        for (ReceptionDate receptionDate : receptionDates){
            DayOfWeek dayOfWeek = receptionDate.getDate().toLocalDate().getDayOfWeek();
            String dayName = dayOfWeek.getDisplayName(TextStyle.FULL, local);
            weekDayDtos.add(new WeekDayDto((long) dayOfWeek.getValue(),
                    dayName.substring(0,1).toUpperCase()+dayName.substring(1)));
        }
        return weekDayDtos;
    }

    public void updateReceptionAndDate(List<WeekDayDto> add,List<WeekDayDto> delete, Employee employee){
        List<ReceptionDate> receptionDates = receptionDateRepository.findAllByEmployee(employee);
        for (ReceptionDate receptionDate : receptionDates){
            DayOfWeek dayOfWeek = receptionDate.getDate().toLocalDate().getDayOfWeek();
            String dayName = dayOfWeek.getDisplayName(TextStyle.FULL, local);
            WeekDayDto weekDayDto = new WeekDayDto((long) dayOfWeek.getValue(),
                    dayName.substring(0,1).toUpperCase()+dayName.substring(1));
            if (delete.contains(weekDayDto)){
                receptionDateRepository.delete(receptionDate);
            }
        }
        for (WeekDayDto weekDayDto : add){
            LocalDate localDate = LocalDate.now();
            while (weekDayDto.getId() != localDate.getDayOfWeek().getValue()) {
                localDate = localDate.plusDays(1);
            }
            saveDateAndTime(localDate,employee);
        }
    }

    public void saveNewReceptionDateAndTime(int count, List<Employee> employees){
        LocalDate localDate = LocalDate.now();
        employees = employees != null ? employees :employeeRepository.findAll();
        for (Employee employee : employees){
            for (int i = 0; i < count; i++) {
                LocalDate date;
                if (count == 1) {
                    date = localDate.plusDays(7);
                }
                else {
                    date = localDate.plusDays(i);
                }
                String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL, local);
                for (WeekDay day : employee.getDays()){
                    if (day.getDayName().toLowerCase().equals(dayOfWeek)){
                        saveDateAndTime(date,employee);
                    }
                }
            }
        }
    }

    private  void saveDateAndTime(LocalDate date, Employee employee){
        ReceptionDate receptionDate = new ReceptionDate(null, java.sql.Date.valueOf(date), employee);
        receptionDateRepository.save(receptionDate);
        for (int j = 0; j < 23; j++){
            ReceptionTime receptionTime = new ReceptionTime(null,
                    new Time(21600000 + 1800000 * j),false,receptionDate);
            receptionTimeRepository.save(receptionTime);
        }
    }

    public void deleteOldDateAndTime(Date date){
        List<ReceptionDate> receptionDates = receptionDateRepository.findAllByDateBefore(date);
        for (ReceptionDate receptionDate  : receptionDates){
            receptionDateRepository.delete(receptionDate);
        }
    }
}
