package com.test.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.test.app.service.trigger.TriggerOne;


/**
 * Created by renwenlong on 16/6/20.
 */
@Component
public class Job {

    @Autowired
    private TriggerOne triggerOne;

    @Scheduled(cron = "0 0 01 * * *")
    public void execute() throws Exception {
       triggerOne.execute();
    }

}
