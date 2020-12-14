package com.dubaleko.vetclinic.service;

import com.dubaleko.vetclinic.comparators.DateComparator;
import com.dubaleko.vetclinic.comparators.TimeComparator;
import com.dubaleko.vetclinic.dto.EmployeeDto;
import com.dubaleko.vetclinic.dto.ReceptionDateDto;
import com.dubaleko.vetclinic.dto.ReceptionTimeDto;
import com.dubaleko.vetclinic.dto.WeekDayDto;
import com.dubaleko.vetclinic.entity.Employee;
import com.dubaleko.vetclinic.entity.ReceptionDate;
import com.dubaleko.vetclinic.entity.ReceptionTime;
import com.dubaleko.vetclinic.entity.WeekDay;
import com.dubaleko.vetclinic.repository.EmployeeRepository;
import com.dubaleko.vetclinic.repository.ReceptionDateRepository;
import com.dubaleko.vetclinic.repository.ReceptionTimeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<ReceptionDateDto> getAllDateByEmployeeId(Long id){
        List<ReceptionDate> receptionDates = receptionDateRepository.findAllByEmployee(employeeRepository.getOne(id));
        EmployeeDto employeeDto = new EmployeeDto();
        new ModelMapper().map(receptionDates.get(0).getEmployee(),employeeDto);
        List<ReceptionDateDto> receptionDateDtos = receptionDates.stream().map(user -> new ModelMapper().
                map(user, ReceptionDateDto.class)).collect(Collectors.toList());
        for (ReceptionDateDto receptionDateDto : receptionDateDtos){
            receptionDateDto.setEmployeeDto(employeeDto);
        }
        Collections.sort(receptionDateDtos, new DateComparator());
        return receptionDateDtos;
    }

    public List<ReceptionTimeDto> getAllTimeByDateId(Long id) {
        List<ReceptionTime> receptionTimes = receptionTimeRepository.findAllByDate(receptionDateRepository.getOne(id));
        EmployeeDto employeeDto = new EmployeeDto();
        ReceptionDateDto receptionDateDto = new ReceptionDateDto();
        new ModelMapper().map(receptionTimes.get(0).getDate(),receptionDateDto);
        new ModelMapper().map(receptionTimes.get(0).getDate().getEmployee(),employeeDto);
        receptionDateDto.setEmployeeDto(employeeDto);
        List<ReceptionTimeDto> receptionTimeDtos = receptionTimes.stream().map(user -> new ModelMapper().
                map(user, ReceptionTimeDto.class)).collect(Collectors.toList());
        for (ReceptionTimeDto receptionTimeDto : receptionTimeDtos){
            receptionTimeDto.setReceptionDateDto(receptionDateDto);
        }
        Collections.sort(receptionTimeDtos, new TimeComparator());
        return  receptionTimeDtos;
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
                    date = localDate.plusDays(6);
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
        for (int j = 0; j < 21; j++){
            Time time = new Time(21600000 + 1800000 * j);
            boolean flag = false;
            if (LocalDate.now().equals(receptionDate.getDate().toLocalDate())){
                if (LocalTime.now().plusMinutes(10).isAfter(time.toLocalTime())){
                    flag = true;
                }
            }
            ReceptionTime receptionTime = new ReceptionTime(null, time,flag,receptionDate);
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
