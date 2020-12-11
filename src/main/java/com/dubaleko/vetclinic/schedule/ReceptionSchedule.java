package com.dubaleko.vetclinic.schedule;

import com.dubaleko.vetclinic.repository.ReceptionDateRepository;
import com.dubaleko.vetclinic.service.ReceptionDateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class ReceptionSchedule {

    private final ReceptionDateService receptionDateService;
    private final ReceptionDateRepository receptionDateRepository;

    public ReceptionSchedule(ReceptionDateService receptionDateService, ReceptionDateRepository receptionDateRepository) {
        this.receptionDateService = receptionDateService;
        this.receptionDateRepository = receptionDateRepository;
    }

    @Scheduled(cron = "00 9 04 * * ?")
    public void updateReceptionSchedule(){
        if (receptionDateRepository.findAll().size() > 0){
            receptionDateService.deleteOldDateAndTime(Date.valueOf(LocalDate.now()));
            receptionDateService.saveNewReceptionDateAndTime(1);
        }
        else {
            receptionDateService.saveNewReceptionDateAndTime(7);
        }
    }
}
