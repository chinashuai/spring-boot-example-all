package com.example.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CoreScheduleJob {


    @Scheduled(cron = "*/30 * * * * ?")
    public void coreExecute() {
        System.out.println("CoreScheduleJob The current time is : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));
    }


}
