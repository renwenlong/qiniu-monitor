package com.test.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.test.app.service.Job;


/**
 * Created by renwenlong on 16/6/17.
 */
@RestController
public class MyAppController {

    @Autowired
    private Job job;

    @RequestMapping("/trigger")
    String myApp() throws Exception {
        job.execute();
        return "已触发定时任务.";
    }


}
