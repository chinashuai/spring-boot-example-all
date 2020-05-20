package com.example.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FixedRateScheduleJob {

    @Scheduled(fixedRate = 10000)
    public void coreExecute() {
        System.out.println("FixedRateScheduleJob The current time is : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    }

}
