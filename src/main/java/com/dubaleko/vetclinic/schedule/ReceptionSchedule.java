package com.dubaleko.vetclinic.schedule;

import com.dubaleko.vetclinic.entity.ReceptionDate;
import com.dubaleko.vetclinic.entity.ReceptionTime;
import com.dubaleko.vetclinic.repository.ReceptionDateRepository;
import com.dubaleko.vetclinic.repository.ReceptionTimeRepository;
import com.dubaleko.vetclinic.service.ReceptionDateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ReceptionSchedule {

    private final ReceptionDateService receptionDateService;
    private final ReceptionDateRepository receptionDateRepository;
    private final ReceptionTimeRepository receptionTimeRepository;

    public ReceptionSchedule(ReceptionDateService receptionDateService, ReceptionDateRepository receptionDateRepository,
                             ReceptionTimeRepository receptionTimeRepository) {
        this.receptionDateService = receptionDateService;
        this.receptionDateRepository = receptionDateRepository;
        this.receptionTimeRepository = receptionTimeRepository;
    }

    @Scheduled(cron = "0 */5 * ? * *")
    public void updateTimeSchedule(){
        LocalDate localDate = LocalDate.now();
        List<ReceptionDate> receptionDates = receptionDateRepository.findAllByDate(java.sql.Date.valueOf(localDate));
        for (ReceptionDate receptionDate : receptionDates) {
            List<ReceptionTime> receptionTimes = receptionTimeRepository.findAllByDate(receptionDate);
            for (ReceptionTime receptionTime : receptionTimes){
                LocalTime localTime = LocalTime.now();
                localTime.plusMinutes(11);
                if (localTime.isAfter(receptionTime.getTime().toLocalTime())
                        && !receptionTime.getOccupied()) {
                    receptionTime.setOccupied(true);
                    receptionTimeRepository.save(receptionTime);
                }
            }
        }
    }

    @Scheduled(cron = "00 00 00 * * ?")
    public void updateReceptionSchedule(){
        if (receptionDateRepository.findAll().size() > 0){
            receptionDateService.deleteOldDateAndTime(Date.valueOf(LocalDate.now()));
            receptionDateService.saveNewReceptionDateAndTime(1,null);
        }
        else {
            receptionDateService.saveNewReceptionDateAndTime(7,null);
        }
    }
}
